import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;

public class Server extends Thread{

    private Peer peer;
    private int peerID;
    private LinkedHashMap<Integer,String[]> peerData;
    public Server(Peer peer, int peerID, LinkedHashMap<Integer,String[]> peerData){
        this.peer = peer;
        this.peerID = peerID;
        this.peerData = peerData;
    }

    @Override
    public void run(){
        System.out.println("The server is running.");
        try {
            ServerSocket listener = new ServerSocket(Integer.parseInt(peerData.get(this.peerID)[1]));
            try{
                while(true){
                    new ServerThread(listener.accept(), peer, peerID).start();
                    System.out.println("Server "  + peerID + " accepted Connection");
                }
            }
            finally {
                listener.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
