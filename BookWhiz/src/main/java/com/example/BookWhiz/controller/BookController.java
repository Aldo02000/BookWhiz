package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.AuthorService;
import com.example.BookWhiz.service.BookService;
import com.example.BookWhiz.service.GenreService;
import com.example.BookWhiz.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

    @Autowired
    private ReviewService reviewService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDto>> searchBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.getBooksByTitle(title);

        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookService.mapToDTOList(books));
    }

    @GetMapping("/search/author")
    public ResponseEntity<Set<String>> searchBooksByAuthorName(@RequestParam String author) {

        List<Author> authors = authorService.getAuthorsByPartOfName(author);
        Set<Book> books = new HashSet<>();
        for (Author authorFound : authors) {
            books.addAll(bookService.getBooksByAuthor(authorFound));
        }

        Set<String> titles = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(titles);
    }

    @GetMapping("/search/genre/{genre}")
    public ResponseEntity<List<BookDto>> searchBooksByGenre(@PathVariable String genre) {
        Genre genreFound = genreService.getGenre(genre);
        List<Book> books = bookService.getBooksByGenre(genreFound);

        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookService.mapToDTOList(books));
    }

    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<BookDto>> searchBooksByAuthorId(@PathVariable Integer authorId) {
        Author authorFound = authorService.getAuthorById(authorId);
        List<Book> books = bookService.getBooksByAuthor(authorFound);

        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookService.mapToDTOList(books));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);

        return ResponseEntity.ok(bookService.mapToDTO(book));
    }

    @GetMapping("/topRated")
    public List<BookDto> getTopRatedBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        Map<Book, Double> bookRatings = new HashMap<>();

        for (Book book: allBooks) {
            double bookRating = reviewService.getAverageRating(book.getId());
            bookRatings.put(book, bookRating);
        }

        return bookRatings.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue())) // Descending order
                .limit(10) // Take the top 10
                .map(entry -> {
                    Book book = entry.getKey();
                    double rating = entry.getValue();
                    return new BookDto(
                            book.getId(),
                            book.getTitle(),
                            book.getImageLink(),
                            book.getAuthors().stream()
                                    .map(author -> new AuthorDto(
                                            author.getId(),
                                            author.getName()
                                    ))
                                    .collect(Collectors.toSet()),
                            book.getGenres().stream()
                                    .map(genre -> new GenreDto(
                                            genre.getId(),
                                            genre.getName()
                                    ))
                                    .collect(Collectors.toSet()),
                            book.getIsbn10(),
                            book.getIsbn13(),
                            book.getPublishingDate(),
                            book.getPublishingHouse(),
                            book.getLanguage(),
                            book.getPageCount(),
                            book.getSummary(),
                            rating // Include the rating as well
                    );
                }) // Map to DTO
                .collect(Collectors.toList());
    }
}
