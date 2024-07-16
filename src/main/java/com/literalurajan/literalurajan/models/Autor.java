package com.literalurajan.literalurajan.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "autor")
public class Autor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true)
        private String nombre;
        private Integer AnioNacimiento;
        private Integer AnioMuerte;
        @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Libro> libros = new ArrayList<>();

        public Autor() {
        }

        public Autor(String nombre, Integer anioNacimiento, Integer anioMuerte) {
            this.nombre = nombre;
            AnioNacimiento = anioNacimiento;
            AnioMuerte = anioMuerte;
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

        public Integer getAnioNacimiento() {
            return AnioNacimiento;
        }

        public void setAnioNacimiento(Integer anioNacimiento) {
            AnioNacimiento = anioNacimiento;
        }

        public Integer getAnioMuerte() {
            return AnioMuerte;
        }

        public void setAnioMuerte(Integer anioMuerte) {
            AnioMuerte = anioMuerte;
        }

        public List<Libro> getLibros() {
            return libros;
        }

        public void setLibros(Libro libro) {
            this.libros.add(libro);
        }



}
