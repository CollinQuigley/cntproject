public class BitField {

    private byte[] bitfield;

    private int numPieces;


    // Initialize bitfield for process if it has file or not
    public BitField(int size, boolean hasFile) {
        // Round up the size to the nearest byte
        int byteSize = (size + 7) / 8;
        this.numPieces = size;
        this.bitfield = new byte[byteSize];
        if (hasFile) {
            // Set all bits to 1
            for (int i = 0; i < byteSize; i++) {
                bitfield[i] = (byte) 0xFF; // All bits set to 1
            }
        }
    }

    //save bitfield received from another process
    public BitField(int size, byte[] bf) {
        // Round up the size to the nearest byte
        this.numPieces = size;
        this.bitfield = bf;
    }

    // Set a specific bit in the bitfield to 1
    public void setBit(int index) {
        if (index >= numPieces) {
            throw new IllegalArgumentException("Index out of range");
        }

        int byteIndex = index / 8;
        int bitOffset = index % 8;
        bitfield[byteIndex] |= (byte) (1 << (7 - bitOffset));
    }


    // Set a specific bit in the bitfield to 0
    public void clearBit(int index) {
        if (index >= numPieces) {
            throw new IllegalArgumentException("Index out of range");
        }

        int byteIndex = index / 8;
        int bitOffset = index % 8;
        bitfield[byteIndex] &= (byte) ~(1 << (7 - bitOffset));
    }

    // See if a bitfield as a certain bit
    public boolean hasBit(int index) {
        if (index >= numPieces) {
            throw new IllegalArgumentException("Index out of range");
        }

        int byteIndex = index / 8;
        int bitOffset = index % 8;
        return ((bitfield[byteIndex] >> (7 - bitOffset)) & 1) == 1;
    }


    //get bitfield
    public byte[] getBitfield() {
        return bitfield;
    }

    //print bitfield if needed for debugging
    public void printBitfield() {
        for (int i = 0; i < numPieces; i++) {
            int byteIndex = i / 8;
            int bitOffset = i % 8;
            int bit = (bitfield[byteIndex] >> (7 - bitOffset)) & 1;
            System.out.println("Bit: " + i + " " + bit);
        }
        System.out.println();
    }



}
