package com.example.BookWhiz.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class AuthorDto {
    private Integer id;
    private String name;
    private Date birthDate;
    private String biography;
    private Set<String> books;

    public AuthorDto(Integer id, String name, Date birthDate, String biography, Set<String> books) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.biography = biography;
        this.books = books;
    }

    public AuthorDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
