package com.example.demo.user;

import com.example.demo.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
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
            User user = userService.registerUser(
                    userRegistrationDTO.getUsername(),
                    userRegistrationDTO.getEmail(),
                    userRegistrationDTO.getPassword()
            );
            UserResponseDTO responseDTO = new UserResponseDTO(user.getUsername(), user.getEmail());
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
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
            User user = userService.authenticateUser(userLoginDTO.getUsernameOrEmail(), userLoginDTO.getPassword());
            UserResponseDTO responseDTO = new UserResponseDTO(user.getUsername(), user.getEmail());
            ApiResponse response = new ApiResponse(true, "User logged in successfully", responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }



    @PutMapping(value = "/profile")
    public ResponseEntity<ApiResponse> updateUserProfile(
            @RequestHeader("Authorization") String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String bio,
            @RequestPart(name = "profileImg", required = false) MultipartFile profileImg) {
        try {
            String contentType = profileImg.getContentType();
            assert contentType != null;
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
            }

            UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO(username, fullName, bio, profileImg);
            User user = userService.updateUserProfile(updateDTO);
            UserResponseDTO responseDTO = new UserResponseDTO(user.getUsername(), user.getEmail());
            ApiResponse response = new ApiResponse(true, "User updated in successfully", responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(@RequestHeader(value="Authorization") String username) throws IOException {
        if (username == null) {
            ApiResponse response = new ApiResponse(false, "User is not logged in");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }


        if (userRepository.findByUsername(username).isEmpty()) {
            ApiResponse response = new ApiResponse(false, "User isn't authenticated");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User user = userRepository.findByUsername(username).get();

        java.nio.file.Path filePath = Paths.get("uploads/avatar/"+username).resolve("avatar-"+username+".jpeg");

        Map<String, String> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());
        profile.put("fullName", user.getFull_name());
        profile.put("bio", user.getBio());
        profile.put("followers", String.valueOf(user.getFollowers()));
        profile.put("following", String.valueOf(user.getFollowing()));

        if (Files.exists(filePath)) {
            System.out.println("test pass:");
            profile.put("profileImg", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
        } else {
            System.out.println("test broke:");
            profile.put("profileImg", "https://www.istockphoto.com/photos/user-profile-image");
        }

        ApiResponse response = new ApiResponse(true, "User profile fetched successfully", profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}