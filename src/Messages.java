import java.nio.ByteBuffer;

public class Messages {

    public static byte[] getHandShakeMessage(int peerID){
        ByteBuffer buffer = ByteBuffer.allocate(32);

        // Handshake header: "P2PFILESHARINGPROJ"
        buffer.put("P2PFILESHARINGPROJ".getBytes());

        // Zero bits
        buffer.put(new byte[10]);

        // Peer ID
        buffer.putInt(peerID);

        return buffer.array();
    }

}
