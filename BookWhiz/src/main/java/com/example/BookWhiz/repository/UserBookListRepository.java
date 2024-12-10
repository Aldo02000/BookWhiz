package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.model.UserAuthorList;
import com.example.BookWhiz.model.UserBookList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBookListRepository extends JpaRepository<UserBookList, Long> {
    Optional<UserBookList> findById(Long id);
    Optional<UserBookList> findByUserIdAndTypeOfList(Long userId, BookListType typeOfList);
}
