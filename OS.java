import java.util.ArrayList;

public class OS {
  private static Kernel kernel;

  public static CallType currentCall;

  public static ArrayList<Object> parameters = new ArrayList<>();

  public static Object returnValue;

  public enum CallType {
    createProcess,
    switchProcess
  }

  /**
   * Create a new kernel land process
   * @param newUserlandProcess the userland process that is set to be created
   * @return index of the new process
   */
  public static int createProcess(UserlandProcess newUserlandProcess) throws InterruptedException {
    parameters.clear();
    parameters.add(newUserlandProcess);
    currentCall = CallType.createProcess;
    kernel.start();
    if(kernel.isProcessRunning()) {
      kernel.stopCurrentlyRunning();
    } else {
      while(OS.returnValue == null) {
        Thread.sleep(10);
      }
    }
    return (int) returnValue;
  }

  /**
   * Creates the initial components and processes for the application
   * @param initialProcess init process that controls the creation of all other processes
   */
  public static void startup(UserlandProcess initialProcess) throws InterruptedException {
    kernel = new Kernel();
    createProcess(initialProcess);
    createProcess(new Idle());
  }

  /**
   * Switch the current process with the next process waiting in queue
   */
  public static void switchProcess() throws InterruptedException {
    parameters.clear();
    currentCall = CallType.switchProcess;
    kernel.start();
    if(kernel.isProcessRunning()) {
      kernel.stopCurrentlyRunning();
    } else {
      while(OS.returnValue == null) {
        Thread.sleep(10);
      }
    }
  }
}
