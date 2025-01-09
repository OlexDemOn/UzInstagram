package com.example.demo.post;

import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Entity
@Setter
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String title;

    private String description;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
