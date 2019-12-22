import java.io.*;

public class User implements Serializable{
    private static final long serialVersionUID = 1L; 
    private String username;
    private char[] password;
    private int status; // 0 = está Offline || 1 =  está online

    //construtor por omissão de um user
    public User() {
        this.username="N/A";
        password=null;
        status=0;
    }

    //construtor por atribuição de um user
    public User(String username, String password, int status) {
        this.username = username;
        this.password = password.toCharArray();
        this.status = status;
    }
    
    //construtor cópia de um user
    public User(User u){
        this.username=u.getUsername();
        this.password=u.getPassword();
        this.status=u.getStatus();
    }

    //get do username
    public String getUsername() {
        return username;
    }

    //get da password
    public char[] getPassword() {
        return password;
    }

    //get dos status(0-offline, 1-online)
    public int getStatus() {
        return status;
    }

    //set username do user
    public void setUsername(String username) {
        this.username = username;
    }

    //set da password do user
    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    //set dos status do user(0-offline, 1-online)
    public void setStatus(int status) {
        this.status = status;
    }

    //método toString de um user (mostrar username)
    public String toString(){
        return "UserName: "+this.username;
    }
}
