package com.example.demo.user;

public class UserResponseDTO {
    private String username;
    private String email;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
