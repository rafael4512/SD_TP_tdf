import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class SoundSky{

    private Map<String,User> users;
    
    public SoundSky(){
        
        this.users = new HashMap<String, User>();
    
    }

    public boolean checkUser(String username, char[] password){
        
        if(this.users.containsKey(username)){
            if (isPasswordCorrect(password , this.users.get(username).getPassword()))
                return true;
        }
        
        return false;

    }

    public void exemplo(){

        User aux1 = new User("bruno","crl1",0);
        User aux2 = new User("rodolfo","crl2",0);
        User aux3 = new User("rafael","crl3",0);
        User aux4 = new User("pedro","crl4",0);
     
        this.users.put("bruno",aux1);
        this.users.put("rodolfo",aux2);
        this.users.put("rafael",aux3);
        this.users.put("pedro",aux4);
    }

    public boolean addUser(String username,String password){

        User aux = new User(username,password,0);
    
        if(!this.users.containsKey(username)){
            this.users.put(username,aux);
            return true;
        }
        
        return false;
    
    }
    
    private static boolean isPasswordCorrect(char[] input,char[] correctPassword) {
    
        boolean isCorrect = true;

        if (input.length != correctPassword.length || input == null) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals (input, correctPassword);
        }

        //Zero out the password.
        Arrays.fill(correctPassword,'0');

        return isCorrect;
    }
}
