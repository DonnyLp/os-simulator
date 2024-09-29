import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Clock;
import java.util.Random;

public class Scheduler {

  public PCB currentUserProcess;
  private final LinkedList<PCB> realTimeProcesses;
  private final LinkedList<PCB> interactiveProcesses;
  private final LinkedList<PCB> backgroundProcesses;
  private final LinkedList<PCB> waitingProcesses;
  private final Timer timer;
  private final Random rand;
  private final Clock clock;
  private int demotionCounter;

  public Scheduler() {
    this.realTimeProcesses = new LinkedList<>();
    this.interactiveProcesses = new LinkedList<>();
    this.backgroundProcesses = new LinkedList<>();
    this.waitingProcesses = new LinkedList<>();
    this.clock = Clock.systemDefaultZone();
    this.demotionCounter = -1;
    this.rand = new Random();
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
  public int createProcess(UserlandProcess process, OS.Priority priority) {
    addProcess(new PCB(process, PCB.nextPID++, priority));
    if(this.currentUserProcess == null) {
      switchProcess();
    } else {
      if (this.currentUserProcess.isInit() && this.currentUserProcess.isDone()) {
        this.currentUserProcess = getNextProcess();
      }
    }
    return this.currentUserProcess.getPID();
  }

  /**
   * Switch the current process with the next process waiting in queue
   */
  public void switchProcess() {
    System.out.println("Switching...");
    int oldPID = 0; //holds the PID of the process that's being switched out
    //check if any processes need to be woken up and give them a chance to run
    for(PCB process: this.waitingProcesses) {
      if(process.wakeUp(clock.millis())) {
        addProcess(process);
        this.waitingProcesses.remove(process);
      }
    }
    //capture the current PID and add it to the list
    if (this.currentUserProcess != null && !this.currentUserProcess.isDone()) {
      oldPID = this.currentUserProcess.getPID();
      addProcess(this.currentUserProcess);
    }
    this.currentUserProcess = getNextProcess();
    //compare the previous process with the new process for demotion case
    if(this.currentUserProcess != null && oldPID == this.currentUserProcess.getPID()) {
      this.demotionCounter++;
    } else {
      this.demotionCounter = 0;
    }
    //handle demotion case
    if(demotionCounter > 5) {
      System.out.println("Demoting " + this.currentUserProcess + " with priority: " + this.currentUserProcess.getPriority());
      demoteProcess(this.currentUserProcess);
      System.out.println("Demoted " + this.currentUserProcess + " to: " + this.currentUserProcess.getPriority() + " priority");
    }
  }

  /**
   * Puts a process to "sleep" (inactive) for a set duration
   * @param duration time in milliseconds that the process is going to sleep
   */
  public void sleep(int duration) {
    long minWakeUp = (duration + clock.millis());
    this.currentUserProcess.setMinWakeUp(minWakeUp);
    this.waitingProcesses.add(this.currentUserProcess);
    this.currentUserProcess = null;
    switchProcess();
  }

  /**
   * Unschedule the current process, so it never gets ran again
   */
  public void exit() {
    this.currentUserProcess = getNextProcess();
  }

  /**
   * Demotes the passed in userland process
   */
  public void demoteProcess(PCB process) {
    switch(process.getPriority()) {
      case realTime -> process.setPriority(OS.Priority.interactive);
      case interactive -> process.setPriority(OS.Priority.background);
      case background -> System.out.println("Cannot demote");
    }
  }
  /**
   * Helper method that will add the process to correct queue depending on its priority
   * @param process the process to be added to queue
   */
  private void addProcess(PCB process) {
    switch (process.getPriority()) {
      case realTime -> this.realTimeProcesses.add(process);
      case interactive -> this.interactiveProcesses.add(process);
      case background  -> this.backgroundProcesses.add(process);
    }
  }
  /**
   * Using a probabilistic model to get the next process to run
   * @return PCB next process to be run
   */
  private PCB getNextProcess() {
    PCB process = null;
    while(process == null) {
      int randomNumber = rand.nextInt(101);
      System.out.println("\n");
      System.out.println("realtime processes: "+ this.realTimeProcesses);
      System.out.println("interactive processes: " + this.interactiveProcesses);
      System.out.println("background processes: "+ this.backgroundProcesses);

      switch(setProbabilisticMode()) {
        case 1 -> {
          if(randomNumber <= 60) {
            process = getRealtimeProcess();
            printChoseProcess(process);
          }
          else if(randomNumber <= 90 && !isInteractiveEmpty()) {
            process = interactiveProcesses.pop();
            printChoseProcess(process);
          } else if(!isBackgroundEmpty()){
            process = getBackgroundProcess();
            printChoseProcess(process);
          } else {
            System.out.println("No processes chosen. Rerun!");
          }
        }
        case 2 -> {
          if(randomNumber <= 75) {
            process = getInteractiveProcess();
            printChoseProcess(process);
          } else if(!isBackgroundEmpty()) {
            process = getBackgroundProcess();
            printChoseProcess(process);
          } else {
            System.out.println("No processes chosen. Rerun!");
          }
        }
        case 3 -> {
          process = getBackgroundProcess();
          printChoseProcess(process);
        }
      }
    }
    return process;
  }

  /**
   * Gets the head of the realtime process list
   * @return PCB head of the realtime process list
   */
  public PCB getRealtimeProcess() {
    return this.realTimeProcesses.removeFirst();
  }

  /**
   * Determines which the probabilistic mode to get from
   */
  public int setProbabilisticMode() {
    int modeNumber = 0;
    if(!isRealTimeEmpty()) {
      modeNumber = 1;
    }
    else if(!isInteractiveEmpty()) {
      modeNumber = 2;
    } else if(!isBackgroundEmpty()){
      modeNumber =  3;
    }
    return modeNumber;
  }
  /**
   * Gets the head of the interactive process list
   * @return PCB head of the interactive process list
   */
  public PCB getInteractiveProcess() {
    return this.interactiveProcesses.removeFirst();
  }

  /**
   * Gets the head of the background process list
   * @return PCB head of the background process list
   */
  public PCB getBackgroundProcess() {
    return this.backgroundProcesses.removeFirst();
  }
  /**
   * Checks if the realtime processes list is empty
   * @return true if the realtime process list is empty, false otherwise
   */
  private boolean isRealTimeEmpty() {
    return this.realTimeProcesses.isEmpty();
  }

  /**
   * Checks if the interactive processes list is empty
   * @return true if the interactive process list is empty, false otherwise
   */
  private boolean isInteractiveEmpty() {
    return this.interactiveProcesses.isEmpty();
  }

  /**
   * Checks if the background processes list is empty
   * @return true if the background process list is empty, false otherwise
   */
  private boolean isBackgroundEmpty() {
    return this.backgroundProcesses.isEmpty();
  }

  /**
   * Helper that prints the process that has been chosen
   * @param process the current userland process
   */
  public void printChoseProcess(PCB process) {
    System.out.println(process + " starting...");
  }
}
