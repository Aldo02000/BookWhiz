package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.AuthorService;
import com.example.BookWhiz.service.BookService;
import com.example.BookWhiz.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/books")
@RestController
public class BookController {

    @Autowired
    private final BookService bookService;

    @Autowired
    private final AuthorService authorService;

    @Autowired
    private final GenreService genreService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/search/title")
    public ResponseEntity<Set<String>> searchBooksByTitle(@RequestParam String title) {
        Set<Book> books = bookService.getBooksByTitle(title);
        Set<String> titles = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(titles);
    }

    @GetMapping("/search/author")
    public ResponseEntity<Set<String>> searchBooksByAuthor(@RequestParam String author) {

        Set<Author> authors = authorService.getAuthorsByPartOfName(author);
        Set<Book> books = new HashSet<>();
        for (Author authorFound : authors) {
            books.addAll(bookService.getBooksByAuthor(authorFound));
        }

        Set<String> titles = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(titles);
    }

    @GetMapping("/search/genre")
    public ResponseEntity<List<BookDto>> searchBooksByGenre(@RequestParam String genre) {
        Genre genreFound = genreService.getGenre(genre);
        Set<Book> books = bookService.getBooksByGenre(genreFound);

        List<BookDto> bookDTOs = books.stream()
                .map(book -> new BookDto(
                        book.getTitle(),
                        book.getImageLink(),
                        book.getAuthors().stream()
                                .map(Author::getName) // Assuming `getName` returns the author's name
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookDTOs);
    }
}
