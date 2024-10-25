public class Pong extends UserlandProcess {

    @Override
    public void main() {
        System.out.println("I am PONG");
        String messageData = "PONG";
        KernelMessage newMessage = null;
        KernelMessage incomingMessage = null;
        int receiverPID;
        try {
            receiverPID = OS.getPIDByName("Ping");
            newMessage = new KernelMessage(0, messageData.getBytes(), 0, receiverPID);
            incomingMessage = OS.waitForMessage();
            System.out.println("Pong: " + incomingMessage);
            OS.sendMessage(newMessage);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
