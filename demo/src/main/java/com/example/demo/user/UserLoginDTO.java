package com.example.demo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    @NotBlank(message = "Username or Email is mandatory")
    private String usernameOrEmail;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    // Constructors
    public UserLoginDTO() {}

    public UserLoginDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

}
