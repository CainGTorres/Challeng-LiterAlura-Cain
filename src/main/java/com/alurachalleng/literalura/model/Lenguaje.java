package com.alurachalleng.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "languages")
public class Lenguaje {

    private Long id;
    private String idioma;
    private String descripcion;

    public Lenguaje (){}

    @OneToMany
    private Libros libros;

    public Lenguaje(String a) {
    }

    public Libros getLibros(){
        return libros;
    }

    public void setLibros(Libros libros) {
        this.libros = libros;
    }
    public Lenguaje(String descripcion, Long id, String idioma, Libros libros) {
        this.descripcion = descripcion;
        this.id = id;
        this.idioma = idioma;
        this.libros = libros;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return "Lenguaje" +
                "descripcion='" + descripcion + '\'' +
                ", id=" + id +
                ", idioma='" + idioma + '\'' +
                ", libros=" + libros;
    }
}
