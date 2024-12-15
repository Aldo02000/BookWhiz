package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.AuthorRepository;
import com.example.BookWhiz.repository.UserAuthorListRepository;
import com.example.BookWhiz.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserAuthorListService {

    @Autowired
    private final UserAuthorListRepository userAuthorListRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorService authorService;

    public UserAuthorListService(UserAuthorListRepository userAuthorListRepository, UserRepository userRepository, AuthorRepository authorRepository) {
        this.userAuthorListRepository = userAuthorListRepository;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }

    public void addAuthorToUserList(Long userId, Integer authorId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        UserAuthorList userAuthorList = userAuthorListRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    UserAuthorList newList = new UserAuthorList();
                    newList.setUser(user);
                    return newList;
                });

        if (userAuthorList.getAuthors().contains(author)) {
            throw new IllegalArgumentException("Author is already in the list");
        }

        userAuthorList.getAuthors().add(author);
        userAuthorListRepository.save(userAuthorList);
    }

    public void removeAuthorFromList(Long userId, Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        UserAuthorList userAuthorList = userAuthorListRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserAuthorList not found"));

        if (!userAuthorList.getAuthors().contains(author)) {
            throw new RuntimeException("Author not found in user's list");
        }

        userAuthorList.getAuthors().remove(author);
        userAuthorListRepository.save(userAuthorList);
    }

    public Set<Author> getAuthorsByUserId(Long userId) {
        User user = userService.getUserById(userId);
        Set<Author> authors = userAuthorListRepository.findAuthorsByUser(user);
        return authors;
    }

    public boolean existsAuthorById(Long userId, Integer authorId) {
        User user = userService.getUserById(userId);
        Author author = authorService.getAuthorById(authorId);
        return userAuthorListRepository.existsByUserAndAuthors(user, Set.of(author));
    }

}
