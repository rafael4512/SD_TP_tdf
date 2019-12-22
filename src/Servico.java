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
import java.util.Iterator;

public class Servico implements Runnable {

    private Socket cliente;
    private SoundSky sound;
    private int k = 0;
    private String name = null;

    public Servico(Socket cliente,SoundSky s) {

        this.cliente = cliente;
        this.sound = s;

    }

    private void register(BufferedReader in, PrintWriter out){

        String psw = null;
        String usr = null;
        try{
            while(usr==null){
                out.println("Please enter your new Username:");
                out.flush();
                usr = in.readLine();
            }
            while(psw==null){
                out.println("Please enter your new Password:");
                out.flush();
                psw = in.readLine();
            }

            if(sound.addUser(usr,psw)){
                out.println("Your account has been successfully created!");
                out.flush();
                File newDir = new File("downloads/" + usr);
                newDir.mkdir();
            }
            else{
                out.println("That Username is already in use.");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private void login(BufferedReader in, PrintWriter out){

        String psw = null;
        String usr = null;
        try{
            while(usr==null){
                out.println("Please enter your Username:");
                out.flush();
                usr = in.readLine();
            }
            while(psw==null){
                out.println("Please enter your Password:");
                out.flush();
                psw = in.readLine();
            }

            if(sound.checkUser(usr,psw.toCharArray())){
                name = usr;
                out.println("Welcome " + usr);
                out.flush();
                k=1;
            }

            else{
                out.println("The Username/Password you inserted is/are incorrect.");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void transferMusic(int uniqId,BufferedReader in, PrintWriter out){
        if(sound.checkSong(uniqId)){
            //Start Transfer
            Musica toTransfer = sound.getMusica(uniqId);
            out.println("Music information starting");
            out.flush();
            out.println(this.name);
            out.flush();
            out.println(toTransfer.getTitulo());
            out.flush();
            File music = new File("musicas/"+toTransfer.getTitulo()+".mp3");
            try{
                InputStream targetStream = new FileInputStream(music);
                byte[] buf = new byte[850000];

                for (int readNum; (readNum = targetStream.read(buf)) != -1;){
                    byte[] bytes = new byte[readNum];
                    System.arraycopy(buf,0,bytes,0,readNum);

                    String send = Base64.getEncoder().encodeToString(bytes);
                    out.println(send);
                    out.flush();
                }
                out.println("Sending Finished");
                out.flush();

                sound.incrementaDw(uniqId);
                sound.saveMusics();
            }catch(Exception e){}
        }
        else{out.println("Song with given ID does not exist in our database");out.flush();}
    }

    public void selectEtiquetas(List<String> etiquetas, BufferedReader in, PrintWriter out){
           int i=0;
           try{
              String etiqueta = in.readLine();
              if(etiqueta==null) etiquetas.add("NULL");
              else{
                  if(etiqueta.contains("1")){etiquetas.add("POP");i++;}
                  if(etiqueta.contains("2")){etiquetas.add("ROCK");i++;}
                  if(etiqueta.contains("3")){etiquetas.add("EDM");i++;}
                  if(i==0){etiquetas.add("NULL");}
              }
           }catch(Exception e){}
    }

    public void receiveMusic(BufferedReader in, PrintWriter out){
        try{
            out.println("Please enter the song name:");
            out.flush();
            String name = in.readLine();

            out.println("Please enter the song year:");
            out.flush();
            String year = in.readLine();

            out.println("Please enter the author of the song:");
            out.flush();
            String autor = in.readLine();

            List<String> etiquetas = new ArrayList<>();

            out.println("Please select the Tags for this song:");
            out.flush();
            out.println("1-POP 2-ROCK 3-EDM (Separate the tags using a space)");
            out.flush();
            selectEtiquetas(etiquetas,in,out);

            out.println("ready for receival.");
            out.println(name);
            out.flush();
            String confirmation = in.readLine();
            if(confirmation.equals("music data incoming")){
                File someFile = new File("musicas/"+name+".mp3");
                FileOutputStream fos = new FileOutputStream(someFile);
                while(true){

                    String data = in.readLine();
                    if(data.equals("sending Finished"))
                      break;
                    byte[] decodedString = Base64.getDecoder().decode(data.getBytes("UTF-8"));
                    fos.write(decodedString);
                    fos.flush();
                }
                fos.close();
                int idenUniq = sound.addMusica(name,autor,year,etiquetas);
                out.println("Unique Identifier::" + idenUniq);
                sound.saveMusics();
                sound.newSongUpdater(name,autor);
              }
        }catch(Exception e){}
    }

    public void sBn(BufferedReader in, PrintWriter out){
        out.println("## Which name would you like to search for? ##");
        out.flush();
        String name="null";
        try{
          name = in.readLine();
        }catch(Exception e){}

        List<String> print = sound.prcNome(name);
    		Iterator<String> it = print.iterator();
    		while(it.hasNext()){
    	      out.println(it.next());
            out.flush();
        }
        out.println("## Which song would you like to download? ##");
        out.flush();
        String input = "-1";
        try{
            input = in.readLine();
        }catch(Exception e){}
        transferMusic(Integer.parseInt(input),in,out);
        out.println("## Song Downloaded ##");
        out.flush();
    }

    public void sBe(BufferedReader in, PrintWriter out){
        out.println("## Please select the Tags you wish to search ##");
        out.println("1-POP 2-ROCK 3-EDM (Separate the tags using a space)");
        out.flush();
        int chosen=0;
        try{
          chosen = Integer.parseInt(in.readLine());
        }catch(Exception e){}
        String tag;

        switch(chosen){
          case 1 :
              tag = "POP";
              break;
          case 2 :
              tag = "ROCK";
              break;
          case 3 :
              tag = "EDM";
              break;
          default :
              tag = "NULL";
        }

        List<String> print = sound.prcEtiqueta(tag);
		    Iterator<String> it = print.iterator();
		    while(it.hasNext()){
			     out.println(it.next());
           out.flush();
        }

        out.println("## Which song would you like to download? ##");
        out.flush();
        String input = "-1";
        try{
          input = in.readLine();
        }catch(Exception e){}

        transferMusic(Integer.parseInt(input),in,out);
        out.println("## Song Downloaded ##");
        out.flush();
    }

    public void sBi(BufferedReader in, PrintWriter out){
        out.println("## Please insert the Unique ID of the song you wish to download: ##");
        out.flush();
        String uniqId = "-1";
        try{
            uniqId = in.readLine();
        }catch(Exception e){}
        transferMusic(Integer.parseInt(uniqId),in,out);
        out.println("## Song Downloaded ##");
        out.flush();
    }

    public void sBa(BufferedReader in, PrintWriter out){
        out.println("## Please insert the author name for whom you would like to search? ##");
        out.flush();
        String autor = "null";
        try{
            autor = in.readLine();
        }catch(Exception e){}

        List<String> print = sound.prcAutor(autor);
  		  Iterator<String> it = print.iterator();
  		  while(it.hasNext()){
  			     out.println(it.next());
             out.flush();
        }

        out.println("## Which song would you like to download? ##");
        out.flush();
        String input = "-1";
        try{
          input = in.readLine();
        }catch(Exception e){}

        transferMusic(Integer.parseInt(input),in,out);
        out.println("## Song Downloaded ##");
        out.flush();
    }

    public void menuLogin(PrintWriter out){
        out.println("#########################################");
        out.println("##            1-Login                  ##");
        out.println("##            2-Register               ##");
        out.println("##            0-Exit                   ##");
        out.println("#########################################");
        out.flush();
    }

    public void menu2(PrintWriter out){
        out.println("#########################################");
        out.println("##            1-Upload music           ##");
        out.println("##            2-Search music           ##");
        out.println("##            0-Logout                 ##");
        out.println("#########################################");
        out.flush();
    }

    public void menuProcura(PrintWriter out){
      out.println("##########################################");
      out.println("##   1-Search using Unique Identifier   ##");
      out.println("##       2-Search Songs by Tags         ##");
      out.println("##          3-Search by Author          ##");
      out.println("##           4-Search by Name           ##");
      out.println("##             5-Go Back                ##");
      out.println("##########################################");
      out.flush();
    }

    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter out = new PrintWriter(cliente.getOutputStream());

            Thread updater = new Thread(new newSongUpdate(this.sound,out));
            updater.start();

            while (true) {

                if(k==0){menuLogin(out);}
                if(k==1){menu2(out);}
                if(k==2){menuProcura(out);}

                String s = in.readLine();   /** Le o que foi escrito no socket do cliente **/

                if ((s==null || s.equals("0")) && k!=2){
                    sound.cS(name);/** Se o cliente escreveu Quit fecha-se a conexao com o cliente **/
                    break;
                }
                if ((s==null) && k==2){
                    sound.cS(name);
                    break;
                }
                if((s.equals("5")) && k==2){ k=1;continue;}
                if(s.equals("1") && k==0){
                    login(in,out);
                    continue;
                }
                if(s.equals("2") && k==0){
                    register(in,out);
                    continue;
                }
                if(s.equals("1") && k==1){
                    receiveMusic(in,out);
                    continue;
                }
                if(s.equals("2") && k==1){
                    k=2;
                    continue;
                }

                if(s.equals("1") && k==2){ // Procura por ID
                  sBi(in,out);
                  k=1;
                  continue;
                }

                if(s.equals("2") && k==2){ // Procura por Etiqueta
                  sBe(in,out);
                  k=1;
                  continue;
                }

                if(s.equals("3") && k==2){ // Procura por Autor
                  sBa(in,out);
                  k=1;
                  continue;
                }

                if(s.equals("4") && k==2){ // Procura por Autor
                  sBn(in,out);
                  k=1;
                  continue;
                }
            }

        }catch(IOException e){
            System.out.println(e);
        }

        finally{
            try{
                cliente.shutdownOutput();  /** Fecha o lado de escrita do socket do cliente **/
                cliente.shutdownInput();   /** Fecha o lado de leitura do socket do cliente **/
                cliente.close();           /** Fecha o socket do cliente **/
            }
            catch(IOException e) { System.out.println(e);}
        }
    }
}
