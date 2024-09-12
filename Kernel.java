public class Kernel extends Process {
  private final Scheduler scheduler;
  public Kernel() {
    this.scheduler = new Scheduler();
  }
  @Override
  public void main() {
      while(true) {
          switch(OS.currentCall) {
              case createProcess -> {
                  UserlandProcess newProcess = (UserlandProcess)OS.parameters.getFirst();
                  OS.returnValue = createProcess(newProcess);
              }
              case switchProcess -> switchProcess();
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
     * Create process by calling the scheduler's version of createProcess
     * @param process the new process to be created
     * @return process ID
     */
  public int createProcess(UserlandProcess process) {
      return this.scheduler.createProcess(process);
  }

    /**
     * Switch the current process with the next process waiting in queue
     */
  public void switchProcess() {
      this.scheduler.switchProcess();
  }

  /**
   * Checks if there is any processes running
   * @return true if there is a process running, false otherwise
   * */
  public boolean isProcessRunning() {
      return this.scheduler.currentUserProcess != null;
  }

  public void stopCurrentlyRunning() throws InterruptedException {
      this.scheduler.currentUserProcess.stop();
  }
}
