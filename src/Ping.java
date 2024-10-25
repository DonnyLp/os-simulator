public class Ping extends UserlandProcess {

    @Override
    public void main() {
        System.out.println("I am PING");
        String messageData = "PING";
        KernelMessage incomingMessage = null;
        try {
           int receiverPID = OS.getPIDByName("Pong");
           KernelMessage newMessage = new KernelMessage(0, messageData.getBytes(), 0, receiverPID);
           OS.sendMessage(newMessage);
           incomingMessage = OS.waitForMessage();
           System.out.println("Ping: " + incomingMessage);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
