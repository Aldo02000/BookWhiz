package com.example.BookWhiz.repository;

import com.example.BookWhiz.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserBookListRepository extends JpaRepository<UserBookList, Long> {
    Optional<UserBookList> findById(Long id);
    Optional<UserBookList> findByUserIdAndTypeOfList(Long userId, BookListType typeOfList);
    boolean existsByBooksAndUserAndTypeOfList(Set<Book> books, User user, BookListType typeOfList);

    @Query("SELECT u.books FROM UserBookList u WHERE u.user = :user AND u.typeOfList = :typeOfList")
    Set<Book> findBooksByUserAndTypeOfList(@Param("user") User user, @Param("typeOfList") BookListType typeOfList);
}
