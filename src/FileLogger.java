import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private String logFileName;

    private BufferedWriter fileWriter;
    private int peerID;

    private final Object lock = new Object();

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
        //ensure that only one thread can write to the file at a time
        synchronized (lock) {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);

            String message = "[" + formattedTime + "]: Peer [" + peerID + "] makes a connection to Peer [" + remotePeerID + "]";
            System.out.println(message);
            try {
                fileWriter.write(message);
                fileWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void isConnected(int remotePeerID){
        //ensure that only one thread can write to the file at a time
        synchronized (lock) {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);

            String message = "[" + formattedTime + "]: Peer [" + peerID + "] is connected from Peer [" + remotePeerID + "]";
            System.out.println(message);
            try {
                fileWriter.write(message);
                fileWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void receivedNotInterested(int remotePeerID){
        //ensure that only one thread can write to the file at a time
        synchronized (lock){
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            String message = "[" + formattedTime + "]: Peer [" + peerID + "] received the 'not interested' message from [" + remotePeerID + "]";
            System.out.println(message);
            try {
                fileWriter.write(message);
                fileWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void receivedInterested(int remotePeerID){
        //ensure that only one thread can write to the file at a time
        synchronized (lock){
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            String message = "[" + formattedTime + "]: Peer [" + peerID + "] received the 'interested' message from [" + remotePeerID + "]";
            System.out.println(message);
            try {
                fileWriter.write(message);
                fileWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeOptimisticallyUnchoked(int remotePeerID){
        //ensure that only one thread can write to the file at a time
        synchronized (lock){
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            String message = "[" + formattedTime + "]: Peer [" + peerID + "] has the optimistically unchoked neighbor [" + remotePeerID + "]";
            System.out.println(message);
            try {
                fileWriter.write(message);
                fileWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() {
        synchronized (lock) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
