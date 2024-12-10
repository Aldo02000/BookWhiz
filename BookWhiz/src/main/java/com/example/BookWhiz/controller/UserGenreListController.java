package com.example.BookWhiz.controller;

import com.example.BookWhiz.service.UserAuthorListService;
import com.example.BookWhiz.service.UserGenreListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserGenreListController {

    private final UserGenreListService userGenreListService;

    public UserGenreListController(UserGenreListService userGenreListService) {
        this.userGenreListService = userGenreListService;
    }

    @PutMapping("/{userId}/genreList/genre/{genreId}")
    public ResponseEntity<String> addBookToList(@PathVariable Long userId,
                                                @PathVariable Integer genreId) {
        try {
            userGenreListService.addGenreToUserList(userId, genreId);
            return ResponseEntity.ok("Genre added to the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/genreList/genre/{genreId}")
    public ResponseEntity<String> deleteBookfromList(@PathVariable Long userId,
                                                @PathVariable Integer genreId) {
        try {
            userGenreListService.removeGenreFromList(userId, genreId);
            return ResponseEntity.ok("Genre removed from the list successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
