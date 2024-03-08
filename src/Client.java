import java.io.IOException;
import java.net.Socket;
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
            Socket requestSocket;
            if (currentPeerID < peer.getPeerID()) {
                try {
                    requestSocket = new Socket("localhost", Integer.parseInt(peerData.get(currentPeerID)[1]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Thread thread = new ConnectionThread(requestSocket, peer, peerData, true);
                thread.start();
            }
        }
    }
}
