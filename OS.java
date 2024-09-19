import java.util.ArrayList;
import java.util.Arrays;

public class OS {
  private static Kernel kernel;

  public static CallType currentCall;

  public static ArrayList<Object> parameters = new ArrayList<>();

  public static Object returnValue;

  public enum CallType {
    createProcess,
    switchProcess,
    sleep
  }

  /**
   * Create a new kernel land process
   * @param newUserlandProcess the userland process that is set to be created
   * @return index of the new process
   */
  public static int createProcess(UserlandProcess newUserlandProcess) throws InterruptedException {
    setupSystemCall(CallType.createProcess, newUserlandProcess);
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
    setupSystemCall(CallType.switchProcess);
  }

  /**
   * Puts a process to "sleep" (inactive) for a set duration
   * @param duration time in milliseconds that the process is going to sleep
   */
  public static void sleep(int duration) throws InterruptedException {
    setupSystemCall(CallType.sleep, duration);
  }

  /**
   * Setups boilerplate work for system calls
   * @param callType the kernel call initiated
   * @param callParameters variable param that accepts all system call parameters
   */
  public static void setupSystemCall(CallType callType, Object... callParameters) throws InterruptedException {
    parameters.clear();
    parameters.addAll(Arrays.asList(callParameters));
    currentCall = callType;
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
