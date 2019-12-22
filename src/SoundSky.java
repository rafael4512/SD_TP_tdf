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
    //--------------------------------------//
    private volatile String lastSongN;
    private volatile String lastSongA;
    private volatile int songValue;

    public SoundSky(){

        this.users = new HashMap<String,User>();

        File dirS = new File("saves");
        if (!dirS.exists()) {
            dirS.mkdir();
        }

        File dirD = new File("downloads");
        if (!dirD.exists()) {
            dirD.mkdir();
        }

        File dirM = new File("musicas");
        if (!dirM.exists()) {
            dirM.mkdir();
        }

        File f = new File("saves/users.ser");
        if(f.exists() && !f.isDirectory()) {
            loadUsers();
        }

        this.musicas = new HashMap<Integer,Musica>();

        File g = new File("saves/musicas.ser");
        if(g.exists() && !g.isDirectory()) {
            loadMusics();
        }

        lastSongN = "null";
        lastSongA = "null";
        this.songValue = 0;

        lock = new ReentrantLock();

    }

    public void loadUsers(){

        try {
            FileInputStream fileIn = new FileInputStream("saves/users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            @SuppressWarnings("unchecked") HashMap<String,User> parse =
                (HashMap<String,User>)in.readObject();
            this.users = parse;
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void loadMusics(){

        try {
            FileInputStream fileIn = new FileInputStream("saves/musicas.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            @SuppressWarnings("unchecked") HashMap<Integer,Musica> parse =
                (HashMap<Integer,Musica>)in.readObject();
            this.musicas = parse;
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public void saveUsers(){
        lock.lock();
        try{
            FileOutputStream fos =
                new FileOutputStream("saves/users.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.users);
            oos.close();
            fos.close();
            System.out.println("Guardou users");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        lock.unlock();
    }

    public void saveMusics(){
        lock.lock();
        try{
            FileOutputStream fos =
                new FileOutputStream("saves/musicas.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.musicas);
            oos.close();
            fos.close();
            System.out.println("Guardou musicas");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        lock.unlock();
    }


    public boolean checkUser(String username, char[] password){
        lock.lock();
        if(this.users.containsKey(username)){
            if (isPasswordCorrect(this.users.get(username).getPassword(),password) && this.users.get(username).getStatus() == 0){
                this.users.get(username).setStatus(1);
                saveUsers();
                lock.unlock();

                return true;
            }
        }
        lock.unlock();

        return false;

    }


    public boolean addUser(String username,String password){

        User aux = new User(username,password,0);
        lock.lock();
        if(!this.users.containsKey(username)){
            this.users.put(username,aux);
            saveUsers();
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;

    }

    private static boolean isPasswordCorrect(char[] input,char[] correctPassword) {//Basta usar o .equals, pois este já verifica se a string é vazia

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

    public void newSongUpdater(String nome,String autor){
      lock.lock();

      if(this.songValue < 254)
        this.songValue+=1;
      else
        this.songValue = 0;

      this.lastSongA = autor;
      this.lastSongN = nome;
      lock.unlock();
    }

    public List<String> prcNome(String nome){
        List<String> yeet = new ArrayList<>();
        yeet.add("##");
        yeet.add("##  Unique ID  ###  Song Title  ###  Song Creator  ###  Release Year  ###  Tags  ###  Number of downloads  ##");
        lock.lock();
        Iterator it = musicas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            Musica inspect = (Musica) pair.getValue();
            if(inspect.getTitulo().contains(nome)){
                yeet.add("##");
                yeet.add("##   "+inspect.getId()+"  ###  "+inspect.getTitulo()+"  ###  "+inspect.getArtista()+"  ###  "+inspect.getAno()+"  ###  "+inspect.getEtiquetas()+"  ###  "+inspect.getDw()+"  ##");
            }
        }
        lock.unlock();
        yeet.add("##");
        return yeet;
    }

    public List<String> prcAutor(String autor){
      List<String> yeet = new ArrayList<>();
      yeet.add("##");
      yeet.add("##  Unique ID  ###  Song Title  ###  Song Creator  ###  Release Year  ###  Tags  ###  Number of downloads  ##");
      lock.lock();
      Iterator it = musicas.entrySet().iterator();
      while(it.hasNext())
      {
          Map.Entry pair = (Map.Entry) it.next();
          Musica inspect = (Musica) pair.getValue();
          if(inspect.getArtista().equals(autor)){
              yeet.add("##");
              yeet.add("##   "+inspect.getId()+"  ###  "+inspect.getTitulo()+"  ###  "+inspect.getArtista()+"  ###  "+inspect.getAno()+"  ###  "+inspect.getEtiquetas()+"  ###  "+inspect.getDw()+"  ##");
          }
      }
      lock.unlock();
      yeet.add("##");
      return yeet;
    }

    public List<String> prcEtiqueta(String tag){
        List<String> yeet = new ArrayList<>();
        yeet.add("##");
        yeet.add("##  Unique ID  ###  Song Title  ###  Song Creator  ###  Release Year  ###  Tags  ###  Number of downloads  ##");
        lock.lock();
        Iterator it = musicas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            Musica inspect = (Musica) pair.getValue();

            List<String> tags = inspect.getEtiquetas();
            if(tags.contains(tag)){
                yeet.add("##");
                yeet.add("##   "+inspect.getId()+"  ###  "+inspect.getTitulo()+"  ###  "+inspect.getArtista()+"  ###  "+inspect.getAno()+"  ###  "+inspect.getEtiquetas()+"  ###  "+inspect.getDw()+"  ##");
            }
        }
        lock.unlock();
        yeet.add("##");
        return yeet;
    }

    public Boolean checkSong(int uniqId){
        lock.lock();
        if(musicas.containsKey(uniqId)){
            lock.unlock();
            return true;}
        else{
            lock.unlock();
            return false;}
    }

    public Musica getMusica(int uniqId){
        lock.lock();
        Musica ret = new Musica(musicas.get(uniqId));
        lock.unlock();
        return ret;
    }

    public void incrementaDw(int uniqId){
        lock.lock(); //Se calhar este lock não é necessário, pois o increment() já bloqueia a música
        Musica ms = musicas.get(uniqId);
        ms.increment();
        musicas.put(uniqId,ms);
        lock.unlock();
    }

    public int getSongValue(){return this.songValue;}
    public String getNewSongName(){return this.lastSongN;}
    public String getNewSongAuthor(){return this.lastSongA;}
}
