import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Clock;

public class Scheduler {
  private final LinkedList<UserlandProcess> processes;
  private final Timer timer;
  private int PID;
  public UserlandProcess currentUserProcess;

  public Scheduler() {
    this.processes = new LinkedList<>();
    this.PID = -1;
    timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          if(currentUserProcess != null) {
            currentUserProcess.requestStop();
          }
        }
      }, 0, 250);
  }
  /**
   * Add the new process to the scheduler's list and starts the process if there's no other processes running
   * @param process the new Userland process to be created
   * @return PID of the new process
   */
  public int createProcess(UserlandProcess process) {
    System.out.println(this.processes);
    if(this.currentUserProcess == null) {
      switchProcess();
    } else {
      if (this.currentUserProcess instanceof Init && this.currentUserProcess.isDone()) {
        this.currentUserProcess = this.processes.removeFirst();
      }
    }
    return PID++;
  }

  /**
   * Switch the current process with the next process waiting in queue
   */
  public void switchProcess() {
    if (this.currentUserProcess != null && !this.currentUserProcess.isDone()) {
        this.processes.add(this.currentUserProcess);
    }
    if(!this.processes.isEmpty()) {
        this.currentUserProcess = this.processes.removeFirst();
    }
  }

  /**
   * Puts a process to "sleep" (inactive) for a set duration
   * @param duration time in milliseconds that the process is going to sleep
   */
  public void sleep(int duration) {
    System.out.println("sleep");
  }

  /**
   * Helper method to display the scheduler's processes in a readable format
   */
  public void displayProcesses() {
    StringBuilder listFormatted = new StringBuilder();
    for(Process current : this.processes) {
      listFormatted.append(current.thread.getName()).append(",");
    }
  }
}
