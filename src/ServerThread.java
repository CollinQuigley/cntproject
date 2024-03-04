import java.net.Socket;

public class ServerThread extends Thread{
     private Socket clientSocket;
     private Peer peer;
     private int peerID;
     public ServerThread(Socket clientSocket, Peer peer, int peerID){
         this.clientSocket = clientSocket;
         this.peer = peer;
         this.peerID = peerID;
     }

     @Override
    public void run(){

         System.out.println("peerProcess: " + peerID + "accepted connection for another peer");
     }
}
