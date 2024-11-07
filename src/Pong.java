import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Pong extends UserlandProcess {

    @Override
    public void main() {
        String messageData = "pong is a userland process in this os";
        int counter = 0;
        try {
            while(true) {
                int receiverPID = OS.getPIDByName("Ping");
                KernelMessage newMessage = new KernelMessage(
                                0,
                                messageData.getBytes(StandardCharsets.UTF_8),
                                0,
                                receiverPID
                );
                counter++;
                OS.sendMessage(newMessage);
                KernelMessage incomingMessage = OS.waitForMessage();
                System.out.println("PONG:" + incomingMessage);
                Thread.sleep(250);
                cooperate();
            }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
