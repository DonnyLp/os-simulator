public abstract  class UserlandProcess extends Process {
    final int PAGE_SIZE = 1024;

    public UserlandProcess () {
        super();
    }

    /**
     *
     */
    public byte read(int address) {
        //find the page number: address / page size
        int pageNumber = address / PAGE_SIZE; //address / page size
        int pageOffset = address & PAGE_SIZE; // page number & page size
        int physicalAddress; // physical page # * PAGE_SIZE + offset

    }

    public void write(int address, byte value) {
        //find the page number: address / page size
    }
}
