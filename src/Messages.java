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

    public static byte[] getBitfieldMessage(byte[] bf) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Start with 1 byte for the message type field
        int length = 1;


        length += bf.length;

        // create a 4 byte buffer to hold the length variable
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(length);
        buffer.flip();

        stream.write(buffer.array());
        stream.write(5);
        stream.write(bf);
        byte[] message = stream.toByteArray();
        stream.close();

        return message;
    }

    public static byte[] getChokeMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(1);

        // type already set to 0
        byte[] type = new byte[1];
        buffer.put(type);

        return buffer.array();
    }

    public static byte[] getUnchokeMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(1);

        byte[] type = new byte[1];
        type[0] = 1;
        buffer.put(type);

        return buffer.array();
    }

    public static byte[] getNotInterestedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(1);

        byte[] type = new byte[1];
        type[0] = 3;
        buffer.put(type);

        return buffer.array();
    }

    public static byte[] getInterestedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(1);

        byte[] type = new byte[1];
        type[0] = 2;
        buffer.put(type);

        return buffer.array();
    }

}
