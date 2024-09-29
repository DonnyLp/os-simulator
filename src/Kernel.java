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
                  OS.Priority priority = (OS.Priority) OS.parameters.getLast();
                  OS.returnValue = this.scheduler.createProcess(newProcess, priority);
              }
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
     * @throws InterruptedException
     */
  public void stopCurrentlyRunning() throws InterruptedException {
      this.scheduler.currentUserProcess.stop();
  }
}
