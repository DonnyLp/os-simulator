import java.util.ArrayList;

public class OS {
  private static Kernel kernel;

  public static CallType currentCall;

  public static ArrayList<Object> parameters;

  public enum CallType {
    CreateProcess,
    SwitchProcess
  };

  public static int createProcess(UserlandProcess up) {
    return 0;
  }

  public static void startup(UserlandProcess init) {
    
  }

  public static void switchProcess() {
    kernel.scheduler.switchProcess();
  }
}
