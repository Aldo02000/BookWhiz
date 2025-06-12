package com.example.BookWhiz.service;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorService authorService;

    @Autowired
    private final GenreService genreService;

    @Autowired
    private final RestTemplate restTemplate;

    @Value("${google.books.api.key}")
    private String googleApiKey;

    @Value("${google.books.api.url}")
    private String baseUrl;

    public BookService(BookRepository bookRepository, AuthorService authorService, GenreService genreService, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
        this.restTemplate = restTemplate;
    }

    public void saveBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(book.getIsbn13());

        // Save only if the book does not already exist
        if (existingBook.isEmpty()) {
            bookRepository.save(book);
        }
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public boolean existsBook(String isbn13) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(isbn13);
        return existingBook.isPresent();
    }

    public List<Book> getBooksByGenre(Genre genre) {
        return bookRepository.findByGenres(genre);
    }

    public List<Book> getBooksByAuthor(Author author) {
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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByTitleContaining(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public boolean saveBookData(Map<String, Object> volumeInfo) {

        Book book = new Book();

        List<Map<String, Object>> identifiers = volumeInfo.containsKey("industryIdentifiers") ? (List<Map<String, Object>>) volumeInfo.get("industryIdentifiers") : null;

        if (identifiers != null) {
            for (Map<String, Object> identifier : identifiers) {
                if ("ISBN_10".equals(identifier.get("type"))) {
                    book.setIsbn10(identifier.get("identifier").toString());
                } else if ("ISBN_13".equals(identifier.get("type"))) {
                    String isbn13 = identifier.get("identifier").toString();
                    if (existsBook(isbn13)) {
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

        saveBook(book);

        return false;
    }

    /**
     * This method retrieves the books from the google books api
     * It is used if a book suggested by the AI is not in the database.
     * It is not being used, but it can be changed to make use it in the getBooks() method
     */
    public void retrieveBooksFromGoogleApi(List<String> aiSuggestedTitles) {
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
                        boolean bookExists = saveBookData(volumeInfo);
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
    }

    public List<Book> getBooks(List<String> aiSuggestedTitles) {

        // retrieveBooksFromGoogleApi(aiSuggestedTitles);

        List<Book> foundBooks = new ArrayList<>();
        for (String title: aiSuggestedTitles) {
            foundBooks.addAll(getBooksByTitleContaining(title));
        }

        System.out.println(foundBooks);
        return foundBooks;
    }

    public List<BookDto> mapToDTOList(List<Book> books) {

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

        return bookDTOs;
    }

    public BookDto mapToDTO(Book book) {
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

        return bookDTO;
    }
}
