import java.util.Arrays;
import java.util.LinkedList;

public class PCB {

    private final UserlandProcess userlandProcess;
    private final String name;
    private final int PID;
    private OS.Priority priority;
    private long minWakeUp;
    public static int nextPID;
    private final int[] deviceIds;
    private LinkedList<KernelMessage> messageQueue;

    public PCB(UserlandProcess userlandProcess, int PID, OS.Priority priority) {
        this.userlandProcess = userlandProcess;
        this.PID = PID;
        this.priority = priority;
        this.name = userlandProcess.name;
        deviceIds = new int[10];
        Arrays.fill(deviceIds, -1);
        this.messageQueue = new LinkedList<>();
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
        return this.name;
    }

    /**
     * Sets the minimum wakeup time of the process.
     * @param minWakeUp the minimum wakeup time of the process
     */
    public void setMinWakeUp(long minWakeUp) {
        this.minWakeUp = minWakeUp;
    }

    /**
     * Returns the process' id
     * @return the process' id
     */
    public int getPID() {
        return this.PID;
    }

    /**
     * Get the device ids
     * @return the device ids
     */
    public int[] getDeviceIds() {
        return this.deviceIds;
    }

    /**
     * Close all open devices i.e. clear the deviceIds
     */
    public void closeDevices(VFS fileSystem) {
        System.out.println("Exiting, attempting to clear all active devices....");
        for(int i = 0; i < deviceIds.length; i++) {
            if(deviceIds[i] != -1) {
                fileSystem.close(deviceIds[i]);
                deviceIds[i] = -1;
            }
        }
        System.out.println("All devices cleared: " + isDeviceCleared());
    }

    /**
     * Check if all devices are cleared i.e. closed
     * @return true if all devices are close otherwise false
     */
    private boolean isDeviceCleared() {
        for (int deviceId : deviceIds) {
            if (deviceId != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add a device ID to the list of IDs
     * @param id the device id
     * @return the index at which the id was stored, -1 if there is no empty space
     */
    public int appendId(int id) {
        for(int i = 0; i < deviceIds.length; i++) {
            if(deviceIds[i] == -1) {
                deviceIds[i] = id;
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if this process has a message in its queue
     * @return the first message in the message queue, otherwise returns null if there is no messages in queue
     */
    public KernelMessage getMessage() {
        if(this.messageQueue.isEmpty()) {
            return null;
        }
        return this.messageQueue.pop();
    }

    /**
     * Add an incoming message to the message queue
     * @param message the message to be queued
     */
    public void queueMessage (KernelMessage message) {
        this.messageQueue.add(message);
    }
    /**
     * Returns the priority level of the process
     * @return the priority level of the process
     */
    public OS.Priority getPriority() { return this.priority; }

    /**
     * Sets the priority level of the process
     * @param priority the priority level of the process
     */
    public void setPriority(OS.Priority priority) { this.priority = priority;}

    /**
     * Get the userland processes' name
     * @return name of the userland process
     */
    public String getName() {
        return this.userlandProcess.name;
    }

    /**
     * Checks whether the current process is an Init process
     * @return true if it's an Init process, otherwise false
     */
    public boolean isInit() {
        return this.userlandProcess instanceof Init;
    }

    public boolean isMessageQueueEmpty() {
        return this.messageQueue.isEmpty();
    }
}
