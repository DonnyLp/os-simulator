public class Kernel extends Process {
  public Scheduler scheduler;

  @Override
  public void main() {
    
  }
  @Override
  public void run() {
    while(true) {
      switch(OS.currentCall) {
        case CreateProcess -> scheduler.createProcess(scheduler.currentUserProcess);
      }
      this.scheduler.currentUserProcess.start();;
      try {
        this.stop();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
