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
        int connectionsAccepted = 0;
        int numConnections = Integer.parseInt(peerData.get(this.peerID)[3]);
        System.out.println(numConnections);
        try {
            ServerSocket listener = new ServerSocket(Integer.parseInt(peerData.get(this.peerID)[1]));
            try{
                while(connectionsAccepted < numConnections){
                    new ConnectionThread(listener.accept(), peer, peerData, false).start();
                    System.out.println("Server "  + peerID + " accepted Connection");
                    connectionsAccepted++;
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
