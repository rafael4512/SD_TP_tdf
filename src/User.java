public class User {

    private String username;
    private char[] password;

    public User() {
        this.username="N/A";
        password=null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password.toCharArray();
    }
    public User(User u){
        this.username=u.getUsername();
        this.password=u.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    public String toString(){
        return "UserName: "+this.username;
    }
}
