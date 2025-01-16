package com.example.demo.user;

import com.example.demo.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Register user response entity.
     *
     * @param userRegistrationDTO the user registration dto
     * @param bindingResult       the binding result
     * @return the response entity
     */
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

    /**
     * Login user response entity.
     *
     * @param userLoginDTO  the user login dto
     * @param bindingResult the binding result
     * @return the response entity
     */
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


    /**
     * Update user profile response entity.
     *
     * @param username   the username
     * @param fullName   the full name
     * @param bio        the bio
     * @param profileImg the profile img
     * @return the response entity
     */
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
                ApiResponse response = new ApiResponse(false, "Only JPEG or PNG images are allowed");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

    @Operation(summary = "Get user details",
            description = "Fetches details of a user by username")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Successful response",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": true,
                               "message": "User details fetched successfully",
                               "data": {
                                 "userId": 1,
                                 "username": "aboltus",
                                 "email": "aboltus@example.com"
                               }
                             }
                         """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "User not found",
                               "data": null
                             }
                         """)
                    )
            )
    })

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(@RequestHeader(value="Authorization") String username) throws IOException {
        if (username == null) {
            ApiResponse response = new ApiResponse(false, "User is not logged in");
            return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
        }


        if (userRepository.findByUsername(username).isEmpty()) {
            ApiResponse response = new ApiResponse(false, "User isn't authenticated");
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
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
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }
}