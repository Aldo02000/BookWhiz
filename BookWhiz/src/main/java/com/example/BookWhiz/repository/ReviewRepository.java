package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.model.Review;
import com.example.BookWhiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT u.id FROM Review u WHERE u.user = :user AND u.book = :book")
    Long findReviewIdByUserAndBook(@Param("user") User user, @Param("book") Book book);

    Optional<Set<Review>> findReviewsByBook(Book book);
}
