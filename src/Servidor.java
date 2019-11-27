import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    
    public static void main(String[] args) throws Exception{
        SoundSky b = new SoundSky();

        ServerSocket socket = new ServerSocket(12345);
        while (true) {
            
            // Fica à escuta de conecoes a serem feitas e aceita-as
            Socket cliente = socket.accept();
            // É criada uma thread para atender cada pedido de um cliente **/
            
            Thread serv = new Thread(new Servico(cliente,b));
            serv.start();
        }
    }
}
