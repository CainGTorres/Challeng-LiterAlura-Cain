package com.alurachalleng.literalura.principal;
import com.alurachalleng.literalura.model.*;
import com.alurachalleng.literalura.repository.AutorRepository;
import com.alurachalleng.literalura.repository.LibrosRepository;
import com.alurachalleng.literalura.service.ConsumoAPI;
import com.alurachalleng.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibrosRepository repositorioLibros;
    private AutorRepository repositorioAutores;

    public Principal( LibrosRepository repositorioLibros) {
        this.repositorioLibros = repositorioLibros;
    }

    public Principal() {}

    public void muestraElmenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1- Buscar libros por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idiomas
                    6- Reporte estadístico
                    7- Top 10 libros más descargados
                    8- Buscar libros por autor
                    9- salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextInt();
        }

        switch (opcion){
            case 1:
                LibroWebBuscar();
                break;
            case 2:
                librosRegistrados();
                break;
            case 3:
                autoresRegistrados();
                break;
            case 4:
                autoresVivos();
                break;
            case 5:
                librosPorIdioma();
            case 6:
                reporteEstadistico();
        }

    }



    private Optional<DatosLibros> getDatosResultados (){
        System.out.println("Ecribe el titulo del libro que desea Buscar: ");
        var tituloL = teclado.nextLine();

        var json= consumoAPI.obtenerDatos(URL_BASE + "?search" + tituloL.replace("", "+"));

        Datos resultados = conversor.obtenerDatos(json, Datos.class);
        Optional <DatosLibros> libroBuscado = resultados.datosListaLibros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloL.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){

        } else{
            System.out.println("Está mal... en algo");
        }
        return libroBuscado;
    }

    private void LibroWebBuscar(){
        Optional<DatosLibros> datosDelLibrOptional = getDatosResultados();
        try {
            if (datosDelLibrOptional.isPresent()){
                DatosLibros datosLibros = datosDelLibrOptional.get();
                Libros libros = new Libros(datosLibros);
                if  (repositorioLibros.existTeTitulo(libros.getTitulo())){
                    try {
                        System.out.println("...........LIBRO..........");
                        System.out.println("Titulo: " + libros.getTitulo());
                        System.out.println("Numero de descargas: " + libros.getNumeroDeDescargas());
                    } catch (DataIntegrityViolationException e){
                        System.out.println("Error al guardar el libro" + e.getMessage());
                    }
                }else {
                    System.out.println("El libro: " + libros.getTitulo() + "Ya existe");
                }
                try {
                    List<Autores> autores = datosDelLibrOptional.stream()
                            .flatMap(d -> d.autorLista().stream()
                                    .map(a -> new Autores(a)))
                            .collect(Collectors.toList());
                    libros.setListaAutores(autores);
                    int i = 0;
                    System.out.println("Autor: " + autores.get(i).getNombre());
                }catch (DataIntegrityViolationException e){
                    System.out.println("El Autor Existe");
                }
            }
        }catch (DataIntegrityViolationException e) {
        }
    }

    private void librosRegistrados() {
        List <Libros> listaLibros = repositorioLibros.listadoDeLibros();
        System.out.println("Lista de libros: ");
        System.out.println(listaLibros);
    }
    private void autoresRegistrados() {
        List <Autores> listaAutores = repositorioLibros.listadoDeAutores();
    }
    private void autoresVivos() {
        System.out.println("Ingresar el año del autor: ");
        var cumple = teclado.nextLong();
        List <Autores> listaAutorVivo = repositorioLibros.listadoDeAutoresVivos();
        System.out.println(listaAutorVivo);
    }
    private void librosPorIdioma() {
        System.out.println("Ingrese el idioma que de desee buscar: ");
        System.out.println("""
                es- Español
                en- Ingles
                fr- Frances
                pt- Potugues
                """);
        var idioma = teclado.nextLine();
        List <Lenguaje> listaIdiomas = repositorioLibros.listaLibrosXIdioma(idioma);
        System.out.println(listaIdiomas);
    }
    private void reporteEstadistico() {
        List<Libros> listaDeLibros = repositorioLibros.estadisticaDeLibros();
        DoubleSummaryStatistics est = listaDeLibros.stream()
                .filter(t ->t.getNumeroDeDescargas()>9)
                .mapToDouble(t->t.getNumeroDeDescargas())
                .summaryStatistics();
        String libroMasDescargado = listaDeLibros.stream()
                .filter(t->t.getNumeroDeDescargas()== est.getMax())
                .map(Libros::getTitulo)
                .findFirst()
                .orElse("No encontrado");
        String libroMenosDescargado = listaDeLibros.stream()
                    .filter(t->t.getNumeroDeDescargas()== est.getMin())
                .map(Libros::getTitulo)
                .findFirst()
                .orElse("No encontrado");

        System.out.println("Estadisticas: ");
        System.out.println("");
        System.out.println("Media de las descargas: " + est.getAverage());
        System.out.println("El libro más leído es: " + libroMasDescargado + " fue descargado " + est.getMax() + " veces ");
        System.out.println("El libro menos leído es: " + libroMenosDescargado + " fue descargado " + est.getMin() + " veces ");
        System.out.println("Libros descargados en total: " + est.getSum());

    }

    private void topTenLibrosMasDescargados (){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        Datos result = conversor.obtenerDatos(json, Datos.class);

        List <DatosLibros> topTenLibrosDescargados = result.datosListaLibros().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("Top 10 de Libros con más descargas: ");
        topTenLibrosDescargados.forEach(d-> System.out.println("Libros " + d.titulo() + " Descargado " + d.numeroDeDescargas() + " veces"));

        List <DatosLibros> topTenMenosDescargados = result.datosListaLibros().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas))
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("Top 10 de Libros con menos descargas: ");
        topTenMenosDescargados.forEach(m-> System.out.println("Libros " + m.titulo() + " Descargado " + m.numeroDeDescargas() + " veces"));
    }

    private void buscarAutor (){
        System.out.println("Escribe el nombre del autor que desees bsucar: ");
        var nombreAutor = teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreAutor.replace("", "+"));
        Datos result = conversor.obtenerDatos(json, Datos.class);

        List<DatosLibros> autorBuscado = result.datosListaLibros().stream()
                .filter(l->l.titulo().toUpperCase().contains(nombreAutor.toUpperCase()))
                .collect(Collectors.toList());

        if (!autorBuscado.isEmpty()){
            autorBuscado.forEach(a-> System.out.println("libros " + a.titulo() + "Autor: " + a.autorLista() + "Idioma: " + a.idiomasLista() + "Numero de Descargas: " + a.numeroDeDescargas()));

        } else {
            System.out.println(" No exciste es autor ");
        }

    }


}
