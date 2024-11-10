import java.util.Arrays;

public class Hardware {
    private static int [][] TLB = new int[2][2];
    private static byte [] memory = new byte[1048576];

    private final static int PAGE_SIZE = 1024;

    /**
     * Read bytes from memory
     * @param virtualAddress the address to read from
     * @return the bytes read
     */
    public static byte read(int virtualAddress) throws InterruptedException {
        int virtualPage = computeVirtualPageNumber(virtualAddress);
        int pageOffset = computeOffset(virtualAddress);
        int physicalPage = getTLBEntry(virtualPage);
        int physicalAddress = 0;

        if(physicalPage == -1) {
            OS.getMapping(virtualPage);
        } else {
            physicalAddress = computePhysicalAddress(physicalPage, pageOffset);
        }

        return memory[physicalAddress];
    }

    /**
     * Write bytes to memory
     * @param virtualAddress the address to write to
     * @param value the value to write
     */
    public static void write(int virtualAddress, byte value) throws InterruptedException {
        int virtualPage = computeVirtualPageNumber(virtualAddress);
        int pageOffset = computeOffset(virtualAddress);
        int physicalPage = getTLBEntry(virtualPage);
        int physicalAddress = 0;

        if(physicalPage == -1) {
            OS.getMapping(virtualPage);
        } else {
            physicalAddress = computePhysicalAddress(physicalPage, pageOffset);
        }
        if(physicalPage != -1) {
            memory[physicalAddress] = value;
        }
    }

    /**
     * Computes the virtual address
     * @param virtualAddress the virtual address
     * @return the virtual page
     */
    public static int computeVirtualPageNumber(int virtualAddress) {
        return virtualAddress / PAGE_SIZE;
    }

    /**
     * Computes the offset
     * @param virtualAddress the virtual address
     * @return the offset
     */
    public static int computeOffset(int virtualAddress) {
        return virtualAddress % PAGE_SIZE;
    }

    /**
     * Compute the physical address
     * @param physicalPage the physical page number
     * @param offset the offset
     * @return the physical address
     */
    public static int computePhysicalAddress(int physicalPage, int offset) {
        return physicalPage * PAGE_SIZE + offset;
    }

    public static int getTLBEntry(int virtualPage) {
        for(int i = 0; i < TLB.length; i++) {
            if (TLB[i][0] == virtualPage) {
                return TLB[i][0];
            }
        }
        return -1;
    }

    public static void clearTLB() {
        TLB[0][0] = 0;
        TLB[0][1] = 0;
        TLB[1][0] = 0;
        TLB[1][1] = 0;
    }
}