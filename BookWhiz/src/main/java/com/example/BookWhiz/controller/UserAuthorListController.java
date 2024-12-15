package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.service.UserAuthorListService;
import jakarta.persistence.AccessType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("{userId}/authorList/authors")
    public ResponseEntity<Set<AuthorDto>> getAuthorList(@PathVariable Long userId) {

        Set<Author> authors = userAuthorListService.getAuthorsByUserId(userId);

        Set<AuthorDto> authorDTOs = authors.stream()
                .map(author -> new AuthorDto(
                        author.getId(),
                        author.getName(),
                        author.getBirthDate(),
                        author.getBiography(),
                        author.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toSet());

        return authorDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(authorDTOs);

    }

    @GetMapping("/{userId}/authorList/author/{authorId}")
    public boolean isAuthorInTheList (@PathVariable Long userId, @PathVariable Integer authorId) {
        return userAuthorListService.existsAuthorById(userId, authorId);
    }
}
