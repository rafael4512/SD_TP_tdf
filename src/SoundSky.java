import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

public class SoundSky implements Serializable{
    private static final long serialVersionUID = 1L; 
    private HashMap<String,User> users;
    private HashMap<Integer,Musica> musicas;
    ReentrantLock lock;
    
    public SoundSky(){

        this.users = new HashMap<String,User>();
        
        File f = new File("users.ser"); 
        if(f.exists() && !f.isDirectory()) { 
            loadUsers();
        }

        this.musicas = new HashMap<Integer,Musica>();
        
        File g = new File("musicas.ser"); 
        if(g.exists() && !g.isDirectory()) { 
            loadMusics();
        }


        lock = new ReentrantLock();

    }

    public void loadUsers(){

        try {
            FileInputStream fileIn = new FileInputStream("users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.users = (HashMap<String,User>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void loadMusics(){

        try {
            FileInputStream fileIn = new FileInputStream("musicas.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.musicas = (HashMap<Integer,Musica>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public void saveUsers(){

        try{
            FileOutputStream fos =
                new FileOutputStream("users.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.users);
            oos.close();
            fos.close();
            System.out.println("Guardou users");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

    public void saveMusics(){

        try{
            FileOutputStream fos =
                new FileOutputStream("musicas.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.musicas);
            oos.close();
            fos.close();
            System.out.println("Guardou musicas");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }


    public boolean checkUser(String username, char[] password){

        if(this.users.containsKey(username)){

            lock.lock();

            if (isPasswordCorrect(this.users.get(username).getPassword(),password) && this.users.get(username).getStatus() == 0){
                this.users.get(username).setStatus(1);
                saveUsers();
                lock.unlock();

                return true;
            }

            lock.unlock();
        }

        return false;

    }


    public boolean addUser(String username,String password){

        User aux = new User(username,password,0);

        if(!this.users.containsKey(username)){
            this.users.put(username,aux);
            saveUsers();
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
            saveUsers();
        }
        lock.unlock();
    }

    public int addMusica(String nome,String autor,String ano,List<String> etiquetas){
        lock.lock();
        int idenUniq = musicas.size() + 1;
        Musica insert = new Musica(nome,autor,Integer.parseInt(ano),etiquetas,idenUniq);
        musicas.put(idenUniq,insert);
        saveMusics();
        lock.unlock();
        return idenUniq;
    }

    public List<String> prcEtiqueta(String tag){
        List<String> yeet = new ArrayList<>();
        yeet.add("Unique ID<----->Song Title<----->Song Author<----->Release Year<----->Number of downloads");
        Iterator it = musicas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            Musica inspect = (Musica) pair.getValue();

            List<String> tags = inspect.getEtiquetas();
            if(tags.contains(tag))
                yeet.add(""+inspect.getId()+"<----->"+inspect.getTitulo()+"<----->"+inspect.getArtista()+"<----->"+inspect.getAno()+"<----->"+inspect.getDw());
        }
        return yeet;
    }
}
