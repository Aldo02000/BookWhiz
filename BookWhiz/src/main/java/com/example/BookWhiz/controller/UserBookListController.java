package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.*;
import com.example.BookWhiz.service.UserBookListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/{userId}/bookList/{listType}/books")
    public ResponseEntity<Set<BookDto>> getBookList(@PathVariable Long userId,
                                                  @PathVariable BookListType listType) {

        Set<Book> books = userBookListService.getBooksByUserIdAndListType(userId, listType);

        Set<BookDto> bookDTOs = books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getImageLink(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(
                                        author.getId(),
                                        author.getName()
                                )) // Assuming `getName` returns the author's name
                                .collect(Collectors.toSet()),
                        book.getGenres().stream()
                                .map(genreDto -> new GenreDto(
                                        genreDto.getId(),
                                        genreDto.getName()
                                ))
                                .collect(Collectors.toSet()),
                        book.getIsbn10(),
                        book.getIsbn13(),
                        book.getPublishingDate(),
                        book.getPublishingHouse(),
                        book.getLanguage(),
                        book.getPageCount(),
                        book.getSummary()
                ))
                .collect(Collectors.toSet());

        return books.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookDTOs);
    }
}

