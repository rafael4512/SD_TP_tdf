import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username;
    private char[] password;
    private List<Integer> downSongs;
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
        this.downSongs = new ArrayList<>();
    }

    //construtor cópia de um user
    public User(User u){
        this.username=u.getUsername();
        this.password=u.getPassword();
        this.status=u.getStatus();
        this.downSongs=u.getDownSongs();
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

    //get das musicas descarregadas por parte do user
    public List<Integer> getDownSongs(){
        return downSongs;
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

    //metodo para adicionar música aos downloads do user
    public void addSong(int uniqId){
        this.downSongs.add(uniqId);
    }

    //método toString de um user (mostrar username)
    public String toString(){
        return "UserName: "+this.username;
    }
}
