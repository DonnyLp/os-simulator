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
    sleep,
    open,
    write,
    read,
    seek,
    close,
    getPID, getPIDByName, sendMessage, waitForMessage, exit
  }

  public enum Priority {
    realTime,
    interactive,
    background
  }

  /**
   * Create a new kernel land process
   * @param newUserlandProcess the userland process that is set to be created
   * @return index of the new process
   */
  public static int createProcess(UserlandProcess newUserlandProcess, Priority priority) throws InterruptedException {
    setupSystemCall(CallType.createProcess, newUserlandProcess, priority);
    return (int) returnValue;
  }

  /**
   * Creates the initial components and processes for the application
   * @param initialProcess init process that controls the creation of all other processes
   */
  public static void startup(UserlandProcess initialProcess) throws InterruptedException {
    kernel = new Kernel();
    createProcess(initialProcess, Priority.interactive);
    createProcess(new Idle(), Priority.background);
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
   * Open a new device
   * @param args device type and arguments to be passed to specified device
   */
  public static int open(String args) throws InterruptedException {
    setupSystemCall(CallType.open, args);
    return (int) returnValue;
  }

  /**
   * Close the specified device
   * @param id the device id
   */
  public static void close(int id) throws InterruptedException {
    setupSystemCall(CallType.close, id);
  }
  /**
    * Read from the device
   * @param id the device id
   * @param size the size of the data to read
   */
  public static byte[] read(int id, int size) throws InterruptedException {
    setupSystemCall(CallType.read, id, size);
    return (byte[]) returnValue;
  }

  /**
   * Seek to a position in the device
   * @param id the device id
   * @param to the position to seek to
   */
  public static void seek(int id, int to) throws InterruptedException{
    setupSystemCall(CallType.seek, id, to);
  }

  /**
   * Write to the device
   * @param id the device id
   * @param data data to write to the device
   */
  public static int write(int id, byte[] data) throws InterruptedException {
    setupSystemCall(CallType.write, id, data);
    return (int) returnValue;
  }

  /**
   * Get the current process' id
   * @return the current process id
   */
  public static int getPID() throws InterruptedException {
    setupSystemCall(CallType.getPID);
    return (int) returnValue;
  }

  /**
   * Get the process id by its name
   * @return the process id
   */
  public static int getPIDByName(String processName) throws InterruptedException {
    setupSystemCall(CallType.getPIDByName, processName);
    return (int) returnValue;
  }

  /**
   * Send a message to a process
   */
  public static void sendMessage (KernelMessage message) throws InterruptedException {
    setupSystemCall(CallType.sendMessage, message);
  }

  /**
   * Wait for an incoming message from a process
   * @return the current process' kernel message
   */
  public static KernelMessage waitForMessage() throws InterruptedException{
    setupSystemCall(CallType.waitForMessage);
    return (KernelMessage) returnValue;
  }

  /**
   * Unschedule the current process, so it never gets ran again
   */
  public static void exit() throws InterruptedException {
    setupSystemCall(CallType.exit);
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
