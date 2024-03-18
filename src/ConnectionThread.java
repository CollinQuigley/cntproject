import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
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

        // eventually will need to loop constantly for incoming messages
        // until bitfield is full and all neighbors' bitfields are full
        try{
            // initial handshake and have messages
            if(!handshake) {
                doHandshake();

                byte[] bitfieldMessage = Messages.getBitfieldMessage(peer.getBitfield());
                sendMessage(bitfieldMessage, out);
            }

            byte[] newMessage = readMessage(in);
            int messageType = newMessage[4];
            int messageLength = getMessageLength(newMessage);

            switch(messageType) {
                // peer has received a bitfield message (optional)
                case 5:
                    byte[] payloadArr = Arrays.copyOfRange(newMessage, 5, messageLength + 5);
                    BitSet payload = BitSet.valueOf(payloadArr);

                    // check if connected peer has data the current peer does not have
                    if(peer.getBitfield().equals(payload)) {
                        byte[] notInterestedMessage = Messages.getNotInterestedMessage();
                        sendMessage(notInterestedMessage, out);
                    }
                    else {
                        byte[] interestedMessage = Messages.getInterestedMessage();
                        sendMessage(interestedMessage, out);
                    }
                    break;

                // default catch all
                default:
                    System.out.println("Unsupported message type " + messageType);
                    break;
            }
        }
        catch (Exception e) {
            System.out.println(e);
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

    public void doHandshake() throws IOException {
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

    private byte[] readMessage(DataInputStream in){
        // allocate 4 bytes then wrap to bytebuffer to get integer value of message length
        byte[] lengthBytes = new byte[4];
        byte[] message;
        int messageLength = 0;
        ByteBuffer bf;

        try {
            in.readFully(lengthBytes);
            bf = ByteBuffer.wrap(lengthBytes);
            messageLength = bf.getInt();

            // create new byte array and readFully into it, then append remaining message to bf
            message = new byte[messageLength + 4];
            System.arraycopy(lengthBytes, 0, message, 0, 4);
            in.readFully(message, 4, messageLength);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    private int getMessageLength(byte[] message){
        byte[] subArray = Arrays.copyOfRange(message, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(subArray);
        return buffer.getInt();
    }
}



