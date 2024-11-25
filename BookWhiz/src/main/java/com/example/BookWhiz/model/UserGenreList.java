package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "user_genre_list")
public class UserGenreList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_genre_list_genres",
            joinColumns = @JoinColumn(name = "user_genre_list_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Genre> genres =  new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
