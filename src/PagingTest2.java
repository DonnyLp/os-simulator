public class PagingTest2 extends UserlandProcess {
    @Override
    public void main() {
        try {
            int virtualAddress = OS.allocateMemory(4 * 1024); //allocate 3 pages of memory
            byte testByte = 'Z';
            System.out.println("Writing the byte \"" + testByte + "\" to memory at virtual address: " + virtualAddress);
            Hardware.write(virtualAddress, testByte);
            byte writtenByte = Hardware.read(virtualAddress); //read the byte written to memory
            System.out.println("Byte read at virtual memory address " + virtualAddress + ": " + writtenByte);
            OS.sleep(30);
            OS.freeMemory(virtualAddress, 2 * 1024);
            OS.exit();
        } catch(InterruptedException err) {
            throw new RuntimeException(err);
        }
    }
}
