package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Book book;
    private Review review;

    @BeforeEach
    void setUp() {

        // Set up sample user, book, and review for testing
        user = new User();
        user.setId(1L);

        book = new Book();
        book.setId(1L);

        review = new Review();
        review.setId(1L);
        review.setRating(4);
        review.setUser(user);
        review.setBook(book);
    }

    @Test
    void getAllReviews_Success() {
        // Arrange
        List<Review> reviews = List.of(review);
        when(reviewRepository.findAll()).thenReturn(reviews);

        // Act
        List<Review> result = reviewService.getAllReviews();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(review, result.get(0));
    }

    @Test
    void getReviewById_Success() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // Act
        Review result = reviewService.getReviewById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(review, result);
    }

    @Test
    void getReviewById_NotFound() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Review result = reviewService.getReviewById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void getReviewByUserIdAndBookId_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewIdByUserAndBook(user, book)).thenReturn(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // Act
        Review result = reviewService.getReviewByUserIdAndBookId(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(review, result);
    }

    @Test
    void getReviewByUserIdAndBookId_NotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewIdByUserAndBook(user, book)).thenReturn(1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Review result = reviewService.getReviewByUserIdAndBookId(1L, 1L);

        // Assert
        assertNull(result);
    }

    @Test
    void getReviewsByBookId_Success() {
        // Arrange
        Set<Review> reviews = Set.of(review);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewsByBook(book)).thenReturn(Optional.of(reviews));

        // Act
        Set<Review> result = reviewService.getReviewsByBookId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(review));
    }

    @Test
    void getReviewsByBookId_NotFound() {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewsByBook(book)).thenReturn(Optional.empty());

        // Act
        Set<Review> result = reviewService.getReviewsByBookId(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void getAverageRating_Success() {
        // Arrange
        Set<Review> reviews = Set.of(review);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewsByBook(book)).thenReturn(Optional.of(reviews));

        // Act
        double result = reviewService.getAverageRating(1L);

        // Assert
        assertEquals(4.0, result, 0.1);
    }

    @Test
    void getAverageRating_EmptyReviews() {
        // Arrange
        Set<Review> reviews = Set.of();
        when(bookService.getBookById(1L)).thenReturn(book);
        when(reviewRepository.findReviewsByBook(book)).thenReturn(Optional.of(reviews));

        // Act
        double result = reviewService.getAverageRating(1L);

        // Assert
        assertEquals(0.0, result, 0.1);
    }

    @Test
    void save_Success() {
        // Arrange
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        reviewService.save(review);

        // Assert
        verify(reviewRepository).save(review);
    }
}
