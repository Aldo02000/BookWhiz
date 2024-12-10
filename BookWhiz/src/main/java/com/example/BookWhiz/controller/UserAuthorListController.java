package com.example.BookWhiz.controller;

import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.service.UserAuthorListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserAuthorListController {

    private final UserAuthorListService userAuthorListService;

    public UserAuthorListController(UserAuthorListService userAuthorListService) {
        this.userAuthorListService = userAuthorListService;
    }

    @PutMapping("/{userId}/authorList/author/{authorId}")
    public ResponseEntity<String> addAuthorToList(@PathVariable Long userId,
                                                @PathVariable Integer authorId) {
        try {
            userAuthorListService.addAuthorToUserList(userId, authorId);
            return ResponseEntity.ok("Author added to the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/authorList/author/{authorId}")
    public ResponseEntity<String> deleteAuthorFromList(@PathVariable Long userId,
                                                @PathVariable Integer authorId) {
        try {
            userAuthorListService.removeAuthorFromList(userId, authorId);
            return ResponseEntity.ok("Author removed from the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
