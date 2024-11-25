package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
