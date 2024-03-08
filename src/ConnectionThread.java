import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

public class ConnectionThread extends Thread{

    private Peer peer;
    private LinkedHashMap<Integer,String[]> peerData;
    private Socket connection;
    private String remoteHost;
    private int remotePort;
    private boolean handshake = false;

    private boolean isInitiatedByThisProcess;

    DataInputStream in;
    DataOutputStream out;
    public ConnectionThread(Socket connection, Peer peer, LinkedHashMap<Integer,String[]> peerData, boolean isInitiatedByThisProcess){
        this.peer = peer;
        this.peerData = peerData;
        this.connection = connection;
        this.remoteHost = this.connection.getInetAddress().getHostName();
        this.remotePort = this.connection.getPort();
        this.isInitiatedByThisProcess = isInitiatedByThisProcess;

        try {
            this.in = new DataInputStream(connection.getInputStream());
            this.out = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){

        System.out.println(peer.getPeerID() + " has started TCP connection with peer process " + remoteHost + " " + remotePort);

        if(!handshake){
            if(isInitiatedByThisProcess){
                byte[] handshakeMessage = Messages.getHandShakeMessage(peer.getPeerID());
                sendMessage(handshakeMessage, out);
                byte[] handShakeMessage = readHandShakeMessage(in);
                String handshakeHeader = new String(handShakeMessage, 0, 18);
                System.out.println("Handshake Header: " + handshakeHeader);

                // Extract zero bits (next 10 bytes)
                String zeroBits = new String(handShakeMessage, 18, 10);
                System.out.println("Zero Bits: " + zeroBits);

                // Extract peer ID (last 4 bytes)
                ByteBuffer buffer = ByteBuffer.wrap(handShakeMessage, 28, 4);
                int remotePeerID = buffer.getInt();
                System.out.println("Peer ID: " + remotePeerID);
                peer.fileLogger.makeConnection(remotePeerID);


            }
            else{
                byte[] handShakeMessage = readHandShakeMessage(in);
                String handshakeHeader = new String(handShakeMessage, 0, 18);
                System.out.println("Handshake Header: " + handshakeHeader);

                // Extract zero bits (next 10 bytes)
                String zeroBits = new String(handShakeMessage, 18, 10);
                System.out.println("Zero Bits: " + zeroBits);

                // Extract peer ID (last 4 bytes)
                ByteBuffer buffer = ByteBuffer.wrap(handShakeMessage, 28, 4);
                int remotePeerID = buffer.getInt();
                System.out.println("Peer ID: " + remotePeerID);
                byte[] handshakeMessage = Messages.getHandShakeMessage(peer.getPeerID());
                sendMessage(handshakeMessage, out);
                peer.fileLogger.isConnected(remotePeerID);


            }


        }



    }

    private void sendMessage(byte[] message, DataOutputStream out)  {
        try {
            out.write(message);
            out.flush();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private byte[] readHandShakeMessage(DataInputStream in){
        byte[] handshakeMessage = new byte[32];

        try {
           in.readFully(handshakeMessage);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        return handshakeMessage;
    }


}



