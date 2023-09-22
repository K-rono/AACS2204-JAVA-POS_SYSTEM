package POS;

/**
 *
 * @author qihong
 */
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginSystem {
    private static Map<String,String> userCredentials = new HashMap<>();
    private static final String MEMBER_FILE_PATH = "src/Data/member.txt";
    private static final String STAFF_FILE_PATH = "src/Data/staff.txt";
    private static final String MEMBER_INFO_FILE_PATH = "src/Data/memberInfo.txt";
    private static final String STAFF_INFO_FILE_PATH = "src/Data/staffInfo.txt";
    private static String file_path;
    
    public static void readCredentialsFile(int mode){
        
        //determining to read STAFF credentials or MEMBER credentials
        if(mode == 0){
            file_path = STAFF_FILE_PATH;
        }
        else{
            file_path = MEMBER_FILE_PATH;
        }
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
            
                userCredentials.put(data[0],data[1]);
            }
        }
        catch(IOException e){
            System.out.println("Error occured when reading User's Credentials file");
        }
    }
    
    //returns true or false
    public static boolean authentication(String userID, String userPassword){
        //userID doesn't exist
        if(!(userCredentials.containsKey(userID))){
            return false;
        }
        
        String storedPassword = userCredentials.get(userID);
        return storedPassword.equals(userPassword);
    }
    
    public static Object loadUserData(int mode){
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
            
                if(mode == 0){
                    return new Staff(data[0],data[1],data[2],data[3]);
                }
                else{
                    return new Member(data[0],data[1],data[2],Integer.parseInt(data[3]));
                }
            }
        }
        catch(IOException e){
            System.out.println("Error occured when reading User's Information file");
        }
        finally{
            return null;
        }
    }
    
}
