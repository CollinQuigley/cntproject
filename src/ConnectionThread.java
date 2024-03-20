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

    private int remotePeerID;
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
        // eventually will need to loop constantly for incoming messages
        // until bitfield is full and all neighbors' bitfields are full
        try {
            // initial handshake and have messages
            if (!handshake) {
                doHandshake();

                byte[] bitfieldMessage = Messages.getBitfieldMessage(peer.bf.getBitfield());
                sendMessage(bitfieldMessage, out);
            }

            //for now while true for receiving messages. In the future implement loop to stop when all peer processes have received file
            while (true) {
                byte[] newMessage = readMessage(in);
                int messageType = newMessage[4];
                int messageLength = getMessageLength(newMessage);

                //switch statement for each message type DON"T FORGET break; FOR EACH CASE
                switch (messageType) {
                    //peer has received interested message from remotePeer. Set remotePeer to interested
                    case 2:
                        //System.out.println("Peer Process " + remotePeerID + " is interested in Peer Process " + peer.getPeerID() + "'s pieces");
                        peer.setInterested(remotePeerID);
                        break;
                    //peer has received not interested message from remotePeer. Set remotePeer to not interested
                    case 3:
                        //System.out.println("Peer Process " + remotePeerID + " is not interested in Peer Process " + peer.getPeerID() + "'s pieces");
                        peer.removeInterested(remotePeerID);
                        break;
                    // peer has received a bitfield message (optional)
                    case 5:
                        byte[] payload = Arrays.copyOfRange(newMessage, 5, messageLength - 1 + 5);
                        peer.createOtherPeerBitField(remotePeerID, payload);

                        if (Arrays.equals(peer.bf.getBitfield(), peer.getOtherPeerBitField(remotePeerID).getBitfield()) || peer.hasFile) {

                            byte[] notInterestedMessage = Messages.getNotInterestedMessage();
                            sendMessage(notInterestedMessage, out);

                        } else {

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


            // Extract zero bits (next 10 bytes)
            String zeroBits = new String(handShakeMessage, 18, 10);


            // Extract peer ID (last 4 bytes)
            ByteBuffer buffer = ByteBuffer.wrap(handShakeMessage, 28, 4);
            this.remotePeerID = buffer.getInt();

            peer.fileLogger.makeConnection(remotePeerID);
        }
        else{
            byte[] handShakeMessage = readHandShakeMessage(in);
            String handshakeHeader = new String(handShakeMessage, 0, 18);



            String zeroBits = new String(handShakeMessage, 18, 10);


            // Extract peer ID (last 4 bytes)
            ByteBuffer buffer = ByteBuffer.wrap(handShakeMessage, 28, 4);
            this.remotePeerID = buffer.getInt();

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



