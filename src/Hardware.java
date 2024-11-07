public class Hardware {
    private static int [][] TLB = new int[2][2];
    private static byte [] memory = new byte[1048576];

    private final int PAGE_SIZE = 1024;

    /**
     *
     */
    public byte read(int address) {
        //find the page number: address / page size
        int pageNumber = address / PAGE_SIZE; //address / page size
        int pageOffset = address & PAGE_SIZE; // page number & page size
        int physicalAddress; // physical page # * PAGE_SIZE + offset

        return 0;
    }

    public void write(int address, byte value) {
        //find the page number: address / page size
    }

    /**
     * Computes the virtual address
     * @param virtualAddress the virtual address
     * @return the virtual page
     */
    public int computeVirtualPage(int virtualAddress) {
        return virtualAddress / PAGE_SIZE;
    }

    /**
     * Computes the offset
     * @param virtualAddress the virtual address
     * @return the offset
     */
    public int computeOffset(int virtualAddress) {
        return virtualAddress % PAGE_SIZE;
    }

    /**
     * Compute the physical address
     * @param physicalPage the physical page number
     * @param offset the offset
     * @return the physical address
     */
    public int computePhysicalAddress(int physicalPage, int offset) {
        return physicalPage * PAGE_SIZE + offset;
    }
}