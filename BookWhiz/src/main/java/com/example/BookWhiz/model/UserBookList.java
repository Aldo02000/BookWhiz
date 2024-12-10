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
@Table(name = "user_book_list")
public class UserBookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_book_list_books",
            joinColumns = @JoinColumn(name = "user_book_list_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books =  new HashSet<>();

    @Column(nullable = false)
    private BookListType typeOfList;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
