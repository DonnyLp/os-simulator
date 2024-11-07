import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {
    public Thread thread;
    public Semaphore counter;
    public String name;
    public boolean quantumExpired;

   public Process () {
       this.name = this.getClass().getName();
       this.thread = new Thread(this, name);
       this.counter = new Semaphore(0);
       this.quantumExpired = false;
       this.thread.start();
   }

   /**
   * Request the process to stop by changing the quantumExpire value to true
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
     * Ensures that the thread is permitted to run i.e. the semaphore count is > 0
     * @return true if the semaphore count is 1, false otherwise
     */
  public boolean isPermitted() { return this.counter.availablePermits() > 0;}
  /**
   *  Check whether this process is done i.e. the thread is not alive
   *  @return true if the thread is active, false otherwise
   */
  public boolean isDone() {
      return !this.thread.isAlive();
  }

  /**
   *  Start the process by releasing the semaphore i.e. increment
   */
  public void start() {
      this.counter.release();
  }

  /**
   *  Stop the process by acquiring the semaphore i.e. decrement
   */
  public void stop() throws InterruptedException {
    this.counter.acquire();
  }

  /**
   * Implement the run method from the Runnable interface
   */
  public void run() {
      try {
          this.counter.acquire();
          main();
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
  }

  /**
   * Handle the cooperation between processes when the quantum has expired
   */
  public void cooperate() throws InterruptedException {

    if(quantumExpired) {
        quantumExpired = false;
        OS.switchProcess();
    }
  }

  /**
   * Where main logic is held after acquiring the semaphore
   */
  public abstract void main();
}
