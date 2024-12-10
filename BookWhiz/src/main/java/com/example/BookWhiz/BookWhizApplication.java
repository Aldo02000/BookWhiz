package com.example.BookWhiz;

import com.example.BookWhiz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookWhizApplication implements CommandLineRunner {

    @Autowired
    private DatabaseInitializerService databaseInitializerService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserBookListService userBookListService;

    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(BookWhizApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // List<String> genres = databaseInitializerService.readGenreListFromFile();
        // databaseInitializerService.fetchBooksByGenre(genres);
    }
}
