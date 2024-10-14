// src/main/java/com/example/userapi/controller/AuthController.java

package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        // Validate input data
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userService.registerUser(
                    userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPassword()
            );
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            // In a real application, handle different exception types more gracefully
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, HttpSession session) {
        // Validate input data
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            // userDTO.getUsername() can contain either username or email
            User user = userService.authenticateUser(userDTO.getUsername(), userDTO.getPassword());
            // Set user in session
            session.setAttribute("user", user);
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            // Invalid credentials
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint to get the current logged-in user's profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint for user logout
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}
