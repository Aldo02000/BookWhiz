package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Author;
import com.example.BookWhiz.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1);
        author.setName("J.K. Rowling");
    }

    @Test
    void saveAuthor_WhenAuthorDoesNotExist_ShouldSave() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.empty());

        authorService.saveAuthor(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void saveAuthor_WhenAuthorExists_ShouldNotSave() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));

        authorService.saveAuthor(author);

        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void getAuthorsByPartOfName_ShouldReturnMatchingAuthors() {
        List<Author> authors = Collections.singletonList(author);
        when(authorRepository.findByNameContainingIgnoreCase("Rowling")).thenReturn(authors);

        List<Author> result = authorService.getAuthorsByPartOfName("Rowling");

        assertEquals(1, result.size());
        assertTrue(result.contains(author));
    }

    @Test
    void existsAuthor_WhenAuthorExists_ShouldReturnTrue() {
        when(authorRepository.findByName("J.K. Rowling")).thenReturn(Optional.of(author));

        assertTrue(authorService.existsAuthor("J.K. Rowling"));
    }

    @Test
    void existsAuthor_WhenAuthorDoesNotExist_ShouldReturnFalse() {
        when(authorRepository.findByName("Unknown"))
                .thenReturn(Optional.empty());

        assertFalse(authorService.existsAuthor("Unknown"));
    }

    @Test
    void getAuthorByName_WhenAuthorExists_ShouldReturnAuthor() {
        when(authorRepository.findByName("J.K. Rowling")).thenReturn(Optional.of(author));

        assertEquals(author, authorService.getAuthorbyName("J.K. Rowling"));
    }

    @Test
    void getAuthorByName_WhenAuthorDoesNotExist_ShouldReturnNull() {
        when(authorRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertNull(authorService.getAuthorbyName("Unknown"));
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        assertEquals(author, authorService.getAuthorById(1));
    }

    @Test
    void getAuthorById_WhenAuthorDoesNotExist_ShouldReturnNull() {
        when(authorRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(authorService.getAuthorById(99));
    }

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() {
        List<Author> authors = Collections.singletonList(author);
        when(authorRepository.findAll()).thenReturn(authors);

        assertEquals(authors, authorService.getAllAuthors());
    }
}
