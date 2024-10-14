import java.util.Arrays;
public class DeviceTest2 extends UserlandProcess {
    @Override
    public void main() {
        try {
            //test random creation and functionality
            int randomDeviceId = OS.open("random 100");
            int bytesWritten = OS.write(randomDeviceId, new byte[10]); //expect a return value of zero
            byte [] readOutput = OS.read(randomDeviceId,20);
            System.out.println("Bytes written to randomDevice: " + bytesWritten + " bytes");
            System.out.println("Bytes read from randomDevice: " + new String(readOutput));
            OS.close(randomDeviceId);
            OS.sleep(30);

            //testing multiple file device creation and functionalities
            for(int i = 0; i < 3; i ++) {
                int fileNumber = i + 1;
                int fd = OS.open("file " + "readme" + fileNumber  + ".txt");
                System.out.println("start of file creation: " + fileNumber);
                String test = "Hey this is a readme file #" + fileNumber;
                byte [] writeTest = new byte[test.length()];
                writeTest = test.getBytes();

                int bWritten = OS.write(fd, test.trim().getBytes());
                OS.seek(fd, 0);
                byte [] rOutput = OS.read(fd,test.length());
                System.out.println("Bytes written to file #" + fileNumber + " : " + bWritten + " bytes");
                System.out.println("Bytes read from file #" +  fileNumber + " : " + new String(rOutput));
                OS.sleep(40);
            }
            OS.exit();
        } catch(InterruptedException err) {
            throw new RuntimeException(err);

        }
    }
}
