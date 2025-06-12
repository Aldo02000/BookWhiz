package com.example.BookWhiz.service;

import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    @Autowired
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void saveGenre(Genre genre) {
        // Check if the author already exists in the database by name
        Optional<Genre> existingGenre = genreRepository.findByName(genre.getName());

        // Save only if the author does not already exist
        if (existingGenre.isEmpty()) {
            genreRepository.save(genre);
        }
    }

    public List<Genre> getGenresByPartOfName(String partOfName) {
        List<Genre> existingGenres = genreRepository.findByNameContainingIgnoreCase(partOfName);
        return existingGenres;
    }

    public boolean existGenre(String name) {
        return genreRepository.findByName(name).isPresent();
    }

    public Genre getGenre(String name) {
        Optional<Genre> existingGenre = genreRepository.findByName(name);
        if (existingGenre.isPresent()) {
            return existingGenre.get();
        }
        return null;
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres;
    }

    public Genre getGenreById(Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }

    public List<GenreDto> mapToDtoList(List<Genre> genres) {
      return genres.stream()
                .map(genre -> new GenreDto(
                        genre.getId(),
                        genre.getName(),
                        genre.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.toSet())
                ))
                .sorted(Comparator.comparing(GenreDto::getName))
                .collect(Collectors.toList());
    }

}
