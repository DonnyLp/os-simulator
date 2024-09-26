public class PCB {

    private final UserlandProcess userlandProcess;
    private final String name;
    private final int PID;

    private OS.Priority priority;

    private long minWakeUp;
    public static int nextPID;

    public PCB(UserlandProcess userlandProcess, int PID, OS.Priority priority) {
        this.userlandProcess = userlandProcess;
        this.PID = PID;
        this.priority = priority;
        this.name = userlandProcess.name;
    }

    /**
     * Determines whether the process should be "awake"
     * @param currentTime the current time in milliseconds
     */
    public boolean wakeUp(long currentTime) {
        return currentTime >= minWakeUp;
    }

    /**
     * Start the userlandProcess
     */
    public void start() {
        this.userlandProcess.start();
    }

    /**
     * Stop the userlandProcess and make the process sleep until it has acquired the semaphore i.e. stopped
     */
    public void stop() throws InterruptedException {
        this.userlandProcess.stop();
        while(!this.userlandProcess.isStopped()) {
            Thread.sleep(10);
        }
    }

    /**
     * Request the process to stop by changing the quantumExpire value to true
     */
    public void requestStop() {
        this.userlandProcess.requestStop();
    }

    /**
     * Check if the userlandProcess is done
     * @return true if the userlandProcess is done, false otherwise
     */
    public boolean isDone() {
        return this.userlandProcess.isDone();
    }
    @Override
    public String toString() {
        return this.name + this.PID;
    }
    public void setMinWakeUp(long minWakeUp) {
        this.minWakeUp = minWakeUp;
    }
    public int getPID() {
        return this.PID;
    }

    public String getName() {
        return this.name + " process";
    }

    public OS.Priority getPriority() { return this.priority; }
    public void setPriority(OS.Priority priority) { this.priority = priority;}

    public boolean isInit() {
        return this.userlandProcess instanceof Init;
    }

  public boolean isPermitted() {
    return this.userlandProcess.isPermitted();
    }
}
