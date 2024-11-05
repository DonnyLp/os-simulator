import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class FakeFileSystem implements Device{

    RandomAccessFile [] files;

    public FakeFileSystem() {
        files = new RandomAccessFile[10];
    }

    /**
     * Create a file
     * @param args name of the new file
     * @return index of the file or -1 if failed to "open"(create) a new file
     */
    @Override
    public int open(String args) {
        int index = 0;
        if(args.isEmpty()) {
            throw new DeviceException("Can't create new file, no filename provided");
        }
        for(int i = 0; i < files.length; i++) {
            if(files[i] == null) {
                try {
                    files[i] = new RandomAccessFile(args, "rw");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
               return i;
            }
        }
        return -1;
    }
    /**
     * Close the file at the specified index
     * @param id the device id
     */
    @Override
    public void close(int id) {
        for(int i = 0; i < files.length; i++) {
            if(id == i) {
                try {
                    files[i].close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    /**
     * Read from a file
     * @param id the device id
     * @param size the number of bytes to read
     * @return the bytes read
     */
    @Override
    public byte[] read(int id, int size) {
        int bytesRead;
        RandomAccessFile currentFile = getFile(id);
        byte[] buffer = new byte[size];
        if(currentFile == null) {
            throw new FakeFileSystemException("Couldn't grab the file with id: " + id);
        }

        try {
           bytesRead = currentFile.read(buffer,0,size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(bytesRead == -1) {
            return null;
        } else {
            try {
                currentFile.seek(bytesRead); //update position of the file before
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return buffer;
        }
    }

    /**
     * Set the current position of the file
     * @param id the device id
     * @param pos position to place file pointer
     */
    @Override
    public void seek(int id, int pos) {
        RandomAccessFile currentFile = getFile(id);
        try {
            System.out.println("Moving file pointer...");
            currentFile.seek(pos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write to a files
     * @param id the device id
     * @param data the data to write
     * @return the number of bytes written
     */
    @Override
    public int write(int id, byte[] data) {
        RandomAccessFile currentFile = getFile(id);
        long initBytes;
        long finalBytes;
        try {
            initBytes = currentFile.getFilePointer();
            System.out.println("Writing: " + new String(data));
            files[id].write(data);
            finalBytes = currentFile.getFilePointer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (int)(finalBytes - initBytes);
    }

    /**
     * Get the file at the specified index
     * @param id the device id
     * @return the file at the specified index
     */
    public RandomAccessFile getFile(int id) {
        for(int i = 0; i < files.length; i++) {
            if(id == i) {
                return files[i];
            }
        }
        return null;
    }
}
