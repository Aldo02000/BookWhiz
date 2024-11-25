package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();

    @ManyToMany(mappedBy = "genres")
    private Set<UserGenreList> listsWhereGenreBelongs = new HashSet<>();

    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public Genre() {}

    @Override
    public String toString() {
        return "Genre{" + "id=" + id +", name='" + name + '\'' +'}';
    }
}
