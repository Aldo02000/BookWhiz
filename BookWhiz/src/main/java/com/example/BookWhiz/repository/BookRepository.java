package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn13(String isbn13);
    Set<Book> findByAuthors(Author author);
    Set<Book> findByTitleContainingIgnoreCase(String title);
    Set<Book> findByGenres(Genre genre);
}
