package com.example.demo.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO {
    private Long userId;
    private String imageUrl;
    private String description;
    private String title;
}
