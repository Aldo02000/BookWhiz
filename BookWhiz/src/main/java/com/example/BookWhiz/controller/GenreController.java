package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/genres")
@RestController
public class GenreController {

    @Autowired
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/allGenres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();

        return genres.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(genreService.mapToDtoList(genres));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GenreDto>> searchGenres(@RequestParam String genreName) {
        List<Genre> genres = genreService.getGenresByPartOfName(genreName);

        return genres.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(genreService.mapToDtoList(genres));
    }

}
