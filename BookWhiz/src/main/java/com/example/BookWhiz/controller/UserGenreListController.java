package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.UserGenreListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("{userId}/genreList/genres")
    public ResponseEntity<Set<GenreDto>> getGenreList(@PathVariable Long userId) {

        Set<Genre> genres = userGenreListService.getGenresByUserId(userId);

        Set<GenreDto> genreDtos = genres.stream()
                .map(genre -> new GenreDto(
                        genre.getId(),
                        genre.getName(),
                        genre.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toSet());

        return genreDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(genreDtos);
    }

    @GetMapping("/{userId}/genreList/genre/{genreId}")
    public boolean isGenreInTheList (@PathVariable Long userId, @PathVariable Integer genreId) {
        return userGenreListService.existsGenreById(userId, genreId);
    }
}
