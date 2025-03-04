package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Review;
import com.example.BookWhiz.model.User;
import com.example.BookWhiz.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.orElse(null);
    }

    public Review getReviewByUserIdAndBookId(Long userId, Long bookId) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);
        Long reviewId = reviewRepository.findReviewIdByUserAndBook(user, book);
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.orElse(null);
    }

    public Set<Review> getReviewsByBookId(Long bookId) {
        Book book = bookService.getBookById(bookId);
        Optional<Set<Review>> reviews = reviewRepository.findReviewsByBook(book);
        return reviews.orElse(null);
    }

    public double getAverageRating(Long bookId) {
        Set<Review> reviews = getReviewsByBookId(bookId);
        double averageRating = 0;
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
            averageRating = (double) totalRating / reviews.size();
        }
        return averageRating;
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }
}
