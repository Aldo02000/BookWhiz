package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.ReviewDto;
import com.example.BookWhiz.model.Review;
import com.example.BookWhiz.service.BookService;
import com.example.BookWhiz.service.ReviewService;
import com.example.BookWhiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/reviews")
@RestController
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;

    public ReviewController(ReviewService reviewService, UserService userService, BookService bookService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/review/user/{userId}/book/{bookId}")
    public ResponseEntity<ReviewDto> getReviewsByUser(@PathVariable Long userId, @PathVariable Long bookId) {
        Review review = reviewService.getReviewByUserIdAndBookId(userId, bookId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Date-only format
        String formattedDate = formatter.format(review.getCreatedDate());

        ReviewDto reviewDto = new ReviewDto(review.getId(), review.getContent(), formattedDate, review.getRating());
        return ResponseEntity.ok(reviewDto);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Set<ReviewDto>> getReviewsByBook(@PathVariable Long bookId) {
        Set<Review> reviews = reviewService.getReviewsByBookId(bookId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Set<ReviewDto> reviewDtos = reviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getContent(),
                        formatter.format(review.getCreatedDate()),
                        review.getRating()
                ))
                .collect(Collectors.toSet());

        return reviewDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviewDtos);
    }
}
