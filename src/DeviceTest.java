import java.util.Arrays;

public class DeviceTest extends UserlandProcess {
    @Override
    public void main() {
        try {
            int fd = OS.open("file test.txt");
            String test = "This is a test file created by userland";
            int bytesWritten = OS.write(fd, test.getBytes());
            OS.seek(fd, 0);
            OS.sleep(30);
            byte [] readOutput = OS.read(fd,39);
            System.out.println("Bytes written: " + bytesWritten);
            System.out.println("Bytes read: " + new String(readOutput));
            OS.exit();
        } catch(InterruptedException err) {
            throw new RuntimeException(err);
        }
    }
}
