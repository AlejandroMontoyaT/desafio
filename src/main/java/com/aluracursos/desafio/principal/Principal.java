package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    //Metodo que muestra el menu para optener los datos de los libros
    public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json,Datos.class);
        System.out.println(datos);

        //Top 10 libros más descargados
        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Busqueda de libros por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))//Busqueda por nombre
                .findFirst(); //Obtiene el primer libro que cumpla con la condicion
        if(libroBuscado.isPresent()){ //Si el libro fue encontrado
            System.out.println("Libro Encontrado "); //Imprime el libro encontrado
            System.out.println(libroBuscado.get());// Imprime el libro encontrado
        }else {//Si el libro no fue encontrado
            System.out.println("Libro no encontrado");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream() //Obtiene las estadisticas de los libros
                .filter(d -> d.numeroDeDescargas() >0 ) //Filtra los libros que tengan descargas
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas)); //Obtiene las estadisticas de los libros
        System.out.println("Cantidad media de descargas: " + est.getAverage()); //Imprime la cantidad media de descargas
        System.out.println("Cantidad máxima de descargas: "+ est.getMax()); //Imprime la cantidad máxima de descargas
        System.out.println("Cantidad mínima de descargas: " + est.getMin()); //Imprime la cantidad mínima de descargas
        System.out.println(" Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount()); //Imprime la cantidad de registros evaluados para calcular las estadisticas

    }
}
