package com.example.BookWhiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookDto {
    private Long id;
    private String title;
    private String imageLink;
    private Set<AuthorDto> authors;
    private Set<GenreDto> genres;
    private String isbn10;
    private String isbn13;
    private String publishingDate;
    private String publishingHouse;
    private String language;
    private String pageCount;
    private String description;

    public BookDto(Long id, String title, String imageLink, Set<AuthorDto> authors, Set<GenreDto> genres,
                   String isbn10, String isbn13, String publishingDate, String publishingHouse,
                   String language, String pageCount, String description) {
        this.id = id;
        this.title = title;
        this.imageLink = imageLink;
        this.authors = authors;
        this.genres = genres;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.publishingDate = publishingDate;
        this.publishingHouse = publishingHouse;
        this.language = language;
        this.pageCount = pageCount;
        this.description = description;
    }
}
