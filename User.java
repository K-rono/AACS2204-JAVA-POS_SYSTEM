package POS;

/**
 *
 * @author qihong
 */
public abstract class User {
    private String username;
    private String userID;
    private String userPassword;

    public User(String username, String userID, String userPassword) {
        this.username = username;
        this.userID = userID;
        this.userPassword = userPassword;
    }
    
    //Setters and Getters
    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString(){
        return "#";
    }
    
}
