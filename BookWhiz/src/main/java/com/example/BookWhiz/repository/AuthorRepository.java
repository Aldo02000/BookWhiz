package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByName(String name);
    List<Author> findByNameContainingIgnoreCase(String partOfName);
    Optional<Author> findById(Integer Id);
}
