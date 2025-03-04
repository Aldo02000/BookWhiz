package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserGenreListServiceTest {

    @Mock
    private UserGenreListRepository userGenreListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private UserService userService;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private UserGenreListService userGenreListService;

    private User user;
    private Genre genre;
    private UserGenreList userGenreList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        genre = new Genre();
        genre.setId(1);

        userGenreList = new UserGenreList();
        userGenreList.setUser(user);
        userGenreList.setGenres(new HashSet<>());
    }

    @Test
    void addGenreToUserList_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));
        when(userGenreListRepository.findByUserId(1L)).thenReturn(Optional.of(userGenreList));

        // Act
        userGenreListService.addGenreToUserList(1L, 1);

        // Assert
        assertTrue(userGenreList.getGenres().contains(genre));
        verify(userGenreListRepository).save(userGenreList);
    }

    @Test
    void addGenreToUserList_GenreAlreadyInList() {
        // Arrange
        userGenreList.getGenres().add(genre);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));
        when(userGenreListRepository.findByUserId(1L)).thenReturn(Optional.of(userGenreList));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userGenreListService.addGenreToUserList(1L, 1));
        assertEquals("Genre is already in the list", exception.getMessage());
    }

    @Test
    void removeGenreFromList_Success() {
        // Arrange
        userGenreList.getGenres().add(genre);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));
        when(userGenreListRepository.findByUserId(1L)).thenReturn(Optional.of(userGenreList));
        when(userGenreListRepository.save(any(UserGenreList.class))).thenReturn(userGenreList);

        // Act
        userGenreListService.removeGenreFromList(1L, 1);

        // Assert
        assertFalse(userGenreList.getGenres().contains(genre));
        verify(userGenreListRepository).save(userGenreList);
    }

    @Test
    void removeGenreFromList_GenreNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));
        when(userGenreListRepository.findByUserId(1L)).thenReturn(Optional.of(userGenreList));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userGenreListService.removeGenreFromList(1L, 1));
        assertEquals("Genre not found in user's list", exception.getMessage());
    }

    @Test
    void getGenresByUserId_Success() {
        // Arrange
        Set<Genre> genres = Set.of(genre);
        when(userService.getUserById(1L)).thenReturn(user);
        when(userGenreListRepository.findGenresByUser(user)).thenReturn(genres);

        // Act
        Set<Genre> result = userGenreListService.getGenresByUserId(1L);

        // Assert
        assertTrue(result.contains(genre));
    }

    @Test
    void existsGenreById_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(genreService.getGenreById(1)).thenReturn(genre);
        when(userGenreListRepository.existsByUserAndGenres(user, Set.of(genre))).thenReturn(true);

        // Act
        boolean result = userGenreListService.existsGenreById(1L, 1);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsGenreById_Failure() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(genreService.getGenreById(1)).thenReturn(genre);
        when(userGenreListRepository.existsByUserAndGenres(user, Set.of(genre))).thenReturn(false);

        // Act
        boolean result = userGenreListService.existsGenreById(1L, 1);

        // Assert
        assertFalse(result);
    }
}
