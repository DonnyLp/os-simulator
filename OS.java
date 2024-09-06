import java.util.ArrayList;

public class OS {
  private static Kernel kernel;

  public static CallType currentCall;

  public static ArrayList<Object> parameters;

  public enum CallType {
    createProcess,
    switchProcess
  };

  /**
   * Create a new kernel land process
   * @param process
   * @return index of the new process
   */
  public static int createProcess(UserlandProcess process) throws InterruptedException {
    //parameters.clear();
    // Add new parameters to the list here:
    currentCall = CallType.createProcess;
    kernel.start();
    if (kernel.getScheduler().isProcessRunning()) {
      kernel.stop();
    } else {
      while (kernel.getPID() == 0) {
        Thread.sleep(10);
      }
    }
    return 0;
  }

  public static void startup(UserlandProcess initialProcess) throws InterruptedException {
    kernel = new Kernel(initialProcess);
    createProcess(initialProcess);
  }

  public static void switchProcess() {
    //parameters.clear();
    currentCall = CallType.switchProcess;
    kernel.start();
  }
}
