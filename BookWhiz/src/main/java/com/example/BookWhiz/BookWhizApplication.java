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

    @Autowired
    private ReviewInitializer reviewInitializer;


    public static void main(String[] args) {
        SpringApplication.run(BookWhizApplication.class, args);
    }

    /**
     * This method can be used for different functionalities out of the main flow of the application.
     * It can be effectively used to populate the database.
     */
    @Override
    public void run(String... args) throws Exception {
        // List<String> genres = databaseInitializerService.readGenreListFromFile();
        // databaseInitializerService.fetchBooksByGenre(genres);
    }
}
