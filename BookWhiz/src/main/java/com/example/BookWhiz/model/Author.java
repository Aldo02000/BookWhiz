package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    @ManyToMany(mappedBy = "authors")
    private Set<UserAuthorList> listsWhereAuthorBelongs = new HashSet<>();

    private String name;

    private Date birthDate;

    private String biography;

    public Author() {}

    public Author(String name, Date birthDate, String biography) {
        this.name = name;
        this.birthDate = birthDate;
        this.biography = biography;
    }

    public Author(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id +", name='" + name + '\'' +'}';
    }

}
