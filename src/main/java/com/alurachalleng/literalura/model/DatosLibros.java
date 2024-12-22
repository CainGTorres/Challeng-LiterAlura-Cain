package com.alurachalleng.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autorLista,
        @JsonAlias("languages") List<String> idiomasLista,
        @JsonAlias("download_count") Double numeroDeDescargas
) {
}
