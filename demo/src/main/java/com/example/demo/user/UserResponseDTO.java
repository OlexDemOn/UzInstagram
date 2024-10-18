package com.example.demo.user;

public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}