package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserAuthorListRepository extends JpaRepository<UserAuthorList, Long> {
    Optional<UserAuthorList> findByUserId(Long userId);
    boolean existsByUserAndAuthors(User user, Set<Author> author);

    @Query("SELECT u.authors FROM UserAuthorList u WHERE u.user = :user")
    Set<Author> findAuthorsByUser(@Param("user") User user);
}
