package com.example.BookWhiz.dto;

import com.example.BookWhiz.model.Author;
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
    private Set<String> genres;
    private String isbn10;
    private String isbn13;
    private String publishingDate;
    private String publishingHouse;
    private String language;
    private String pageCount;

    public BookDto(Long id, String title, String imageLink, Set<AuthorDto> authors, Set<String> genres,
                   String isbn10, String isbn13, String publishingDate, String publishingHouse,
                   String language, String pageCount) {
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
    }
}
