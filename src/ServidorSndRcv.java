import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

public class ServidorSndRcv implements Runnable{

    private String operation;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader in2;
    PrintWriter out2;
    private SoundSky sound;

    private int uniqId;
    private String name1;

    private String pathF;
    private String name2;
    private String year;
    private String autor;
    private List<String> etiquetas;
    private String filename;

    //construtor por atribuição
    public ServidorSndRcv(String op,BufferedReader i,PrintWriter o,BufferedReader i2,PrintWriter o2,SoundSky ss,String path,String nm2,String ano,String aut,List<String> etiq,String filen){
        operation = op;
        in = i;
        in2 = i2;
        out = o;
        out2 = o2;
        sound = ss;
        pathF = path;
        name2 = nm2;
        year = ano;
        autor = aut;
        etiquetas = etiq;
        filename = filen;
    }

    //construtor por atribuição
    public ServidorSndRcv(String op,BufferedReader i,PrintWriter o,BufferedReader i2,PrintWriter o2,SoundSky ss,int uID,String nm1){
        operation = op;
        in = i;
        in2 = i2;
        out = o;
        out2 = o2;
        sound = ss;
        uniqId = uID;
        name1 = nm1;
    }

    //método para transferir uma música
    public void transferMusic(){
        if(sound.checkSong(uniqId)){
            //Start Transfer
            Musica toTransfer = sound.getMusica(uniqId);
            out.println("Music information starting");
            out.flush();
            out2.println(this.name1);
            out2.flush();
            out2.println(toTransfer.getFilename());
            out2.flush();
            File music = new File("musicas/"+toTransfer.getFilename());
            try{
                sound.incrementPessoas();
                InputStream targetStream = new FileInputStream(music);
                byte[] buf = new byte[850000];

                for (int readNum; (readNum = targetStream.read(buf)) != -1;){
                    byte[] bytes = new byte[readNum];
                    System.arraycopy(buf,0,bytes,0,readNum);

                    String send = Base64.getEncoder().encodeToString(bytes);
                    out2.println(send);
                    out2.flush();
                }
                out2.println("Sending Finished");
                out2.flush();

                /*try{
                    TimeUnit.SECONDS.sleep(10);
                }
                catch(InterruptedException e){;}*/
                sound.decrementPessoas();
                sound.incrementaDw(uniqId);
                sound.addToUser(name1,uniqId);
                sound.saveMusics();
                sound.saveUsers();
                out.println("## "+ toTransfer.getTitulo() +" by "+ toTransfer.getArtista() +" downloaded ##");
                out.flush();
            }catch(Exception e){}
        }
        else{out.println("Song with given ID does not exist in our database");out.flush();}
    }

    //método para dar upload de uma música fornecendo informações sobre esta
    public void receiveMusic(){
        try{
            out.println("ready for receival.");
            out.flush();
            out2.println(pathF);
            out2.flush();
            String confirmation = in2.readLine();
            if(confirmation.equals("music data incoming")){
                File someFile = new File("musicas/"+ filename);
                FileOutputStream fos = new FileOutputStream(someFile);
                while(true){

                    String data = in2.readLine();
                    if(data.equals("sending Finished"))
                      break;
                    byte[] decodedString = Base64.getDecoder().decode(data.getBytes("UTF-8"));
                    fos.write(decodedString);
                    fos.flush();
                }
                fos.close();
                int idenUniq = sound.addMusica(name2,autor,year,etiquetas,filename);
                out.println("## "+ name2 +" by "+ autor +" uploaded with ID:"+idenUniq+" ##");
                out.flush();
                sound.saveMusics();
                sound.newSongUpdater(name2,autor);
            }else{
              out.println("Given song name does not exist");
              out.flush();
            }
          }catch(Exception e){}
        }

    //método run
    public void run(){
      if(operation.equals("receive")){
        this.receiveMusic();
      }
      else{
        this.transferMusic();
      }
    }
  }
