package com.example.BookWhiz.service;

import com.example.BookWhiz.controller.BookController;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(openAIService, "bookService", bookService);
        ReflectionTestUtils.setField(openAIService, "model", "gpt-3.5-turbo");
        ReflectionTestUtils.setField(openAIService, "url", "https://api.openai.com/v1/chat/completions");
        ReflectionTestUtils.setField(openAIService, "key", "test-api-key");
    }

    @Test
    void testGetBookSuggestions_SuccessfulResponse() throws JsonProcessingException {
        // Mock favorite books
        List<String> favoriteBooks = List.of("Book A", "Book B");

        Map<String, Object> mockResponseBody = Map.of(
            "choices", List.of(
                Map.of(
                    "message", Map.of(
                        "function_call", Map.of(
                            "arguments", "{\"titles\": [\"Book X\", \"Book Y\", \"Book Z\", \"Book W\", \"Book V\"]}"
                        )
                    )
                )
            )
        );

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        doReturn(mockResponse).when(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));

        // Mock book retrieval from BookController
        List<Book> mockBooks = List.of(
                new Book(1L, "Suggested Book 1", "img1.jpg", new HashSet<>(), new HashSet<>(), "1234567890", "9876543210123",
                        "2020-01-01", "Publisher A", "English", 300, "Summary 1"),
                new Book(2L, "Suggested Book 2", "img2.jpg", new HashSet<>(), new HashSet<>(), "1234567891", "9876543210124",
                        "2021-02-02", "Publisher B", "English", 350, "Summary 2")
        );
        when(bookService.getBooks(anyList())).thenReturn(mockBooks);

        // Call method
        ResponseEntity<List<BookDto>> result = openAIService.getBookSuggestions(favoriteBooks);

        // Verify results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals("Suggested Book 1", result.getBody().get(0).getTitle());
        assertEquals("Suggested Book 2", result.getBody().get(1).getTitle());
    }

    @Test
    void testGetBookSuggestions_EmptyResponse() {
        // Mock API returning an empty response with "choices" as an empty list
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", new ArrayList<>());  // Empty list for "choices"

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);
        doReturn(mockResponse).when(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));

        // Call method
        ResponseEntity<List<BookDto>> result = openAIService.getBookSuggestions(List.of("Book A"));

        // Verify that no content is returned
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }


    @Test
    void testGetBookSuggestions_MalformedResponse() {
        // Mock API returning a response with an invalid structure (missing "choices")
        Map<String, Object> malformedResponse = new HashMap<>();
        // The response body doesn't contain "choices", simulating a malformed response
        malformedResponse.put("unexpected_key", "unexpected_value");

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(malformedResponse, HttpStatus.OK);
        doReturn(mockResponse).when(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));

        // Call method
        ResponseEntity<List<BookDto>> result = openAIService.getBookSuggestions(List.of("Book A"));

        // Verify that no content is returned due to the malformed response
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

}
