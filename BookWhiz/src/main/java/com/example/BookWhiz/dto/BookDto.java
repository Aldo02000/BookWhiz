package com.example.BookWhiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookDto {
    private String title;
    private String imageLink;
    private List<String> authors;

    public BookDto(String title, String imageLink, List<String> authors) {
        this.title = title;
        this.imageLink = imageLink;
        this.authors = authors;
    }
}
