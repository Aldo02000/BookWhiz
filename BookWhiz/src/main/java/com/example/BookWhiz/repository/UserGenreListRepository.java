package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserGenreListRepository extends JpaRepository<UserGenreList, Long> {
    Optional<UserGenreList> findById(Long id);
    Optional<UserGenreList> findByUserId(Long userId);
    boolean existsByUserAndGenres(User user, Set<Genre> genres);

    @Query("SELECT u.genres FROM UserGenreList u WHERE u.user = :user")
    Set<Genre> findGenresByUser(@Param("user") User user);
}
