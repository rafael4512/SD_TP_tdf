import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Servico implements Runnable {

    private Socket cliente;
    private SoundSky sound;

    public Servico(Socket cliente,SoundSky s) {

        this.cliente = cliente;
        this.sound = s;

    }

    private void login(BufferedReader in, PrintWriter out){
        
        String psw = null;
        String usr = null;
        try{
            while(usr==null){
                out.println("Username: ");
                out.flush();
                usr = in.readLine();
            }
            while(psw==null){
                out.println("Password:");
                out.flush();
                psw = in.readLine();
            }
            
            if(sound.checkUser(usr,psw.toCharArray())){
                out.println("Welcome");
                out.flush();
            }
            else{
                out.println("Wrong username or password");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void run(){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter out = new PrintWriter(cliente.getOutputStream());

            while (true) {

                String s = in.readLine();   /** Le o que foi escrito no socket do cliente **/
                if (s==null || s.equals("Quit"))       /** Se o cliente escreveu Quit fecha-se a conexao com o cliente **/
                    break;

                if (s.equals("Login")){
                    login(in,out);
                }

                out.println("");             /** Escreve no socket do cliente o que foi lido e envia para o cliente **/
                out.flush();                /** Limpa a stream de dados **/
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
