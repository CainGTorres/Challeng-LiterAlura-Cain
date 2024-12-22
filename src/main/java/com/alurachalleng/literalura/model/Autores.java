package com.alurachalleng.literalura.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "authors")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeaFallecimiento;

    @ManyToOne
    private Libros libros;
    public Libros getLibros(){
        return libros;
    }

    public void setLibros(Libros libros) {
        this.libros = libros;
    }
    public Autores(){}
    public Autores(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = Integer.valueOf(datosAutor.fechaDeNacimiento());
        this.fechaDeaFallecimiento = Integer.valueOf(datosAutor.fechaDeFallecimiento());
    }

    public Integer getFechaDeaFallecimiento() {
        return fechaDeaFallecimiento;
    }

    public void setFechaDeaFallecimiento(Integer fechaDeaFallecimiento) {
        this.fechaDeaFallecimiento = fechaDeaFallecimiento;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autores autores = (Autores) o;
        return Objects.equals(autores, nombre) && Objects.equals(nombre, autores.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "Autores" +
                "fechaDeaFallecimiento=" + fechaDeaFallecimiento +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaDeNacimiento=" + fechaDeNacimiento +
                ", libros=" + libros;
    }


}
