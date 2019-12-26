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
    private Socket comms;
    private SoundSky sound;
    private int k = 0;
    private String name = null;

    private BufferedReader in2;
    private PrintWriter out2;

    //construtor de um servico
    public Servico(Socket cliente,Socket comms,SoundSky s) {

        this.cliente = cliente;
        this.comms = comms;
        this.sound = s;

    }

    //método de registar/verificar clientes
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
                out.println("create file");
                out.flush();
                out.println(usr);
                out.flush();
            }
            else{
                out.println("That Username is already in use.");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    //método para dar login a um cliente
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

    //método para selecionar as etiquetas que pretendemos procurar nas musicas
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

    public void transferMusic(int uniqId,BufferedReader in, PrintWriter out){
        ServidorSndRcv ssr = new ServidorSndRcv("send",in,out,in2,out2,sound,uniqId,this.name);
        Thread t = new Thread(ssr);
        t.start();
    }

    public void receiveM(BufferedReader in,PrintWriter out){
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

          out.println("Please enter the path of the location of the file you wish to upload:");
          out.flush();
          String pathF = in.readLine();
          //String extension = FilenameUtils.getExtension(pathF);
          String extension = "";

          int i = pathF.lastIndexOf('.');
          if (i > 0) {
              extension = pathF.substring(i+1);
          }

          String filename = name+"."+extension;
          ServidorSndRcv ssr = new ServidorSndRcv("receive",in,out,in2,out2,sound,pathF,name,year,autor,etiquetas,filename);
          Thread t = new Thread(ssr);
          t.start();

      }catch(Exception e){}
    }

    //método para listar todas as músicas existentes no sistema
    public void listSongs(PrintWriter out){
        List<String> print = sound.list();
            Iterator<String> it = print.iterator();
            while(it.hasNext()){
                out.println(it.next());
                out.flush();
            }
    }

    //método para listar todas as músicas descarregadas pelo utilizador
    public void listOwnSongs(PrintWriter out){
        List<String> print = sound.listSelf(this.name);
        Iterator<String> it = print.iterator();
        while(it.hasNext()){
            out.println(it.next());
            out.flush();
        }
    }

    //método para procurar música por nome
    public void sBn(BufferedReader in, PrintWriter out){
        out.println("## Which name would you like to search for? ##");
        out.flush();
        String name="null";
        try{
          name = in.readLine();
        }catch(Exception e){}

        List<String> print = sound.prcNome(name);
        List<Integer> available = sound.prcNomeInt(name);
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
        if(available.contains(Integer.parseInt(input))){
            transferMusic(Integer.parseInt(input),in,out);
            out.println("## Song Downloaded ##");
            out.flush();
        }else{
            out.println("## Provided ID does not belong to any song on the list shown above ##");
            out.flush();
        }
    }

    //método para procurar música por etiquetas
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
        List<Integer> available = sound.prcEtiquetaInt(tag);
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

        if(available.contains(Integer.parseInt(input))){
            transferMusic(Integer.parseInt(input),in,out);
            out.println("## Song Downloaded ##");
            out.flush();
        }else{
            out.println("## Provided ID does not belong to any song on the list shown above ##");
            out.flush();
        }
    }

    //método para procurar música por identificador
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

    //método para procurar música pelo autor
    public void sBa(BufferedReader in, PrintWriter out){
        out.println("## Please insert the author name for whom you would like to search? ##");
        out.flush();
        String autor = "null";
        try{
            autor = in.readLine();
        }catch(Exception e){}

        List<String> print = sound.prcAutor(autor);
        List<Integer> available = sound.prcAutorInt(autor);
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

        if(available.contains(Integer.parseInt(input))){
            transferMusic(Integer.parseInt(input),in,out);
            out.println("## Song Downloaded ##");
            out.flush();
        }else{
            out.println("## Provided ID does not belong to any song on the list shown above ##");
            out.flush();
        }
    }

    //menu login
    public void menuLogin(PrintWriter out){
        out.println("#########################################");
        out.println("##            1-Login                  ##");
        out.println("##            2-Register               ##");
        out.println("##            0-Exit                   ##");
        out.println("#########################################");
        out.flush();
    }

    //menu para dar upload ou procurar músicas
    public void menu2(PrintWriter out){
        out.println("#########################################");
        out.println("##            1-Upload song            ##");
        out.println("##            2-Search song            ##");
        out.println("##            0-Logout                 ##");
        out.println("#########################################");
        out.flush();
    }

    //menu de procura
    public void menuProcura(PrintWriter out){
      out.println("##########################################");
      out.println("##   1-Search using Unique Identifier   ##");
      out.println("##       2-Search Songs by Tags         ##");
      out.println("##          3-Search by Author          ##");
      out.println("##           4-Search by Name           ##");
      out.println("##           5-List all Songs           ##");
      out.println("##     6-List own downloaded Songs      ##");
      out.println("##             7-Go Back                ##");
      out.println("##########################################");
      out.flush();
    }

    //método run
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            this.in2 = new BufferedReader(new InputStreamReader(comms.getInputStream()));

            PrintWriter out = new PrintWriter(cliente.getOutputStream());
            this.out2 = new PrintWriter(comms.getOutputStream());

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
                if((s.equals("7")) && k==2){ k=1;continue;}
                if(s.equals("1") && k==0){
                    login(in,out);
                    continue;
                }
                if(s.equals("2") && k==0){
                    register(in,out);
                    continue;
                }
                if(s.equals("1") && k==1){
                    receiveM(in,out);
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

                if(s.equals("5") && k==2){ // Lista as músicas do sistema
                  listSongs(out);
                  k=1;
                  continue;
                }

                if(s.equals("6") && k==2){
                  listOwnSongs(out);
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
                comms.shutdownOutput();
                comms.shutdownInput();
                comms.close();
            }
            catch(IOException e) { System.out.println(e);}
        }
    }
}
