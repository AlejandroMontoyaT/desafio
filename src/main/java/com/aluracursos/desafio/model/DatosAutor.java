package com.aluracursos.desafio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
@JsonIgnoreProperties(ignoreUnknown = true) //para ignorar propiedades que no estan en el json
public record DatosAutor( //record es una clase inmutable
        @JsonAlias("name") String nombre, //JsonAlias para que coincida con el nombre del json
        @JsonAlias("birth_year") String fechaDeNacimiento //JsonAlias para que coincida con el nombre del json

) {
}
