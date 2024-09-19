public class PCB {

    private UserlandProcess userlandProcess;
    private String name;
    private int PID;
    public static int nextPID;

    public PCB(UserlandProcess userlandProcess, int PID) {
        this.PID = PID;
        this.userlandProcess = userlandProcess;
        this.name = userlandProcess.name;
    }

    /**
     * Start the userlandProcess
     */
    public void start() {
        this.userlandProcess.start();
    }

    /**
     * Stop the userlandProcess and make the process sleep until it has acquired the semaphore i.e. stopped
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException {
        this.userlandProcess.stop();
        while(!this.userlandProcess.isStopped()) {
            Thread.sleep(10);
        }
    }

    /**
     * Check if the userlandProcess is done
     * @return true if the userlandProcess is done, false otherwise
     */
    public boolean isDone() {
        return this.userlandProcess.isDone();
    }

    public
}
