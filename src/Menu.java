import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Menu {
    BufferedReader in;
    PrintWriter out;

    public Menu(BufferedReader in2 ,PrintWriter out2){
        this.in=in2;
        this.out=out2;
    }
    public void mInitMenu(){

        this.out.println("1-Registar Cliente");
        this.out.println("2-Login");
        this.out.println("3-Sair");
        out.flush();

    }

    private void mLogin1(){

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

        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void mLoggedClient2(){
        System.out.println("1-Publicar uma música");
        System.out.println("2-Procurar uma música");
        System.out.println("3-Descarregar uma música");
        System.out.println("4-Sair");
    }

    public void mPublicMusic3(){
        System.out.println("");
    }
    /*public void mCarrosDisp(int id) throws IOException{
        Scanner e = new Scanner(System.in);
        boolean ola=true;
        int i;
        List<Carro> aux=this.d.veiculosDispo();
        int N=this.d.veiculosDispo().size();
        while(ola==true){
            System.out.println("\nPara voltar ao menu anterior insira 0.\n");
            for(i=1;i<=N;i++)System.out.println(i+"-\t"+aux.get(i-1).toString());
            System.out.println("\n\tInsira o nº do veiculo que pretende:");
            String li=e.nextLine();
            if(li.equals("0"))
                this.mParado(id);
            else if(Integer.parseInt(li)-1<=N && Integer.parseInt(li)-1>=0){
                Point fim2=mPonto();
                this.d.AlugarCarro(id,aux.get(Integer.parseInt(li)-1),fim2);
                System.out.println("Pedido com sucesso.\n");
                ola=false;
            }
            else System.out.println("Opção invalida");
        }
        mParado(id);

    }*/
}
