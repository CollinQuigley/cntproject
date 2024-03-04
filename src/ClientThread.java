import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

public class ClientThread extends Thread{

    private Peer peer;
    private int targetPeerID;
    private LinkedHashMap<Integer,String[]> peerData;
    public ClientThread(Peer peer, int targetPeerID, LinkedHashMap<Integer,String[]> peerData){
        this.peer = peer;
        this.targetPeerID = targetPeerID;
        this.peerData = peerData;
    }

    @Override
    public void run(){

          Socket requestSocket;
          try{
              requestSocket = new Socket("localhost", Integer.parseInt(peerData.get(this.targetPeerID)[1]));
              System.out.println("Connected to peer Process ID " + targetPeerID + " " + Integer.parseInt(peerData.get(this.targetPeerID)[1]));

          } catch (UnknownHostException e) {
              throw new RuntimeException(e);
          } catch (IOException e) {
              throw new RuntimeException(e);
          }

    }

}



