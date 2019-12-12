import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente implements Runnable{

  private BufferedReader pw;

  public Cliente(BufferedReader pw){
      this.pw = pw;
  }

    public void run(){
      while(true){
        System.out.println(message);
      }
  }

    public static void main(String[] args) throws Exception{
        //Socket conectado na porta 12345 e com o IP 127.0.0.1 (localhost)

        Socket socket = new Socket(InetAddress.getLocalHost(), 12345);

        // in le do input do socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // buffer vai ler do System.in
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        // out escreve no output do socket
        PrintWriter out = new PrintWriter((socket.getOutputStream()));

        System.out.println("Welcome to SoundSky!");
        System.out.println("For information about the different commands SoundSky supports, please type 'help'.");
        System.out.println("What would like to do?");

        Thread printer = new Thread(new Cliente(in));
        printer.start();

        while( true ){

            String s = buffer.readLine();       //Le o que foi escrito no System.in
            if(s==null || s.equals("Quit") )              // Se o cliente escreve Quit o cliente fecha
                break;
            out.println(s);                     // Escreve no socket o que foi lido e envia para o servidor
            out.flush();                        // Limpa a stream de dados
            //System.out.println(in.readLine());  // Obtem a resposta do servidor e faz echo para o terminal
            //(bloqueia a espera da resposta)
        }
        socket.shutdownOutput();                // Fecha o lado de escrita do socket
        socket.shutdownInput();                 // Fecha o lado de leitura do socket
        socket.close();                         // Fecha o socket
    }

}
