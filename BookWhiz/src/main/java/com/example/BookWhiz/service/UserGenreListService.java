package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserGenreListService {

    @Autowired
    private final UserGenreListRepository userGenreListRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final GenreRepository genreRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final GenreService genreService;

    public UserGenreListService(UserGenreListRepository userGenreListRepository, UserRepository userRepository, GenreRepository genreRepository, UserService userService, GenreService genreService) {
        this.userGenreListRepository = userGenreListRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.userService = userService;
        this.genreService = genreService;
    }

    public void addGenreToUserList(Long userId, Integer genreId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        UserGenreList userGenreList = userGenreListRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    UserGenreList newList = new UserGenreList();
                    newList.setUser(user);
                    return newList;
                });

        if (userGenreList.getGenres().contains(genre)) {
            throw new IllegalArgumentException("Genre is already in the list");
        }

        userGenreList.getGenres().add(genre);
        userGenreListRepository.save(userGenreList);
    }

    public void removeGenreFromList(Long userId, Integer genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        UserGenreList userGenreList = userGenreListRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserGenreList not found"));

        if (!userGenreList.getGenres().contains(genre)) {
            throw new RuntimeException("Genre not found in user's list");
        }

        userGenreList.getGenres().remove(genre);
        userGenreListRepository.save(userGenreList);
    }

    public Set<Genre> getGenresByUserId(Long userId) {
        User user = userService.getUserById(userId);
        Set<Genre> genres = userGenreListRepository.findGenresByUser(user);
        return genres;
    }

    public boolean existsGenreById(Long userId, Integer genreId) {
        User user = userService.getUserById(userId);
        Genre genre = genreService.getGenreById(genreId);

        return userGenreListRepository.existsByUserAndGenres(user, Set.of(genre));
    }
}
