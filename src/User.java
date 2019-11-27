public class User {
    private int id=0;
    private String username;
    private char[] password;

    public User() {
        this.id=-1;
        this.username="N/A";
        password=null;
    }

    public User(String username, String password) {
        this.id=id++;
        this.username = username;
        this.password = password.toCharArray();
    }
    public User(User u){
        this.id=u.getId();
        this.username=u.getUsername();
        this.password=u.getPassword();
    }

    public int getId(){
        return id;
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
        return "UserName:"+this.username;
    }
}
