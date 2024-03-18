import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private String logFileName;

    private BufferedWriter fileWriter;
    private int peerID;

    public FileLogger(int peerID){
        this.peerID = peerID;
        this.logFileName = "log_peer_" + this.peerID+ ".log";
        try {

            fileWriter = new BufferedWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void makeConnection(int remotePeerID){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String message = "[" + formattedTime + "]: Peer [" + peerID + "] makes a connection to Peer [" + remotePeerID + "]";

        try {
            fileWriter.write(message);
            fileWriter.newLine();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void isConnected(int remotePeerID){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String message = "[" + formattedTime + "]: Peer [" + peerID + "] is connected from Peer [" + remotePeerID + "]";

        try {
            fileWriter.write(message);
            fileWriter.newLine();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
