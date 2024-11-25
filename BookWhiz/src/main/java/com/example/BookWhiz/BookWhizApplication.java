package com.example.BookWhiz;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.AuthorService;
import com.example.BookWhiz.service.BookService;
import com.example.BookWhiz.service.DatabaseInitializerService;
import com.example.BookWhiz.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

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


    public static void main(String[] args) {
        SpringApplication.run(BookWhizApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Set<Author> authors = authorService.getAuthorsByPartOfName("Jane");

        Set<Book> books = bookService.getBooksByTitle("Harry Potter");

        for (Book book : books) {
            System.out.println(book.getBookInfo());
            System.out.println();
        }
    }
}
