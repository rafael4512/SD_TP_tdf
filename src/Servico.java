import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;

public class Servico implements Runnable {

    private Socket cliente;
    private SoundSky sound;
    private int k = 0;
    private String name = null;

    public Servico(Socket cliente,SoundSky s) {

        this.cliente = cliente;
        this.sound = s;

    }

    private void register(BufferedReader in, PrintWriter out){

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

            if(sound.addUser(usr,psw)){
                out.println("User registered");
                out.flush();
                File newDir = new File("downloads/" + usr);
                newDir.mkdir();
            }
            else{
                out.println("Username is already in use");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
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
                name = usr;
                out.println("Welcome");
                out.flush();
                k=1;
            }
            else{
                out.println("Wrong username or password");
                out.flush();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void menuLogin(PrintWriter out){
        out.println("1-Login");
        out.println("2-Register");
        out.println("0-Exit");
        out.flush();
    }

    public void menu2(PrintWriter out){
        out.println("1-Upload music");
        out.flush();
    }
    public void run(){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter out = new PrintWriter(cliente.getOutputStream());
            while (true) {
                if(k==0){
                    menuLogin(out);
                }
                if(k==1){
                    menu2(out);
                }

                String s = in.readLine();   /** Le o que foi escrito no socket do cliente **/
                if (s==null || s.equals("0")){
                    sound.cS(name);/** Se o cliente escreveu Quit fecha-se a conexao com o cliente **/
                    break;
                }

                if(s.equals("1") && k==0){
                    login(in,out);
                }
                if(s.equals("2") && k==0){
                    register(in,out);
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
