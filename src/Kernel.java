public class Kernel extends Process implements Device{
  private final Scheduler scheduler;
  private VFS fileSystem;

  public Kernel() {
    this.scheduler = new Scheduler(this);
    this.fileSystem = new VFS();
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
              case close -> close((int) OS.parameters.getFirst());
              case switchProcess -> this.scheduler.switchProcess();
              case sleep -> this.scheduler.sleep((int)OS.parameters.getFirst());
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

    /**
     * Get the file system
     * @return the file system
     */
    public VFS getFileSystem() {
      return this.fileSystem;
    }
}
