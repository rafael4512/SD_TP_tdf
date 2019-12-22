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

public class newSongUpdate implements Runnable{
  private SoundSky instance;
  private PrintWriter out;
  private int prevalue;

  //construtor de notificação de músicas
  public newSongUpdate(SoundSky s,PrintWriter ot){
      instance = s;
      out = ot;
      prevalue = s.getSongValue();
  }

  //método run
  public void run(){
      while(true){
        int val = this.instance.getSongValue();
        if(prevalue != val){
            String name = this.instance.getNewSongName();
            String autor = this.instance.getNewSongAuthor();
            prevalue = val;
            out.println("##");
            out.println("##     New music added! "+name+" by "+autor+"!     ##");
            out.println("##");
            out.flush();
        }
      }
  }
}
