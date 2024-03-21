import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Peer {
    public BitField bf;


    private int peerID;
    private int numPieces;
    private int fileSize;
    private int pieceSize;
    public ConcurrentHashMap<Integer, BitField> otherPeersBitFields;
    private ConcurrentHashMap<Integer, Boolean> peersChokingState;
    private Set<Integer> peersInterested;
    private Map<Integer, byte[]> filePieces;
    boolean hasFile;

    FileLogger fileLogger;
    public Peer(LinkedHashMap<String,String> commonData, LinkedHashMap<Integer,String[]> peerData, int peerID) throws IOException {
           this.peerID = peerID;

           this.hasFile = Integer.parseInt(peerData.get(this.peerID)[2]) == 1;
           this.fileSize = Integer.parseInt(commonData.get("FileSize"));
           this.pieceSize = Integer.parseInt(commonData.get("PieceSize"));
           this.numPieces = (int) Math.ceil((double) this.fileSize / (double) this.pieceSize);
           fileLogger = new FileLogger(peerID);
           this.bf = new BitField(numPieces, hasFile);
           this.otherPeersBitFields = new ConcurrentHashMap<>();

           filePieces = new ConcurrentHashMap<>();
           Set<Integer> set = new HashSet<>();
           this.peersChokingState = new ConcurrentHashMap<>();
           peersInterested = Collections.synchronizedSet(set);

           if(hasFile) {
               readFile(commonData.get("FileName"));
           }
           else{
               initializePieces();
           }


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

    public void setChoking(int peerID, boolean isChoking){
        peersChokingState.put(peerID, isChoking);
    }

    public boolean isChoked(int peerID){
        return peersChokingState.getOrDefault(peerID, false);
    }

    public void setInterested(int peerID){

        peersInterested.add(peerID);

    }

    public void removeInterested(int peerID){

        peersInterested.remove(peerID);

    }
    public boolean isInterested(int peerID){

        return peersInterested.contains(peerID);

    }

    //print to see who is interested for debugging if needed
    public void printInterested() {
        System.out.println("Peers Interested:");
        for (Integer peerID : peersInterested) {
            System.out.println(peerID);
        }
    }

    //initialize file pieces if peerProcess does not have file.
    private void initializePieces(){

        for(int i = 0; i < numPieces; i++){
            filePieces.put(i, new byte[pieceSize]);
        }

    }

    //read the file and store pieces if peerProcess has the file
    private void readFile(String fileName) throws IOException {
        String filePath = "./project_config_file_large/" + peerID + "/" + fileName;
        File file = new File(filePath);
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            int bytesRead;
            byte[] buffer = new byte[pieceSize];

            int pieceIndex = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (bytesRead < pieceSize) {
                    byte[] trimmedBuffer = new byte[bytesRead];
                    System.arraycopy(buffer, 0, trimmedBuffer, 0, bytesRead);
                    filePieces.put(pieceIndex, trimmedBuffer);
                } else {
                    filePieces.put(pieceIndex, buffer.clone());
                }
                pieceIndex++;
            }
        } finally {
            inputStream.close();
        }
        /*if (file.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
        }*/
        //System.out.println(filePath);
    }


    //write file to test if file data is stored properly in filePieces
    private void reassembleFile() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("reassembled_file.jpg");
        for (int i = 0; i < filePieces.size(); i++) {
            outputStream.write(filePieces.get(i));
        }
        outputStream.close();
    }
}
