package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseInitializerService {

    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    public DatabaseInitializerService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<String> readGenreListFromFile() {
        // Load the JSON file from resources folder
        File file = null;
        List<String> genreList = new ArrayList<>();
        try {
            file = ResourceUtils.getFile("classpath:Genres.json");
            genreList = objectMapper.readValue(file, List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return genreList;
    }

    public void fetchBooksByGenre(List<String> genreList) {

        for (String genreFromList : genreList) {
            iterateBooksByGenre(genreFromList);
        }
    }

    public void iterateBooksByGenre(String genreFromList) {
        for (int i = 0; i < 10; i++) {
            String url = String.format("%s?q=subject:%s&maxResults=40&langRestrict=en&startIndex=%d&key=%s", baseUrl, genreFromList, i * 40, apiKey);

            try {
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");

                if (items == null) {
                    return;
                }

                for (Map<String, Object> item : items) {

                    Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
                    boolean bookExists = saveBook(volumeInfo);
                    if (bookExists) {
                        continue;
                    }

                    System.out.println("\n\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    book.getAuthors().add(authorService.getAuthor(authorobj.toString()));
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
