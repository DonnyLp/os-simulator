public class Kernel extends Process {
  private Scheduler scheduler;
  private int PID;

  public Kernel(UserlandProcess initialProcess) {
      super();
    this.scheduler = new Scheduler(initialProcess);
    this.PID = 0;
  }

  @Override
  public void main() {
      start();
  }

  @Override
  public void run() {
      try {
          this.counter.acquire();
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
      while(true) {
      switch(OS.currentCall) {
        case createProcess -> {
          PID = scheduler.createProcess(scheduler.currentUserProcess);
        }
        case switchProcess -> scheduler.switchProcess();
      }
      this.scheduler.currentUserProcess.start();
      try {
        this.stop();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public Scheduler getScheduler() {
    return this.scheduler;
  }

  public int getPID() {
    return this.PID;
  }
}
