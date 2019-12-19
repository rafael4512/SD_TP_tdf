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

    public Musica(){

        this.titulo = "";
        this.artista = "";
        this.ano = 0;
        this.etiquetas = new ArrayList<String>();
        this.id = -1;
        lock = new ReentrantLock();
    }

    public Musica(String tit,String art, int x,List<String> eti,int idUniq){

        this.titulo = tit;
        this.artista = art;
        this.ano = x;
        this.etiquetas = eti;
        this.id = idUniq;
        lock = new ReentrantLock();
    }

    public Musica(Musica m){

        this.titulo = m.getTitulo();
        this.artista = m.getArtista();
        this.ano = m.getAno();
        this.etiquetas = m.getEtiquetas();
        this.id = m.getId();
        lock = new ReentrantLock();
    }

    //Gets

    public String getTitulo(){

        return this.titulo;
    }

    public String getArtista(){

        return this.artista;
    }

    public int getAno(){

        return this.ano;
    }

    public List<String> getEtiquetas(){

        return this.etiquetas;
    }

    public int getId(){

        return this.id;
    }

    public int getDw(){
      return this.downloads;
    }

    //Sets

    public void setTitulo(String tit){

        this.titulo = tit;
    }

    public void setArtista(String art){

        this.artista = art;
    }

    public void setAno(int x){

        this.ano = x;
    }

    public void setEtiquetas(List<String> l){

        this.etiquetas = l;
    }

    public void setId(int x){

        this.id = x;
    }

    public Musica clone(){

        return new Musica(this);
    }

    public void increment(){
      lock.lock();
      this.downloads++;
      lock.unlock();
    }


}
