import java.util.Random;
public class RandomDevice implements Device {

    Random [] randomDevices;

    public RandomDevice() {
        randomDevices = new Random[10];
    }

    /**
     * Create a new Random device
     * @param args the seed number for the Random object
     * @return id of the random device
     */
    @Override
    public int open(String args) {
        int id = 0;
        for(int i = 0; i < randomDevices.length; i++) {
            if(randomDevices[i] == null) {
                if(!args.isEmpty()) {
                    randomDevices[i] = new Random(Integer.parseInt(args));
                } else {
                    randomDevices[i] = new Random();
                }
                id = i;
                break;
            }
        }
        return id;
    }
    /**
     * Close the random device
     * @param id the id of the random device
     */
    @Override
    public void close(int id) {
        for(int i = 0; i < randomDevices.length; i++) {
            if(id == i) {
                randomDevices[i] = null;
                break;
            }
        }
    }

    /**
     * Read from the random device
     * @param id the id of the random device
     * @param size the number of bytes to read
     * @return the bytes read
     */
    @Override
    public byte[] read(int id, int size) {
        byte[] result = new byte[size];
        Random device = null;
        for(int i = 0; i < randomDevices.length; i++) {
            if(id == i) {
                device = randomDevices[i];
                break;
            }
        }
        if(device == null) {
           throw new DeviceException("Device found at index: " + id + " is null");
        }
        device.nextBytes(result);
        return result;
    }

    /**
     * Seek to a specific position in the random device
     * @param id the id of the random device
     * @param pos the position to seek to
     */
    @Override
    public void seek(int id, int pos) {
        byte [] randomBytes = read(id, pos);
    }
    /**
     * Write to the random device
     * @param id the id of the random device
     * @param data the data to write
     * @return the number of bytes written
     */
    @Override
    public int write(int id, byte[] data) {
        return 0;
    }
}
