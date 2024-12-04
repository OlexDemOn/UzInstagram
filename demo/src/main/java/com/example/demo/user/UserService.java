package com.example.demo.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    String uploadAvatarDir = "uploads/avatar";

    public User registerUser(String username, String email, String password) throws Exception {
        // Check if username or email is already taken
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(username, email);
        if (existingUser.isPresent()) {
            throw new Exception("Username or email is already taken");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(username, email, hashedPassword);
        return userRepository.save(user);
    }

    public User authenticateUser(String usernameOrEmail, String password) throws Exception {
        // Find user by username or email
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid username/email or password");
        }

        User user = userOpt.get();

        // Verify password
        if (!BCrypt.checkpw(password, user.getPassword_hash())) {
            throw new Exception("Invalid username/email or password");
        }

        return user;
    }

    public User updateUserProfile(UserProfileUpdateDTO updateDTO) throws IOException {
        System.out.println("Updating user profile: " + updateDTO.getUsername());
        User user = userRepository.findByUsername(updateDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateDTO.getFullName()!= null) {
            user.setFull_name(updateDTO.getFullName());
        }
        if (updateDTO.getBio()!= null) {
            user.setBio(updateDTO.getBio());
        }

        if (updateDTO.getProfileImg()!= null) {
            System.out.println(updateDTO.getProfileImg());
            String filePath = saveImage(updateDTO.getProfileImg(), updateDTO.getUsername());
            user.setProfile_image_url(filePath);
        }

        return userRepository.save(user); // Save the updated user profile
    }

    private String saveImage(MultipartFile file, String username) throws IOException {
        Path uploadPath =  Paths.get(uploadAvatarDir+ "-"+username);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String newFileName = "avatar-" + username + ".jpeg";
        Path newFilePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilePath.toString();
    }

}
