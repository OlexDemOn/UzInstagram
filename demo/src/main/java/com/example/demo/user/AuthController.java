// src/main/java/com/example/userapi/controller/AuthController.java

package com.example.demo.user;

import com.example.demo.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        // Validation handled by GlobalExceptionHandler
        if (bindingResult.hasErrors()) {
            // Collect errors
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            ApiResponse response = new ApiResponse(false, "Validation Failed", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userService.registerUser(
                    userRegistrationDTO.getUsername(),
                    userRegistrationDTO.getEmail(),
                    userRegistrationDTO.getPassword()
            );
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            ApiResponse response = new ApiResponse(true, "User registered successfully", responseDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            if (e.getMessage().equals("Query did not return a unique result: 2 results were returned")) {
                ApiResponse response = new ApiResponse(false, "Username or email is already taken");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        // Validation handled by GlobalExceptionHandler
        if (bindingResult.hasErrors()) {
            // Collect errors
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            ApiResponse response = new ApiResponse(false, "Validation Failed", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // userLoginDTO.getUsernameOrEmail() can contain either username or email
            User user = userService.authenticateUser(userLoginDTO.getUsernameOrEmail(), userLoginDTO.getPassword());
            // Set user in session
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            ApiResponse response = new ApiResponse(true, "User logged in successfully", responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint to get the current logged-in user's profile
//    @GetMapping("/profile")
//    public ResponseEntity<ApiResponse> getUserProfile(HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        if (user != null) {
//            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
//            ApiResponse response = new ApiResponse(true, "User profile fetched successfully", responseDTO);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            ApiResponse response = new ApiResponse(false, "User is not logged in");
//            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateUserProfile(@RequestBody UserProfileUpdateDTO updateDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            ApiResponse response = new ApiResponse(false, "Validation Failed", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userService.updateUserProfile(updateDTO);
            UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            ApiResponse response = new ApiResponse(true, "User logged in successfully", responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
//        if (user != null) {
//            User updatedUser = userService.updateUserProfile(user.getId(), updateDTO);
//            return ResponseEntity.ok(new ApiResponse(true, "Profile updated successfully", updatedUser));
//        } else {
//            return ResponseEntity.status(401).body(new ApiResponse(false, "User not logged in"));
//        }
    }
}