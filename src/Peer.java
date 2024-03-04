import java.util.BitSet;
import java.util.LinkedHashMap;

public class Peer {
    private BitSet bitField;
    private int peerID;
    private int numPieces;
    private int fileSize;
    private int PieceSize;
    boolean hasFile;
    public Peer(LinkedHashMap<String,String> commonData, LinkedHashMap<Integer,String[]> peerData, int peerID){
           this.peerID = peerID;
           this.hasFile = Integer.parseInt(peerData.get(this.peerID)[2]) == 1;
           this.fileSize = Integer.parseInt(commonData.get("FileSize"));
           this.PieceSize = Integer.parseInt(commonData.get("PieceSize"));
           this.numPieces = (int) Math.ceil((double) this.fileSize / (double) this.PieceSize);
           initializeBitfield();
    }

    public int getPeerID(){
        return peerID;
    }
    private synchronized void initializeBitfield() {
        bitField = new BitSet(numPieces);
        if (hasFile) {
            bitField.set(0, numPieces);
        }
    }

    // Set a specific bit in the bitfield to 0
    public synchronized void clearBit(int Piece) {
        if (Piece < 1 || Piece > numPieces){
            return;
        }
        bitField.clear(Piece - 1);
    }

    // Set a specific bit in the bitfield to 1
    public synchronized void setBit(int Piece) {
        if (Piece < 1 || Piece > numPieces){
            return;
        }
        bitField.set(Piece - 1);
    }

    // Get the value of a specific bit in the bitfield
    public synchronized boolean getBit(int Piece) {
        return bitField.get(Piece - 1);
    }

    // Get the entire bitfield
    public synchronized BitSet getBitfield() {

        return (BitSet) bitField.clone();
    }

    // Set the entire bitfield
    public synchronized void setBitfield(BitSet newBitfield) {
        bitField =  (BitSet) newBitfield.clone();
    }

    public synchronized void printBitfield(){
        System.out.print("peerProcess " + peerID + " Bitfield: ");
        System.out.println();
        for (int i = 0; i < numPieces; i++) {
            System.out.print("Piece: " + (i + 1) + " " + (bitField.get(i) ? "1" : "0"));
            System.out.println();
        }
    }
}
