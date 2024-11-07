import java.nio.ByteBuffer;

public class Ping extends UserlandProcess {

    @Override
    public void main() {
        String messageData = "ping is a userland process in this os";
        int counter = 0;
        try {
           while(true) {
               int receiverPID = OS.getPIDByName("Pong");
               ByteBuffer intBuffer = ByteBuffer.allocate(4);
               intBuffer.putInt(counter);
               KernelMessage newMessage = new KernelMessage(0, messageData.getBytes(), 0, receiverPID);
               counter++;
               OS.sendMessage(newMessage);
               KernelMessage incomingMessage = OS.waitForMessage();
               System.out.println("PING:" + incomingMessage);
               Thread.sleep(300);
               cooperate();
           }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
