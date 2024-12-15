package com.example.BookWhiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GenreDto {
    private Integer id;
    private String name;
    private Set<String> books;

    public GenreDto(Integer id, String name, Set<String> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public GenreDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


}
