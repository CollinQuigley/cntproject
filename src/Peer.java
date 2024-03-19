import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Peer {
    public BitField bf;


    private int peerID;
    private int numPieces;
    private int fileSize;
    private int PieceSize;
    public ConcurrentHashMap<Integer, BitField> otherPeersBitFields;
    boolean hasFile;

    FileLogger fileLogger;
    public Peer(LinkedHashMap<String,String> commonData, LinkedHashMap<Integer,String[]> peerData, int peerID){
           this.peerID = peerID;

           this.hasFile = Integer.parseInt(peerData.get(this.peerID)[2]) == 1;
           this.fileSize = Integer.parseInt(commonData.get("FileSize"));
           this.PieceSize = Integer.parseInt(commonData.get("PieceSize"));
           this.numPieces = (int) Math.ceil((double) this.fileSize / (double) this.PieceSize);
           fileLogger = new FileLogger(peerID);
           this.bf = new BitField(numPieces, hasFile);
           this.otherPeersBitFields = new ConcurrentHashMap<>();
           System.out.println(numPieces);


    }

    public int getPeerID(){
        return peerID;
    }

   //Initialize remote peer process bitfield for reference
    public synchronized void createOtherPeerBitField(int remotePeerID, byte[] remoteBitField) {
        BitField bf = new BitField(numPieces, remoteBitField);
        otherPeersBitFields.put(remotePeerID, bf);
    }

    //update bitfield in specified remote peer process
    public synchronized void updateOtherPeerBitField(int peerID, int pieceIndex) {
        BitField bf = otherPeersBitFields.get(peerID);
        if (bf != null) {
            bf.setBit(pieceIndex);
            otherPeersBitFields.put(peerID, bf);
        }
    }

    public synchronized boolean hasOtherPeerBitField(int remotePeerID, int pieceIndex){
        BitField bf = otherPeersBitFields.get(peerID);
        if (bf != null) {
            return bf.hasBit(pieceIndex);
        }
        else{
            return false;
        }
    }

    //get a bitfield of a remote peer process
    public synchronized BitField getOtherPeerBitField(int peerID) {
        return otherPeersBitFields.get(peerID);
    }

    public void printOtherPeersBitFields() {
        for (Integer peerId : otherPeersBitFields.keySet()) {
            System.out.println("Bitfield for peer " + peerId + ":");
            BitField bitField = otherPeersBitFields.get(peerId);
            bitField.printBitfield();
        }
    }


}
