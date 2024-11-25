package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public void saveGenre(Genre genre) {
        // Check if the author already exists in the database by name
        Optional<Genre> existingGenre = genreRepository.findByName(genre.getName());

        // Save only if the author does not already exist
        if (existingGenre.isEmpty()) {
            genreRepository.save(genre);
        }
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

}
