public class Kernel extends Process implements Device{
  private final Scheduler scheduler;
  private final VFS fileSystem;
  private boolean[] memoryInUse = new boolean[100];
  public Kernel() {
    this.scheduler = new Scheduler(this);
    this.fileSystem = new VFS();
    this.memoryInUse = new boolean[100];
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
              case allocateMemory -> allocateMemory((int) OS.parameters.getFirst());
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

    public void getMapping(int virtualPage) {
        //PCB changes and update the TLB
    }

    public void allocateMemory(int size) {
        //PCB changes and update the TLB
    }

    public boolean freeMemory(int pointer, int size) {
        return false;
    }

    /**
     * Get the file system
     * @return the file system
     */
    public VFS getFileSystem() {
      return this.fileSystem;
    }
}
