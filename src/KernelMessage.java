public class KernelMessage {
    private int senderPID;
    private int receiverPID;
    private int messageInstruction;
    private byte [] messageData;

    public KernelMessage(int senderPID, byte[] messageData, int messageInstruction, int receiverPID) {
        this.senderPID = senderPID;
        this.messageData = messageData;
        this.messageInstruction = messageInstruction;
        this.receiverPID = receiverPID;
    }

    //copy constructor
    public KernelMessage(KernelMessage kernelMessage) {
        this.senderPID = kernelMessage.receiverPID;
        this.receiverPID = kernelMessage.receiverPID;
        this.messageInstruction = kernelMessage.messageInstruction;
        this.messageData = kernelMessage.messageData;
    }

    public int getReceiverPID() {
        return receiverPID;
    }

    public int getMessageInstruction() {
        return messageInstruction;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public int getSenderPID() {
        return senderPID;
    }

    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }

    @Override
    public String toString() {
        return "Message from: " + this.senderPID + " sending to: " + receiverPID;
    }
}
