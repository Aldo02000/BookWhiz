package com.example.BookWhiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long id;
    private String content;
    private String createdDate;
    private int rating;

    public ReviewDto(Long id, String content, String createdDate, int rating) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.rating = rating;
    }
}
