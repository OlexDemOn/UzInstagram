package com.example.demo.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // Ім'я користувача,

    @Column(nullable = false)
    private String content;  // Вміст поста

    private String image;    //  шлях до зображення

    @Column(nullable = false)
    private LocalDate datePost; // Дата створення поста

    @Column(name = "user_id", nullable = false)
    private Long userId; // Зв'язок із користувачем
}
