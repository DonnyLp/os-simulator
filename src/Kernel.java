import java.util.Arrays;
import java.util.Random;

public class Kernel extends Process implements Device{
  private final Scheduler scheduler;
  private final VFS fileSystem;
  private boolean[] memoryMap;
  private Random rand;
  public Kernel() {
    this.scheduler = new Scheduler(this);
    this.fileSystem = new VFS();
    this.memoryMap = new boolean[100];
    this.rand = new Random();
    Arrays.fill(memoryMap, false);
  }

  @Override
  public void main() {
      while(true) {
          switch(OS.currentCall) {
              case createProcess -> {
                  UserlandProcess newProcess = (UserlandProcess) OS.parameters.getFirst();
                  OS.Priority priority = (OS.Priority) OS.parameters.getLast();
                  OS.returnValue = this.scheduler.createProcess(newProcess, priority);
              }
              case open -> {
                  OS.returnValue = open((String) OS.parameters.getFirst());
              }
              case read -> {
                  OS.returnValue = read((int) OS.parameters.getFirst(), (int) OS.parameters.getLast());
              }
              case seek -> seek((int) OS.parameters.getFirst(), (int) OS.parameters.getLast());
              case write -> {
                  OS.returnValue = write((int) OS.parameters.getFirst(), (byte[]) OS.parameters.getLast());
              }
              case getPID -> OS.returnValue = this.scheduler.getPID();
              case getPIDByName -> OS.returnValue = this.scheduler.getPIDByName((String) OS.parameters.getFirst());
              case close -> close((int) OS.parameters.getFirst());
              case switchProcess -> this.scheduler.switchProcess();
              case sleep -> this.scheduler.sleep((int)OS.parameters.getFirst());
              case sendMessage -> sendMessage((KernelMessage) OS.parameters.getFirst());
              case waitForMessage -> OS.returnValue = waitForMessage();
              case getMapping -> getMapping((int) OS.parameters.getFirst());
              case allocateMemory -> OS.returnValue = allocateMemory((int) OS.parameters.getFirst());
              case freeMemory -> OS.returnValue = freeMemory((int) OS.parameters.getFirst(), (int) OS.parameters.getLast());
              case exit -> this.scheduler.exit();
          }
          this.scheduler.currentUserProcess.start();
          try {
              this.stop();
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      }
  }

    /**
     * Send a message to another process
     * @param message the message to be sent to the receiver
     */
    public void sendMessage(KernelMessage message) {
        KernelMessage messageCopy = new KernelMessage(message); //what is being done with the message copy
        messageCopy.setSenderPID(getCurrentUserProcess().getPID());
        PCB targetPCB = this.scheduler.getProcessByID(message.getReceiverPID());

        if(targetPCB == null) {
            throw new KernelException("PCB with PID: " + messageCopy.getReceiverPID() + " doesn't exist.");
        }

        //add message to target's queue
        targetPCB.queueMessage(messageCopy);

        //restore targetPCB to runnable queue if it's waiting for a message
        if(this.scheduler.isWaitingForMessage(targetPCB)) {
            this.scheduler.removeFromMessageQueue(targetPCB);
            this.scheduler.addProcess(targetPCB);
        }
    }

    /**
     * Handles the current process' message
     * if there isn't one then current process is added to a waiting queue
     * @return the current process' message
     */
    public KernelMessage waitForMessage() {
        KernelMessage message = null;
        //check if the current process has a message in the queue
        message = getCurrentUserProcess().getMessage();
        if(message == null) {
            //deschedules from current runnable queue and adds to waiting queue
            this.scheduler.addToMessageQueue();
        }
        return message;
    }


  /**
   * Checks if there is any processes running
   * @return true if there is a process running, false otherwise
   * */
  public boolean isProcessRunning() {
      return this.scheduler.currentUserProcess != null;
  }

    /**
     * Stops the process the is currently running
     */
  public void stopCurrentlyRunning() throws InterruptedException {
      this.scheduler.currentUserProcess.stop();
  }

    /**
     * Get the current user process
     * @return the current user process
     */
  public PCB getCurrentUserProcess() {
      return this.scheduler.currentUserProcess;
  }

    /**
     * Open a device
     * @param args the device to open and the device's args
     * @return the device id
     */
    @Override
    public int open(String args) {
        PCB currentProcess = getCurrentUserProcess();
        int pcbIndex;
        int [] deviceIds = currentProcess.getDeviceIds();

        for (int i = 0; i < deviceIds.length; i++) {
            if (deviceIds[i] == -1) {
                pcbIndex = fileSystem.open(args);
                currentProcess.appendId(pcbIndex);
                return i;
            }
        }
        return -1;
    }

    /**
     * Close a device
     * @param id the device id
     */
    @Override
    public void close(int id) {
        fileSystem.close(getCurrentUserProcess().getDeviceIds()[id]);
        this.getCurrentUserProcess().getDeviceIds()[id] = -1;
    }

    /**
     * Read from a device
     * @param id the device id
     * @param size the size to read
     * @return the data read
     */
    @Override
    public byte[] read(int id, int size) {
        return fileSystem.read(getCurrentUserProcess().getDeviceIds()[id], size);
    }

    /**
     * Seek to a specified position in the device
     * @param id the device id
     * @param pos the position to seek
     */
    @Override
    public void seek(int id, int pos) {
        fileSystem.seek(getCurrentUserProcess().getDeviceIds()[id], pos);
    }

    /**
     * Write to a device
     * @param id the device id
     * @param data the data to write
     * @return the number of bytes written
     */
    @Override
    public int write(int id, byte[] data) {
        return fileSystem.write(getCurrentUserProcess().getDeviceIds()[id], data);
    }

    public void getMapping(int virtualPageNumber) {
        //PCB changes and update the TLB
        int physicalPage = getCurrentUserProcess().getPhysicalPage(virtualPageNumber);
        if(physicalPage == -1) {
            System.out.println("SEGMENTATION FAULT");
            this.scheduler.exit();
        } else {
            int index = rand.nextInt(2);
            Hardware.updateTLBEntry(index, virtualPageNumber, physicalPage);
        }
    }

    /**
     * Allocate memory to the process
     * @param size amount of memory to allocate
     * @return the virtual address
     */
    public int allocateMemory(int size) {
        int pageCount = 0;
        int physicalPage;
        if(size % 1024 != 0) {
            throw new RuntimeException("Size is not a multiple of 1024, reenter");
        }
        pageCount = size / 1024;
        physicalPage = findEmptySpace(pageCount);
        this.getCurrentUserProcess().updateMemoryMap(pageCount,physicalPage);
        return size;
    }

    /**
     * Free memory from the current process
     * @param pointer the virtual address of the memory to free
     * @param size the amount of the memory to free
     */
    public boolean freeMemory(int pointer, int size) {
        int pageCount = 0;
        int physicalPage = 0;
        int virtualPage = 0;
        int start = 0;
        int end = 0;

        if(size % 1024 != 0) {
            throw new RuntimeException("Error while reading size, enter a size that is a multiple of 1024");
        } else if(pointer % 1024 != 0) {
            throw new RuntimeException("Error while reading pointer, enter a pointer that is a multiple of 1024");
        }

        virtualPage = pointer / 1024 ;
        pageCount = pointer / 1024; //amount of pages to free
        physicalPage = getCurrentUserProcess().getPhysicalPage(virtualPage);

        if(pageCount < virtualPage) {
            end = physicalPage + virtualPage;
            start = end - virtualPage;
            Arrays.fill(this.memoryMap, start, end, false);
            return true;
        } else if(pageCount == virtualPage) {
            end = physicalPage + virtualPage;
            getCurrentUserProcess().clearVirtualPage(virtualPage);
            Arrays.fill(this.memoryMap, physicalPage, end, false);
            return true;
        }
        return false;
    }


    /**
     * Get the file system
     * @return the file system
     */
    public VFS getFileSystem() {
      return this.fileSystem;
    }

    /**
     * Find empty space to allocate memory
     * @param size amount of space to allocate
     * @return the starting index of the allocated space
     */
    private int findEmptySpace(int size) {
        int start = 0;
        int physicalPage = 0;
        int end = size - 1;
        boolean loopBool = true;

        while(start < end) {
            boolean isSpaceOccupied = false;
            if(memoryMap[start] || memoryMap[end]) {
                start++;
                end++;
                continue;
            }

            for(int i = start; i <= end; i ++) {
                if(memoryMap[i]) {
                    isSpaceOccupied = true;
                    break;
                }
            }

            if(!isSpaceOccupied) {
                physicalPage = start;
                Arrays.fill(memoryMap, start, end + 1, true);
                break;
            }

            start++;
            end++;
        }
        return physicalPage;
    }
}
