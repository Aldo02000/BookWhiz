package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.AuthorRepository;
import com.example.BookWhiz.service.AuthorService;
import com.example.BookWhiz.service.BookService;
import com.example.BookWhiz.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/authors")
@RestController
public class AuthorController {

    @Autowired
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/allAuthors")
    public ResponseEntity<Set<AuthorDto>> getAllAuthors() {

        List<Author> authors = authorService.getAllAuthors();

        Set<AuthorDto> authorDtos = authors.stream()
                .map(author -> new AuthorDto(
                        author.getId(),
                        author.getName(),
                        author.getBirthDate(),
                        author.getBiography(),
                        author.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.toSet())
                ))
                .sorted(Comparator.comparing(AuthorDto::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return authorDtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(authorDtos);
    }
}
