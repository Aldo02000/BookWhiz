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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    private RestTemplate restTemplate;


    @Value("${google.books.api.key}")
    private String googleApiKey;

    @Value("${google.books.api.url}")
    private String baseUrl;

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
    public ResponseEntity<Set<String>> searchBooksByAuthorName(@RequestParam String author) {

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

    @GetMapping("/search/genre/{genre}")
    public ResponseEntity<List<BookDto>> searchBooksByGenre(@PathVariable String genre) {
        Genre genreFound = genreService.getGenre(genre);
        Set<Book> books = bookService.getBooksByGenre(genreFound);

        List<BookDto> bookDTOs = books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getImageLink(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(
                                        author.getId(),
                                        author.getName()
                                )) // Assuming `getName` returns the author's name
                                .collect(Collectors.toSet()),
                        book.getGenres().stream()
                                .map(genreDto -> new GenreDto(
                                        genreDto.getId(),
                                        genreDto.getName()
                                ))
                                .collect(Collectors.toSet()),
                        book.getIsbn10(),
                        book.getIsbn13(),
                        book.getPublishingDate(),
                        book.getPublishingHouse(),
                        book.getLanguage(),
                        book.getPageCount(),
                        book.getSummary()
                ))
                .collect(Collectors.toList());

        return bookDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<BookDto>> searchBooksByAuthorId(@PathVariable Integer authorId) {
        Author authorFound = authorService.getAuthorById(authorId);
        Set<Book> books = bookService.getBooksByAuthor(authorFound);

        List<BookDto> bookDTOs = books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getImageLink(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(
                                        author.getId(),
                                        author.getName()
                                )) // Assuming `getName` returns the author's name
                                .collect(Collectors.toSet()),
                        book.getGenres().stream()
                                .map(genreDto -> new GenreDto(
                                        genreDto.getId(),
                                        genreDto.getName()
                                ))
                                .collect(Collectors.toSet()),
                        book.getIsbn10(),
                        book.getIsbn13(),
                        book.getPublishingDate(),
                        book.getPublishingHouse(),
                        book.getLanguage(),
                        book.getPageCount(),
                        book.getSummary()
                ))
                .collect(Collectors.toList());

        return bookDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        BookDto bookDTO = new BookDto(book.getId(), book.getTitle(), book.getImageLink(),
                book.getAuthors().stream()
                        .map(author -> new AuthorDto(
                                author.getId(),
                                author.getName()
                        ))
                        .collect(Collectors.toSet()),
                book.getGenres().stream()
                        .map(genreDto -> new GenreDto(
                                genreDto.getId(),
                                genreDto.getName()
                        ))
                        .collect(Collectors.toSet()),
                book.getIsbn10(), book.getIsbn13(), book.getPublishingDate(),
                book.getPublishingHouse(), book.getLanguage(), book.getPageCount(), book.getSummary());

        return ResponseEntity.ok(bookDTO);
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

    public List<Book> getBooks(List<String> aiSuggestedTitles) {

        for (String title : aiSuggestedTitles) {
            try {
                String googleBooksApiUrl = "https://www.googleapis.com/books/v1/volumes?q=intitle:" +
                        URLEncoder.encode(title, StandardCharsets.UTF_8) + "&maxResults=1&langRestrict=en" +"&key=" + googleApiKey;

                ResponseEntity<Map> response = restTemplate.exchange(googleBooksApiUrl, HttpMethod.GET, null, Map.class);
                Map<String, Object> responseBody = response.getBody();

                if (responseBody != null && responseBody.containsKey("items")) {
                    List<Map> items = (List<Map>) responseBody.get("items");

                    for (Map<String, Object> item : items) {

                        Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
                        boolean bookExists = saveBook(volumeInfo);
                        if (bookExists) {
                            continue;
                        }

                        System.out.println("\n\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions (e.g., log them)
            }
        }

        List<Book> foundBooks = new ArrayList<>();
        for (String title: aiSuggestedTitles) {
            foundBooks.addAll(bookService.getBooksByTitleContaining(title));
        }

        System.out.println(foundBooks);
        return foundBooks;
    }

    public boolean saveBook(Map<String, Object> volumeInfo) {

        Book book = new Book();

        List<Map<String, Object>> identifiers = volumeInfo.containsKey("industryIdentifiers") ? (List<Map<String, Object>>) volumeInfo.get("industryIdentifiers") : null;

        if (identifiers != null) {
            for (Map<String, Object> identifier : identifiers) {
                if ("ISBN_10".equals(identifier.get("type"))) {
                    book.setIsbn10(identifier.get("identifier").toString());
                } else if ("ISBN_13".equals(identifier.get("type"))) {
                    String isbn13 = identifier.get("identifier").toString();
                    if (bookService.existsBook(isbn13)) {
                        return true;
                    }
                    book.setIsbn13(identifier.get("identifier").toString());
                }
            }
        }

        HashMap<String, String> images = volumeInfo.containsKey("imageLinks") ? (HashMap<String, String>) volumeInfo.get("imageLinks"): null;
        if (images != null) {
            String imageLink = images.containsKey("thumbnail") ? images.get("thumbnail") : null;
            book.setImageLink(imageLink);
        }

        book.setTitle(volumeInfo.get("title").toString());
        book.setPublishingDate(volumeInfo.containsKey("publishedDate") ? volumeInfo.get("publishedDate").toString() : null);
        book.setPublishingHouse(volumeInfo.containsKey("publisher") ? volumeInfo.get("publisher").toString() : null);
        book.setSummary(volumeInfo.containsKey("description") ? volumeInfo.get("description").toString() : null);
        book.setPageCount(volumeInfo.containsKey("pageCount") ? volumeInfo.get("pageCount").toString() : null);
        book.setLanguage(volumeInfo.containsKey("language") ? volumeInfo.get("language").toString() : null);


        ArrayList authors = volumeInfo.containsKey("authors") ? (ArrayList) volumeInfo.get("authors") : null;
        if (authors != null) {
            for (Object authorobj : authors) {
                if (authorService.existsAuthor(authorobj.toString())) {
                    book.getAuthors().add(authorService.getAuthorbyName(authorobj.toString()));
                } else {
                    Author author = new Author();
                    author.setName(authorobj.toString());
                    authorService.saveAuthor(author);
                    book.getAuthors().add(author);
                }
            }
        }


        ArrayList categories = volumeInfo.containsKey("categories") ? (ArrayList) volumeInfo.get("categories") : null;
        if (categories != null) {
            for (Object category : categories) {
                if (genreService.existGenre(category.toString())) {
                    book.getGenres().add(genreService.getGenre(category.toString()));
                } else {
                    Genre genre = new Genre();
                    genre.setName(category.toString());
                    genreService.saveGenre(genre);
                    book.getGenres().add(genre);
                }
            }
        }

        bookService.saveBook(book);

        return false;
    }
}
