import java.beans.PropertyEditor;
import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {
    public Thread thread;
    public Semaphore counter;
    public boolean quantumExpired;

   public Process () {
       this.thread = new Thread(this);
       this.counter = new Semaphore(0);
       this.quantumExpired = false;
   }
  /**
   * Requesting the process to stop by setting the quantumExpire boolean value to true
   */
  public void requestStop() {
        this.quantumExpired = true;
  }

  /**
   * Check if the process has stopped or in other words the semaphore is 0
   * @return boolean - indicating whether the process has stopped
   */
  public boolean isStopped() {
    return this.counter.availablePermits() == 0;
  }

  /**
   *  Check whether this process is done i.e. the thread is not alive
   *  @return true if the thread is active, false otherwise
   */
  public boolean isDone() {
      return !thread.isAlive();
  }

  /**
   *  Start the process by releasing the semaphore i.e. increment
   */
  public void start() {
      thread = new Thread(this);
      thread.start();
      this.counter.release();
  }

  /**
   *  Stop the process by acquiring the semaphore i.e. decrement and calling the main process
   */
  public void stop() throws InterruptedException {
    this.counter.acquire();
  }

  /**
   *
   */
  public void run() {
      try {
          this.counter.acquire();
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
      main();
  }

  /**
   * Handles the cooperation between processes when the quantum has expired
   */
  public void cooperate() throws InterruptedException {
    if(quantumExpired) {
      quantumExpired = false;
       OS.switchProcess();
    }
  }

  /**
   * Represents the main program
   */
  public abstract void main();
}
