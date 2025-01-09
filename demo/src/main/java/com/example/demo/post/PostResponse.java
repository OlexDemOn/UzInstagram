package com.example.demo.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String imageUrl;
    private String title;
    private String description;
//    private String imgUrl;
    private Date createdAt;
    private String username;
    private String profileImageUrl;
}