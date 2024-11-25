package com.example.BookWhiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    private Date createdDate;

    private int rating;

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    private Book book;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Review(String content, Date createdDate, int rating) {
        this.content = content;
        this.createdDate = createdDate;
        this.rating = rating;
    }

    public Review() {}

    public String getReviewInfo() {
        return String.format(
                "Book: %s\nUser: %s\nContent: %s\nDate Reviewed: %s\nRating (1-5): %d",
                book.getTitle(),
                user.getUsername(),
                content,
                createdDate.toString(),
                rating
        );
    }
}
