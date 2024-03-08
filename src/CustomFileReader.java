import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
public class CustomFileReader {

     public static LinkedHashMap<String, String> readCommon(String path){
          LinkedHashMap<String, String> commonData = new LinkedHashMap<>();

          try (BufferedReader br = new BufferedReader(new FileReader(path))) {
               String line;
               while ((line = br.readLine()) != null) {
                    String[] parts = line.trim().split(" ", 2);
                    if (parts.length == 2) {
                         String key = parts[0];
                         String value = parts[1];
                         commonData.put(key, value);
                    }
               }
          } catch (IOException e) {
               e.printStackTrace();
          }

          return commonData;
     }

     public static LinkedHashMap<Integer, String[]> readPeerInfo(String path){
          LinkedHashMap<Integer, String[]> peerInfo = new LinkedHashMap<>();
          int numLines = countLines(path);
          try (BufferedReader br = new BufferedReader(new FileReader(path))) {
               String line;
               while ((line = br.readLine()) != null) {
                    String[] parts = line.trim().split(" ", 4);
                    numLines = numLines - 1;
                    if (parts.length == 4) {
                         int peerID = Integer.parseInt(parts[0]);
                         String[] infoArray = new String[parts.length];
                         infoArray[0] = parts[1];
                         infoArray[1] = parts[2];
                         infoArray[2] = parts[3];
                         infoArray[3] = Integer.toString(numLines);
                         peerInfo.put(peerID, infoArray);
                    }
               }
          } catch (IOException e) {
               e.printStackTrace();
          }

          return peerInfo;

     }

     private static int countLines(String filePath) {
          int lineCount = 0;
          try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
               while (reader.readLine() != null) {
                    lineCount++;
               }
          } catch (IOException e) {
               e.printStackTrace();
          }
          return lineCount;
     }
}
