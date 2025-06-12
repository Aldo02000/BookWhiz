package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByName(String name);
    List<Genre> findByNameContainingIgnoreCase(String partOfName);
}
