package com.example.demo.user;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // Register a new user
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
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Invalid username/email or password");
        }

        return user;
    }

    public User updateUserProfile(UserProfileUpdateDTO updateDTO) {
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
            user.setProfile_image_url(updateDTO.getProfileImg());
        }

//        user.setFull_name(updateDTO.getFullName());
//        user.setBio(updateDTO.getBio());
//        user.setProfile_image_url(updateDTO.getProfileImg());

        return userRepository.save(user); // Save the updated user profile
    }
}
