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

@Service
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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

    public void save(Review review) {
        reviewRepository.save(review);
    }
}
