public class PagingTest3 extends UserlandProcess {
    @Override
    public void main() {
        try {
            int virtualAddress =

            virtualAddress = OS.allocateMemory(3 * 1024);
            byte writtenByte = Hardware.read(virtualAddress); //read the byte written to memory
            System.out.println("Byte read at virtual memory address " + virtualAddress + ": " + writtenByte);
            OS.sleep(30);
            byte testByte = 'D';
            System.out.println("Writing the byte \"" + testByte + "\" to memory at virtual address: " + virtualAddress);
            Hardware.write(virtualAddress, testByte);


            OS.exit();
        } catch(InterruptedException err) {
            throw new RuntimeException(err);
        }
    }
}