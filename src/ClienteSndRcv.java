import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Base64;
import java.io.FileOutputStream;

public class ClienteSndRcv implements Runnable{

    private String operation;
    private BufferedReader pw;
    private PrintWriter ot;
    private BufferedReader pw2;
    private PrintWriter ot2;

    //construtor por atribuição
    public ClienteSndRcv(String op,BufferedReader i,PrintWriter o,BufferedReader i2,PrintWriter o2){
        operation = op;
        pw = i;
        ot = o;
        pw2 = i2;
        ot2 = o2;
    }

    //método receive para auxiliar o transferencia de ficheiro
    public void receive(){
        try{
            String uname = pw2.readLine();
            String fname = pw2.readLine();
            File music = new File("downloads/"+uname+"/"+fname);
            FileOutputStream stream = new FileOutputStream(music);
            while(true){
                String data = pw2.readLine();
                if(data.equals("Sending Finished"))
                  break;

                byte[] decodedString = Base64.getDecoder().decode(data.getBytes("UTF-8"));
                stream.write(decodedString);
                stream.flush();
            }
            stream.close();
        }catch(Exception e){}
    }

    //método send para auxiliar transferencia de ficheiro
    public void send(){
      try{
          String name = pw2.readLine();
          //String path = "from/"+name;
          String path = name;
          File music = new File(path);
          if(music.exists()){
              InputStream targetStream = new FileInputStream(music);
              byte[] buf = new byte[850000];

              ot2.println("music data incoming");
              ot2.flush();
              for (int readNum; (readNum = targetStream.read(buf)) != -1;){
                  byte[] bytes = new byte[readNum];

                  System.arraycopy(buf,0,bytes,0,readNum);

                  String sende = Base64.getEncoder().encodeToString(bytes);
                  ot2.println(sende);
                  ot2.flush();
              }
              ot2.println("sending Finished");
              ot2.flush();
          }else{
              ot2.println("name invalid");
              ot2.flush();
          }
      }catch(Exception e){}
    }

    //método run
    public void run(){
      if(operation.equals("receive")){
        this.receive();
      }
      else{
        this.send();
      }
    }
}
