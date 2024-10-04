public interface Device {
    int open(String args);
    void close(int id);
    byte[] read(int id, int size);
    void seek(int id, int pos);
    int write(int id, byte[] data);
}
