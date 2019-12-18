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

public class Cliente implements Runnable{

    //private BufferedReader sysin;
    private BufferedReader pw;
    private PrintWriter ot;
    private static int k = 0;

    public Cliente(BufferedReader pw,PrintWriter ot){
        this.pw = pw;
        this.ot = ot;
    }

    public void send(){
      try{
          String name = pw.readLine();
          String path = "from/"+name+".mp3";
          File music = new File(path);
          InputStream targetStream = new FileInputStream(music);
          byte[] buf = new byte[850000];

          ot.println("music data incoming");
          for (int readNum; (readNum = targetStream.read(buf)) != -1;){
              byte[] bytes = new byte[readNum];

              System.arraycopy(buf,0,bytes,0,readNum);

              String sende = Base64.getEncoder().encodeToString(bytes);
              ot.println(bytes.length);
              ot.println(sende);
          }
        ot.println("sending Finished");
      }catch(Exception e){}
    }

    public void run(){
        while(k == 0){
            try{
              String message = pw.readLine();
              switch(message){
                case "ready for receival." :
                    this.send();
                    break;

                default :
                    if(message !=null)
                        System.out.println(message);
                    else{
                        Thread.currentThread().interrupt();
                    }
              }
            }catch(Exception e){
                System.out.println("Exit");
                Thread.currentThread().interrupt();
            }
        }
    }


    public static void main(String[] args) throws Exception{
        //Socket conectado na porta 12345 e com o IP 127.0.0.1 (localhost)

        Socket socket = new Socket(InetAddress.getLocalHost(), 12345);

        // in le do input do socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // buffer vai ler do System.in
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        // ot escreve no otput do socket
        PrintWriter out = new PrintWriter((socket.getOutputStream()));

        System.out.println("Welcome to SoundSky!");

        Thread printer = new Thread(new Cliente(in,out));
        printer.start();

        while( true ){

            String s = buffer.readLine();       //Le o que foi escrito no System.in
            if(s==null || s.equals("0")){ // Se o cliente escreve Quit o cliente fecha
                k=1;
                break;
            }

            out.println(s);                     // Escreve no socket o que foi lido e envia para o servidor
            out.flush();                        // Limpa a stream de dados
            //System.System.out.println(in.readLine());  // Obtem a resposta do servidor e faz echo para o terminal
            //(bloqueia a espera da resposta)
        }
        socket.shutdownOutput();                // Fecha o lado de escrita do socket
        socket.shutdownInput();                 // Fecha o lado de leitura do socket
        socket.close();                         // Fecha o socket
    }

}
