import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SoundSky{

    private Map<String,User> users;
    private Map<Integer,Musica> musicas;
    ReentrantLock lock;
    
    public SoundSky(){

        this.users = new HashMap<String,User>();
        this.musicas = new HashMap<Integer,Musica>();
        lock = new ReentrantLock();
    }

    public boolean checkUser(String username, char[] password){

        if(this.users.containsKey(username)){

            lock.lock();

            if (isPasswordCorrect(this.users.get(username).getPassword(),password) && this.users.get(username).getStatus() == 0){
                this.users.get(username).setStatus(1);

                lock.unlock();

                return true;
            }

            lock.unlock();
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

    public void cS(String username){
        lock.lock();
        if(username!=null){
            users.get(username).setStatus(0);
        }
        lock.unlock();
    }

    public int addMusica(String nome,String autor,String ano,List<String> etiquetas){
        lock.lock();
        int idenUniq = musicas.size() + 1;
        Musica insert = new Musica(nome,autor,Integer.parseInt(ano),etiquetas,idenUniq);
        musicas.put(idenUniq,insert);
        lock.unlock();
        return idenUniq;
    }
}
