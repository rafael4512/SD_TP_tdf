import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{

    //main do servidor
    public static void main(String[] args) throws Exception{
        SoundSky b = new SoundSky();

        ServerSocket socket1 = new ServerSocket(12345);
        ServerSocket socket2 = new ServerSocket(23456);
        while (true) {

            // Fica à escuta de conecoes a serem feitas e aceita-as
            Socket cliente = socket1.accept();
            Socket comms = socket2.accept();
            // É criada uma thread para atender cada pedido de um cliente **/

            Thread serv = new Thread(new Servico(cliente,comms,b));
            serv.start();
        }
    }
}
