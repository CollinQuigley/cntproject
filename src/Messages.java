import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;

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

    public static byte[] getHaveMessage(BitSet bf) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Start with 1 byte for the message type field
        int length = 1;

        byte[] payload = bf.toByteArray();
        length += payload.length;

        // create a 4 byte buffer to hold the length variable
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(length);
        buffer.flip();

        stream.write(buffer.array());
        stream.write(5);
        stream.write(payload);
        stream.close();

        return stream.toByteArray();
    }

}
