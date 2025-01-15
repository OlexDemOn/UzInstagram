package com.example.demo.post.comment;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private String ownerUsername;
    private String ownerProfileImageUrl;
    private Date createdAt;
}
