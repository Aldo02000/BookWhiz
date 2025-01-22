package com.example.BookWhiz.service;

import com.example.BookWhiz.controller.BookController;
import com.example.BookWhiz.dto.AuthorDto;
import com.example.BookWhiz.dto.BookDto;
import com.example.BookWhiz.dto.GenreDto;
import com.example.BookWhiz.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate;

    @Autowired
    private BookController bookController;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Value("${openai.api.key}")
    private String key;// Store securely

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<List<BookDto>> getBookSuggestions(List<String> favoriteBooks) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 1.3);
        requestBody.put("messages", Arrays.asList(
                Map.of("role", "system", "content",
                        "You are a book recommendation assistant. Your task is to recommend similar books."),
                Map.of("role", "user", "content",
                        "Given these favorite books: " + String.join(", ", favoriteBooks) +
                                ", suggest 5 other books that are similar to these. Do not include the provided books in the results.")
        ));

        // Set up function calling for book titles
        List<Map<String, Object>> functions = List.of(
                Map.of(
                        "name", "suggest_other_books",
                        "description", "Suggest 5 other books.",
                        "parameters", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "titles", Map.of(
                                                "type", "array",
                                                "items", Map.of("type", "string"),
                                                "minItems", 5,
                                                "maxItems", 5,
                                                "description", "A list of 5 book titles similar to the input but not matching them."
                                        )
                                ),
                                "required", List.of("titles")
                        )
                )
        );
        requestBody.put("functions", functions);

        // Make the HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + key);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Send the request and get the response
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Extract book titles from the response
        List<String> titleList = new ArrayList<>();
        if (response.getBody() != null) {
            List<Map> choices = (List<Map>) response.getBody().get("choices");
            if (!choices.isEmpty()) {
                Map choice = choices.get(0);
                Map message = (Map) choice.get("message");
                Map functionCall = (Map) message.get("function_call");

                if (functionCall != null) {
                    // Extract the "arguments" string (which is in JSON format)
                    String argumentsString = (String) functionCall.get("arguments");

                    try {
                        // Parse the JSON string into a Map
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> arguments = objectMapper.readValue(argumentsString, Map.class);

                        // Extract the "titles" array from the parsed Map
                        List<String> titles = (List<String>) arguments.get("titles");
                        titleList.addAll(titles);

                    } catch (Exception e) {
                        // Handle the exception if parsing fails
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Book> books = bookController.getBooks(titleList);

        List<BookDto> bookDTOs = books.stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getImageLink(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(
                                        author.getId(),
                                        author.getName()
                                )) // Assuming `getName` returns the author's name
                                .collect(Collectors.toSet()),
                        book.getGenres().stream()
                                .map(genreDto -> new GenreDto(
                                        genreDto.getId(),
                                        genreDto.getName()
                                ))
                                .collect(Collectors.toSet()),
                        book.getIsbn10(),
                        book.getIsbn13(),
                        book.getPublishingDate(),
                        book.getPublishingHouse(),
                        book.getLanguage(),
                        book.getPageCount(),
                        book.getSummary()
                ))
                .collect(Collectors.toList());

        return bookDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookDTOs);
    }
}
