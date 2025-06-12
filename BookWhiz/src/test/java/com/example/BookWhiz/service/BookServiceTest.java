package com.example.BookWhiz.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setIsbn13("9780134685991");
        book.setTitle("Effective Java");
    }

    @Test
    public void testSaveBook_bookNotExist_shouldSaveBook() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.empty());

        // Act
        bookService.saveBook(book);

        // Assert
        verify(bookRepository, times(1)).save(book);  // verify save is called once
    }

    @Test
    public void testSaveBook_bookExists_shouldNotSaveBook() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.of(book));

        // Act
        bookService.saveBook(book);

        // Assert
        verify(bookRepository, times(0)).save(book);  // verify save is not called
    }

    @Test
    public void testGetBooksByTitle_shouldReturnBooks() {
        // Arrange
        List<Book> books = List.of(book);
        when(bookRepository.findByTitleContainingIgnoreCase("Effective")).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooksByTitle("Effective");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(book));
    }

    @Test
    public void testExistsBook_bookExists_shouldReturnTrue() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.of(book));

        // Act
        boolean exists = bookService.existsBook(book.getIsbn13());

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsBook_bookDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.empty());

        // Act
        boolean exists = bookService.existsBook(book.getIsbn13());

        // Assert
        assertFalse(exists);
    }

    @Test
    public void testGetBooksByGenre_shouldReturnBooks() {
        // Arrange
        Genre genre = new Genre();
        List<Book> books = List.of(book);
        when(bookRepository.findByGenres(genre)).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooksByGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(book));
    }

    @Test
    public void testGetBooksByAuthor_shouldReturnBooks() {
        // Arrange
        Author author = new Author();
        List<Book> books = List.of(book);
        when(bookRepository.findByAuthors(author)).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooksByAuthor(author);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(book));
    }

    @Test
    public void testGetBookByIsbn13_bookExists_shouldReturnBook() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.getBookByIsbn13(book.getIsbn13());

        // Assert
        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    public void testGetBookByIsbn13_bookDoesNotExist_shouldReturnNull() {
        // Arrange
        when(bookRepository.findByIsbn13(book.getIsbn13())).thenReturn(Optional.empty());

        // Act
        Book result = bookService.getBookByIsbn13(book.getIsbn13());

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetBookById_bookExists_shouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.getBookById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    public void testGetBookById_bookDoesNotExist_shouldReturnNull() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Book result = bookService.getBookById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAllBooks_shouldReturnBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(book));

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(book));
    }

    @Test
    public void testGetBooksByTitleContaining_shouldReturnBooks() {
        // Arrange
        List<Book> books = List.of(book);
        when(bookRepository.findByTitleContainingIgnoreCase("Effective")).thenReturn(books);

        // Act
        List<Book> result = bookService.getBooksByTitleContaining("Effective");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(book));
    }
}
