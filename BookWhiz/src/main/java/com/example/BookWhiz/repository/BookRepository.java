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
    List<Book> findByAuthors(Author author);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByGenres(Genre genre);
}
