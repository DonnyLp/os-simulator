import java.util.Random;
import java.util.Arrays;
public class VFS implements Device {
    private final Device[] devices;
    private final int[] ids;

    public VFS() {
        devices = new Device[20];
        ids = new int[20];
        Arrays.fill(ids,-1);
    }
    /**
     * Open specified device
     * @param args defines the device and the arguments to pass into the device
     * @return the device id
     */
    @Override
    public int open(String args) {
        String deviceName = args.split (" ")[0];
        String deviceArgs = args.split(" ")[1];
        Device newDevice;
        int vfsIndex = 0;
        int deviceId = 0;

        switch(deviceName) {
            case "random" -> {
                RandomDevice randomDevice = new RandomDevice();
                deviceId = randomDevice.open(deviceArgs);
                vfsIndex = addDevice(deviceId, randomDevice);
            }
            case "file" -> {
                FakeFileSystem file = new FakeFileSystem();
                deviceId = file.open(deviceArgs);
                vfsIndex = addDevice(deviceId, file);
            }
        }
        return vfsIndex;
    }

    /**
     * Close the device
     * @param id the device id
     */
    @Override
    public void close(int id) {
        if(ids[id] != -1) {
            devices[id].close(ids[id]);
            ids[id] = -1;
            devices[id] = null;
        } else {
            throw new VFSException("Trying to close a non-existent device");
        }
    }

    /**
     * Read from the device
     * @param id the device id
     * @param size the size to read
     * @return the data read
     */
    @Override
    public byte[] read(int id, int size) {
        return devices[id].read(ids[id], size);
    }
    /**
     * Seek to a position in the file
     * @param id the device id
     * @param pos the position to seek to
     */
    @Override
    public void seek(int id, int pos) {
        devices[id].seek(ids[id], pos);
    }

    /**
     * Write to the device
     * @param id the device id
     * @param data the data to write
     * @return the number of bytes written
     */
    @Override
    public int write(int id, byte[] data) {
        return devices[id].write(ids[id], data);
    }

    /**
     * Add a device and it's id to the device mapper
     * @param id the device id
     * @param device the device to add
     * @return the index of the device
     */
    public int addDevice(int id, Device device) {
        for(int i = 0; i < devices.length; i++) {
            if(ids[i] == -1 && devices[i] == null) {
                ids[i] = id;
                devices[i] = device;
                return i;
            }
        }
        return -1;
    }
}
