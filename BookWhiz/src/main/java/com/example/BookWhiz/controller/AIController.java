package com.example.BookWhiz.controller;

import com.example.BookWhiz.dto.*;
import com.example.BookWhiz.service.OpenAIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/suggest")
    public ResponseEntity<List<BookDto>> suggestBooks(@RequestBody List<String> favoriteBooks) {
        return openAIService.getBookSuggestions(favoriteBooks);
    }


}
