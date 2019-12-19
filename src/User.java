import java.io.*;

public class User implements Serializable{
    private static final long serialVersionUID = 1L; 
    private String username;
    private char[] password;
    private int status; // 0 = está Offline || 1 =  está online

    public User() {
        this.username="N/A";
        password=null;
        status=0;
    }

    public User(String username, String password, int status) {
        this.username = username;
        this.password = password.toCharArray();
        this.status = status;
    }
    
    public User(User u){
        this.username=u.getUsername();
        this.password=u.getPassword();
        this.status=u.getStatus();
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public int getStatus() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString(){
        return "UserName: "+this.username;
    }
}
