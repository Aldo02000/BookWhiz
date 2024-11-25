package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Setter
@Getter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres =  new HashSet<>();

    @ManyToMany(mappedBy = "books")
    private Set<UserBookList> listsWhereBookBelongs = new HashSet<>();

    @OneToMany(mappedBy="book")
    private Set<Review> reviews = new HashSet<>();

    private String isbn10;

    private String isbn13;

    private String title;

    private String publishingDate;

    private String publishingHouse;

    private String language;

    private String pageCount;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    public Book() {}

    public Book(String isbn10, String isbn13, String title, String publishingDate, String publishingHouse, String summary) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.title = title;
        this.publishingDate = publishingDate;
        this.publishingHouse = publishingHouse;
        this.summary = summary;
    }

    public Book(String title, String publishingYear) {
        this.title = title;
        this.publishingDate = publishingDate;
    }

    public String getBookInfo() {

        String bookAuthors = authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));

        String bookGenres = genres.stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));

        return String.format(
                "Title: %s\nISBN13: %s\nPublishing Year: %s\nAuthors: %s\nGenres: %s",
                title,
                isbn13,
                publishingDate,
                bookAuthors,
                bookGenres
        );
    }

}
