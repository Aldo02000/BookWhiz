package com.example.BookWhiz.service;

import com.example.BookWhiz.model.*;
import com.example.BookWhiz.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@SpringBootTest
class UserBookListServiceTest {

    @Mock
    private UserBookListRepository userBookListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private UserBookListService userBookListService;

    private User user;
    private Book book;
    private UserBookList userBookList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        book = new Book();
        book.setId(1L);
        userBookList = new UserBookList();
        userBookList.setUser(user);
        userBookList.setTypeOfList(BookListType.FAVORITES);
    }

    @Test
    void addBookToUserList_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userBookListRepository.findByUserIdAndTypeOfList(1L, BookListType.FAVORITES)).thenReturn(Optional.of(userBookList));

        // Act
        userBookListService.addBookToUserList(1L, 1L, BookListType.FAVORITES);

        // Assert
        assertTrue(userBookList.getBooks().contains(book));
        verify(userBookListRepository).save(userBookList);
    }

    @Test
    void addBookToUserList_AlreadyInList_Failure() {
        // Arrange
        userBookList.getBooks().add(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userBookListRepository.findByUserIdAndTypeOfList(1L, BookListType.FAVORITES)).thenReturn(Optional.of(userBookList));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userBookListService.addBookToUserList(1L, 1L, BookListType.FAVORITES);
        });
        assertEquals("Book is already in the list", exception.getMessage());
    }

    @Test
    void removeBookFromUserList_Success() {
        // Arrange
        userBookList.getBooks().add(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userBookListRepository.findByUserIdAndTypeOfList(1L, BookListType.FAVORITES)).thenReturn(Optional.of(userBookList));
        when(userBookListRepository.save(any(UserBookList.class))).thenReturn(userBookList);

        // Act
        userBookListService.removeBookFromUserList(1L, 1L, BookListType.FAVORITES);

        // Assert
        assertFalse(userBookList.getBooks().contains(book));
        verify(userBookListRepository).save(userBookList);
    }

    @Test
    void removeBookFromUserList_BookNotInList_Failure() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userBookListRepository.findByUserIdAndTypeOfList(1L, BookListType.FAVORITES)).thenReturn(Optional.of(userBookList));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userBookListService.removeBookFromUserList(1L, 1L, BookListType.FAVORITES);
        });
        assertEquals("Book not found in user's list", exception.getMessage());
    }

    @Test
    void getBooksByUserIdAndListType_Success() {
        // Arrange
        Set<Book> books = new HashSet<>();
        books.add(book);
        when(userService.getUserById(1L)).thenReturn(user);
        when(userBookListRepository.findBooksByUserAndTypeOfList(user, BookListType.FAVORITES)).thenReturn(books);

        // Act
        Set<Book> result = userBookListService.getBooksByUserIdAndListType(1L, BookListType.FAVORITES);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(book));
    }

    @Test
    void existsBookInUserList_Success() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(userBookListRepository.existsByBooksAndUserAndTypeOfList(Set.of(book), user, BookListType.FAVORITES)).thenReturn(true);

        // Act
        boolean result = userBookListService.existsBookInUserList(1L, 1L, BookListType.FAVORITES);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsBookInUserList_Failure() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(user);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(userBookListRepository.existsByBooksAndUserAndTypeOfList(Set.of(book), user, BookListType.FAVORITES)).thenReturn(false);

        // Act
        boolean result = userBookListService.existsBookInUserList(1L, 1L, BookListType.FAVORITES);

        // Assert
        assertFalse(result);
    }
}
