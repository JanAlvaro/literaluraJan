package com.literalurajan.literalurajan.repository;

import com.literalurajan.literalurajan.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainsIgnoreCase(String title);

    @Query("SELECT l FROM Libro l WHERE LOWER(l.idioma) = LOWER(:language) ORDER BY l.titulo ASC")
    List<Libro> findBookByLanguage(String language);

    //@Query("SELECT l FROM libro l ORDER BY numeroDeDescargas desc LIMIT 10")
    @Query("SELECT l.titulo FROM Libro l ORDER BY l.numeroDeDescargas desc LIMIT 10")
    List<String> findTop10Books();
    //List<double> getBooksDownloads();
}

