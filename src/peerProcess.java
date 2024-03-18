import java.sql.SQLOutput;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;
public class peerProcess {

    static final String commonPath = "./project_config_file_large/Common.cfg";
    static final String peerInfoPath = "./project_config_file_large/PeerInfo.cfg";
    public static void main(String[] args) {

         int peerID;

         if(args.length != 1){
             System.out.println("Error: Incorrect number of arguments");
             return;
         }
         else{
             String firstArgument = args[0];
             try{
                 peerID = Integer.parseInt(firstArgument);
             }
             catch(NumberFormatException e){
                 System.out.println("Error: The argument is not an integer");
                 return;
             }
         }

         //Bitfield.initializeBitfield();

        LinkedHashMap<String,String> commonData = CustomFileReader.readCommon(commonPath);
        LinkedHashMap<Integer,String[]> peerData = CustomFileReader.readPeerInfo(peerInfoPath);


      Peer peer = new Peer(commonData, peerData, peerID);

      Client client = new Client(peer, peerData);
      client.start();

      Server server = new Server(peer, peerID, peerData);
      server.start();



        /*for (Map.Entry<String, String> entry : commonData.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }


        for (Map.Entry<Integer, String[]> entry : peerData.entrySet()) {
            System.out.print("Peer ID: " + entry.getKey() + ", Info Array: ");
            String[] infoArray = entry.getValue();
            for (String info : infoArray) {
                System.out.print(info + " ");
            }
            System.out.println();
        }*/
       while(Thread.activeCount() > 1){

       }

       peer.fileLogger.close();
    }
}