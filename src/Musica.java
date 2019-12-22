import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class Musica implements Serializable{
    private static final long serialVersionUID = 1L;
    private String titulo;
    private String artista;
    private int ano;
    private List<String> etiquetas;
    private int id;
    private int downloads;
    ReentrantLock lock;

    //construtor de uma musica por omissão
    public Musica(){

        this.titulo = "";
        this.artista = "";
        this.ano = 0;
        this.etiquetas = new ArrayList<String>();
        this.id = -1;
        lock = new ReentrantLock();
    }

    //construtor de uma música por atribuição
    public Musica(String tit,String art, int x,List<String> eti,int idUniq){

        this.titulo = tit;
        this.artista = art;
        this.ano = x;
        this.etiquetas = eti;
        this.id = idUniq;
        lock = new ReentrantLock();
    }

    //construtor cópia de uma música
    public Musica(Musica m){

        this.titulo = m.getTitulo();
        this.artista = m.getArtista();
        this.ano = m.getAno();
        this.etiquetas = m.getEtiquetas();
        this.id = m.getId();
        lock = new ReentrantLock();
    }

    //Gets

    //get do titulo
    public String getTitulo(){

        return this.titulo;
    }

    //get do nome do artista
    public String getArtista(){

        return this.artista;
    }

    //get do ano da música
    public int getAno(){

        return this.ano;
    }

    //get das etiquetas da música
    public List<String> getEtiquetas(){

        return this.etiquetas;
    }

    //get do identificador da música
    public int getId(){

        return this.id;
    }

    //get do número de downloads da música
    public int getDw(){
      return this.downloads;
    }

    //Sets

    //set do título da música
    public void setTitulo(String tit){

        this.titulo = tit;
    }

    //set do artista da música
    public void setArtista(String art){

        this.artista = art;
    }

    //set do ano da música
    public void setAno(int x){

        this.ano = x;
    }

    //set das etiquetas da música
    public void setEtiquetas(List<String> l){

        this.etiquetas = l;
    }

    //set do identificador da música
    public void setId(int x){

        this.id = x;
    }

    //clone de uma música
    public Musica clone(){

        return new Musica(this);
    }

    //método que incrementa o número de downloads
    public void increment(){
      this.downloads++;
    }


}
