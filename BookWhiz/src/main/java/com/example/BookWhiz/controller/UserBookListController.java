package com.example.BookWhiz.controller;

import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.service.UserBookListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserBookListController {

    private final UserBookListService userBookListService;

    public UserBookListController(UserBookListService userBookListService) {
        this.userBookListService = userBookListService;
    }

    @PutMapping("/{userId}/bookList/{listType}/book/{bookId}")
    public ResponseEntity<String> addBookToList(@PathVariable Long userId,
                                                @PathVariable BookListType listType,
                                                @PathVariable Long bookId) {
        try {
            userBookListService.addBookToUserList(userId, bookId, listType);
            return ResponseEntity.ok("Book added to the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/bookList/{listType}/book/{bookId}")
    public ResponseEntity<String> deleteBookFromList(@PathVariable Long userId,
                                                     @PathVariable BookListType listType,
                                                     @PathVariable Long bookId) {
        try {
            userBookListService.removeBookFromUserList(userId, bookId, listType);
            return ResponseEntity.ok("Book removed from the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

