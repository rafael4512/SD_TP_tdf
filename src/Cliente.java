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

    private BufferedReader pw;
    private PrintWriter ot;
    private BufferedReader inC;
    private PrintWriter outC;
    private static int k = 0;
    private static int sair = 0;

    //construtor de um cliente
    public Cliente(BufferedReader pw,PrintWriter ot,BufferedReader inC,PrintWriter outC){
        this.pw = pw;
        this.ot = ot;
        this.inC = inC;
        this.outC = outC;
    }
    //método run
    public void run(){
        while(k == 0){
            try{
              String message = pw.readLine();
              switch(message){
                case "Music information starting" :
                    ClienteSndRcv csr = new ClienteSndRcv("receive",pw,ot,inC,outC);
                    Thread rn = new Thread(csr);
                    rn.start();
                    break;

                case "ready for receival." :
                    ClienteSndRcv csre = new ClienteSndRcv("send",pw,ot,inC,outC);
                    Thread rne = new Thread(csre);
                    rne.start();
                    break;

                default :
                    if(message !=null)
                        System.out.println(message);
                    else{
                        Thread.currentThread().interrupt();
                    }
              }
            }catch(Exception e){
                this.sair=1;
                System.out.println("Exit");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    //main
    public static void main(String[] args) throws Exception{
        //Socket conectado na porta 12345 e com o IP 127.0.0.1 (localhost)

        Socket socket1 = new Socket(InetAddress.getLocalHost(), 12345);
        Socket socket2 = new Socket(InetAddress.getLocalHost(), 23456);

        // in le do input do socket
        BufferedReader in = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        BufferedReader inC = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        // buffer vai ler do System.in
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        // ot escreve no otput do socket
        PrintWriter out = new PrintWriter((socket1.getOutputStream()));
        PrintWriter outC = new PrintWriter((socket2.getOutputStream()));

        System.out.println("---------Welcome to SoundSky!---------");

        Thread printer = new Thread(new Cliente(in,out,inC,outC));
        printer.start();

        while( true ){

            String s = buffer.readLine();       //Le o que foi escrito no System.in
            if(sair==1 || s==null|| s.equals("0") ){ // Se o cliente escreve Quit o cliente fecha
                k=1;
                break;
            }

            out.println(s);                     // Escreve no socket o que foi lido e envia para o servidor
            out.flush();                        // Limpa a stream de dados
        }
        socket1.shutdownOutput();                // Fecha o lado de escrita do socket
        socket1.shutdownInput();                 // Fecha o lado de leitura do socket
        socket1.close();                         // Fecha o socket
        socket2.shutdownOutput();                // Fecha o lado de escrita do socket
        socket2.shutdownInput();                 // Fecha o lado de leitura do socket
        socket2.close();
    }

}
