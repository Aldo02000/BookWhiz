package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.UserAuthorList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorListRepository extends JpaRepository<UserAuthorList, Long> {
}
