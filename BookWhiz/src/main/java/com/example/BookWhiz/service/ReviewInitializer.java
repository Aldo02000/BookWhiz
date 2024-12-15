package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Review;
import com.example.BookWhiz.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
public class ReviewInitializer {

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;
    @Autowired
    private ReviewService reviewService;

    public ReviewInitializer(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    public void createReviews() {
        User user = userService.getUserById(1L);

        for (Long i = 1L; i < 178; i++) {
            Book book = bookService.getBookById(i);
            Review review = new Review();
            review.setUser(user);
            review.setBook(book);
            review.setCreatedDate(new Date());
            review.setContent("A captivating and emotional journey, this book explores complex themes with " +
                    "depth and sensitivity. The characters are richly developed, making it easy to connect" +
                    "with their struggles and growth. Though occasionally slow, the story offers powerful " +
                    "moments and a satisfying conclusion. " +
                    "A must-read for fans of heartfelt fiction.");
            review.setRating((int)(Math.random() * 5) + 1);
            reviewService.save(review);
        }

    }
}
