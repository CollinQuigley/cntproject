import java.util.LinkedHashMap;
import java.util.Map;

public class Client extends Thread {

    private Peer peer;
    private LinkedHashMap<Integer,String[]> peerData;

    public Client(Peer peer, LinkedHashMap<Integer,String[]> peerData){
        this.peer = peer;
        this.peerData = peerData;
    }

    @Override
    public void run(){
        for (Map.Entry<Integer, String[]> entry : peerData.entrySet()) {
            int currentPeerID = entry.getKey();
            if (currentPeerID < peer.getPeerID()) {
                Thread thread = new ClientThread(peer, currentPeerID, peerData);
                thread.start();
            }
        }
    }
}
