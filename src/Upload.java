import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Base64;
import java.io.FileOutputStream;


public class Upload {

  public Upload(){}

  public void send(BufferedReader in,PrintWriter out){
    try{
        out.println("Song name:");
        out.flush();
        String name = in.readLine();

        out.println("Song year:");
        out.flush();
        String year = in.readLine();

        out.println("Author:");
        out.flush();
        String autor = in.readLine();

        //Etiquetas coiso

        //Etiquetas coisa

        String path = "from/"+name+".mp3";
        File music = new File(path);
        InputStream targetStream = new FileInputStream(music);
        byte[] buf = new byte[850000];

        out.println("music data incoming");
        out.flush();

        out.println(name);
        out.flush();
        out.println(year);
        out.flush();
        out.println(autor);
        out.flush();

        for (int readNum; (readNum = targetStream.read(buf)) != -1;){
            byte[] bytes = new byte[readNum];

            System.arraycopy(buf,0,bytes,0,readNum);

            String sende = Base64.getEncoder().encodeToString(bytes);
            out.println(bytes.length);
            out.flush();
            out.println(sende);
            out.flush();
        }
      out.println("sending Finished");
      out.flush();
    }catch(Exception e){}
  }
}
