package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.model.UserBookList;
import com.example.BookWhiz.model.UserGenreList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGenreListRepository extends JpaRepository<UserGenreList, Long> {
    Optional<UserGenreList> findById(Long id);
    Optional<UserGenreList> findByUserId(Long userId);
}
