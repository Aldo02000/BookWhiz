package com.example.BookWhiz.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.BookWhiz.model.Genre;
import com.example.BookWhiz.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre genre;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        genre = new Genre();
        genre.setName("Fantasy");
    }

    @Test
    public void testSaveGenre_genreNotExist_shouldSaveGenre() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.empty());

        // Act
        genreService.saveGenre(genre);

        // Assert
        verify(genreRepository, times(1)).save(genre);  // Verify save is called once
    }

    @Test
    public void testSaveGenre_genreExists_shouldNotSaveGenre() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));

        // Act
        genreService.saveGenre(genre);

        // Assert
        verify(genreRepository, times(0)).save(genre);  // Verify save is not called
    }

    @Test
    public void testGetGenresByPartOfName_shouldReturnGenres() {
        // Arrange
        Set<Genre> genres = Set.of(genre);
        when(genreRepository.findByNameContainingIgnoreCase("Fan")).thenReturn(genres);

        // Act
        Set<Genre> result = genreService.getGenresByPartOfName("Fan");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(genre));
    }

    @Test
    public void testExistGenre_genreExists_shouldReturnTrue() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));

        // Act
        boolean exists = genreService.existGenre(genre.getName());

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistGenre_genreDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.empty());

        // Act
        boolean exists = genreService.existGenre(genre.getName());

        // Assert
        assertFalse(exists);
    }

    @Test
    public void testGetGenre_genreExists_shouldReturnGenre() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));

        // Act
        Genre result = genreService.getGenre(genre.getName());

        // Assert
        assertNotNull(result);
        assertEquals(genre, result);
    }

    @Test
    public void testGetGenre_genreDoesNotExist_shouldReturnNull() {
        // Arrange
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.empty());

        // Act
        Genre result = genreService.getGenre(genre.getName());

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAllGenres_shouldReturnGenres() {
        // Arrange
        List<Genre> genres = List.of(genre);
        when(genreRepository.findAll()).thenReturn(genres);

        // Act
        List<Genre> result = genreService.getAllGenres();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(genre));
    }

    @Test
    public void testGetGenreById_genreExists_shouldReturnGenre() {
        // Arrange
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre));

        // Act
        Genre result = genreService.getGenreById(1);

        // Assert
        assertNotNull(result);
        assertEquals(genre, result);
    }

    @Test
    public void testGetGenreById_genreDoesNotExist_shouldReturnNull() {
        // Arrange
        when(genreRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Genre result = genreService.getGenreById(1);

        // Assert
        assertNull(result);
    }
}
