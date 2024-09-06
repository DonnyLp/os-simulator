import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
  private LinkedList<UserlandProcess> processes;
  private Timer timer;
  public UserlandProcess currentUserProcess;

  public Scheduler(UserlandProcess process) {
    this.processes = new LinkedList<>();
    this.currentUserProcess = process;
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
   * @param process the Userland process to be added to processes or started
   * @return PID of the new process
   */
  public int createProcess(UserlandProcess process) {
    this.processes.add(process);
    if(!isProcessRunning() || currentUserProcess.isDone()) {
      switchProcess();
    }
    return processes.size() - 1;
  }

  /**
   * Switch the current running process with the process at the head of the scheduler's list
   */
  public void switchProcess() {
    if(!isProcessRunning() || this.currentUserProcess.isDone() || !this.processes.isEmpty()) {
      this.currentUserProcess = this.processes.getFirst();
    }
    this.processes.add(this.currentUserProcess);
    this.processes.getFirst().start();
  }

  /**
   * Checks if there is any processes running
   * @return true if there is a process running, false otherwise
   */
  public boolean isProcessRunning() {
    Iterator<UserlandProcess> processIterator = this.processes.iterator();
    while(processIterator.hasNext()) {
      UserlandProcess currentProcess = processIterator.next();
      if (!currentProcess.isDone()) {
        return true;
      }
    }
    return false;
  }
}
