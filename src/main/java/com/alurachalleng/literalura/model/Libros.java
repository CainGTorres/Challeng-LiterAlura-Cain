package com.alurachalleng.literalura.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Double numeroDeDescargas;

    @OneToMany(mappedBy = "books",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Autores> listaAutores;

    public List<Autores> getListaAutores(){
        return listaAutores;
    }

    public void setListaAutores(List<Autores> listaAutores) {
       listaAutores.forEach(a -> a.setLibros(this) );
       this.listaAutores=listaAutores;
    }
    @OneToMany(mappedBy = "books",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List <Lenguaje> listaLenguaje;

    public List<Lenguaje> getListaLenguaje() {
        return listaLenguaje;
    }

    public void setListaLenguaje(List<Lenguaje> listaLenguaje) {
        listaLenguaje.forEach(l -> l.setLibros(this) );
        this.listaLenguaje = listaLenguaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Libros(){}
    public Libros(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return "Libros" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", numeroDeDescargas=" + numeroDeDescargas +
                ", listaAutores=" + listaAutores +
                ", listaLenguaje=" + listaLenguaje;
    }
}
