package com.example.BookWhiz.service;

import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void saveAuthor(Author author) {
        // Check if the author already exists in the database by name
        Optional<Author> existingAuthor = authorRepository.findByName(author.getName());

        // Save only if the author does not already exist
        if (existingAuthor.isEmpty()) {
            authorRepository.save(author);
        }
    }

    public List<Author> getAuthorsByPartOfName(String partOfName) {
        List<Author> existingAuthors = authorRepository.findByNameContainingIgnoreCase(partOfName);
        return existingAuthors;
    }

    public boolean existsAuthor(String name) {
        Optional<Author> existingAuthor = authorRepository.findByName(name);
        return existingAuthor.isPresent();
    }

    public Author getAuthorbyName(String name) {
        Optional<Author> existingAuthor = authorRepository.findByName(name);
        return existingAuthor.orElse(null);
    }

    public Author getAuthorById(Integer id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<AuthorDto> mapToDTOList(List<Author> authors) {

        return authors.stream()
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
                .collect(Collectors.toList());
    }
}
