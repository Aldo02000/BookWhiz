package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(book.getIsbn13());

        // Save only if the book does not already exist
        if (existingBook.isEmpty()) {
            bookRepository.save(book);
        }
    }

    public Set<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public boolean existsBook(String isbn13) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(isbn13);
        return existingBook.isPresent();
    }

    public Set<Book> getBooksByGenre(Genre genre) {
        return bookRepository.findByGenres(genre);
    }

    public Set<Book> getBooksByAuthor(Author author) {
        return bookRepository.findByAuthors(author);
    }

    public Book getBookByIsbn13(String isbn13) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(isbn13);
        return existingBook.orElse(null);
    }

    public Book getBookById(Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);
        return existingBook.orElse(null);
    }


}
