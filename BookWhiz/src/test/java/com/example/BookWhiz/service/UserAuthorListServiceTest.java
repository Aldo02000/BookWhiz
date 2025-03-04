package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserAuthorListServiceTest {

    @Mock
    private UserAuthorListRepository userAuthorListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private UserAuthorListService userAuthorListService;

    private User user;
    private Author author;
    private UserAuthorList userAuthorList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        author = new Author();
        author.setId(1);
        userAuthorList = new UserAuthorList();
        userAuthorList.setUser(user);
    }

    @Test
    void addAuthorToUserList_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(userAuthorListRepository.findByUserId(1L)).thenReturn(Optional.of(userAuthorList));

        // Act
        userAuthorListService.addAuthorToUserList(1L, 1);

        // Assert
        assertTrue(userAuthorList.getAuthors().contains(author));
        verify(userAuthorListRepository, times(1)).save(userAuthorList);
    }

    @Test
    void addAuthorToUserList_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userAuthorListService.addAuthorToUserList(1L, 1));
    }

    @Test
    void addAuthorToUserList_AuthorNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userAuthorListService.addAuthorToUserList(1L, 1));
    }

    @Test
    void addAuthorToUserList_AuthorAlreadyInList() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(userAuthorListRepository.findByUserId(1L)).thenReturn(Optional.of(userAuthorList));

        userAuthorList.getAuthors().add(author); // Simulate author already in the list

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userAuthorListService.addAuthorToUserList(1L, 1));
    }

    @Test
    void removeAuthorFromList_Success() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(userAuthorListRepository.findByUserId(1L)).thenReturn(Optional.of(userAuthorList));

        userAuthorList.getAuthors().add(author); // Simulate author in the list

        // Act
        userAuthorListService.removeAuthorFromList(1L, 1);

        // Assert
        assertFalse(userAuthorList.getAuthors().contains(author));
        verify(userAuthorListRepository, times(1)).save(userAuthorList);
    }

    @Test
    void removeAuthorFromList_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userAuthorListService.removeAuthorFromList(1L, 1));
    }

    @Test
    void removeAuthorFromList_UserAuthorListNotFound() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(userAuthorListRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userAuthorListService.removeAuthorFromList(1L, 1));
    }

    @Test
    void removeAuthorFromList_AuthorNotInList() {
        // Arrange
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(userAuthorListRepository.findByUserId(1L)).thenReturn(Optional.of(userAuthorList));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userAuthorListService.removeAuthorFromList(1L, 1));
    }

    @Test
    void getAuthorsByUserId_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        Set<Author> authors = Set.of(author);
        when(userAuthorListRepository.findAuthorsByUser(user)).thenReturn(authors);

        // Act
        Set<Author> result = userAuthorListService.getAuthorsByUserId(1L);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(author));
    }

    @Test
    void existsAuthorById_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(authorService.getAuthorById(1)).thenReturn(author);
        when(userAuthorListRepository.existsByUserAndAuthors(user, Set.of(author))).thenReturn(true);

        // Act
        boolean result = userAuthorListService.existsAuthorById(1L, 1);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsAuthorById_Failure() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(authorService.getAuthorById(1)).thenReturn(author);
        when(userAuthorListRepository.existsByUserAndAuthors(user, Set.of(author))).thenReturn(false);

        // Act
        boolean result = userAuthorListService.existsAuthorById(1L, 1);

        // Assert
        assertFalse(result);
    }
}
