import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {
    public Thread thread;
    public Semaphore counter = new Semaphore(0);
    public boolean quantumExpired;

  /**
   * Requesting the process to stop by setting the quantumExpire boolean value to true
   * @return void
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
   * @return void
   */
  public void start() {
      this.counter.release();
  }

  /**
   *  Stop the process by acquiring the semaphore i.e. decrement and calling the main process
   * @return void
   */
  public void stop() throws InterruptedException {
    this.counter.acquire();
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
