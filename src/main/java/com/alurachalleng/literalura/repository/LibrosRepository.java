package com.alurachalleng.literalura.repository;
import com.alurachalleng.literalura.model.Autores;
import com.alurachalleng.literalura.model.Lenguaje;
import com.alurachalleng.literalura.model.Libros;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibrosRepository {
    @Query("SELECT COUNT b > 0 FROM Books b WHERE b.titulo = :titulo")
    boolean existTeTitulo(@Param("titulo") String titulo);

    @Query("SELECT b FROM Libros b JOIN b.listaAutores a  ORDER BY b.id DESC")
    List<Libros> listadoDeLibros();

    @Query("SELECT b FROM Autores b ORDER BY b.id DESC")
    List<Autores> listadoDeAutores();

    @Query("SELECT b FROM Autores b WHERE b.fechaDeNacimiento > :fecha AND b.fechaNacimiento < :fecha")
    List<Autores> listadoDeAutoresVivos();

    @Query("SELECT l FROM Lenguaje l WHERE l.idioma LIKE :idioma")
    List<Lenguaje> listaLibrosXIdioma(String idioma);

    @Query("SELECT b FROM Libros b ")
    List<Libros> estadisticaDeLibros();
}
