package com.example.demo.user;

import com.example.demo.imgHandle.SaveImage;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
            String filePath = SaveImage.saveAvatarImage(updateDTO.getProfileImg(), updateDTO.getUsername());
            user.setProfile_image_url(filePath);
        }

        return userRepository.save(user); // Save the updated user profile
    }

    public void followUser(String username, String followUsername) {
        if (username.equals(followUsername)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + username));
        User userToFollow = userRepository.findByUsername(followUsername)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + followUsername));

        userToFollow.getFollowers().add(user);
        user.getFollowers().add(userToFollow);
        userRepository.save(userToFollow);
        userRepository.save(user);
    }

    public void unfollowUser(String username, String followUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + username));
        User userToUnfollow = userRepository.findByUsername(followUsername)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + followUsername));

        userToUnfollow.getFollowers().remove(user);
        user.getFollowing().remove(userToUnfollow);
        userRepository.save(userToUnfollow);
        userRepository.save(user);

    }

    public boolean isFollowing(String username, String followUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + username));
        User userToCheck = userRepository.findByUsername(followUsername)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + followUsername));

        return user.getFollowing().contains(userToCheck);
    }

    public boolean isFollowingEachOther(String username, String followUsername){
        User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("User not found with id: " + username));
        User userToCheck = userRepository.findByUsername(followUsername)
               .orElseThrow(() -> new RuntimeException("User not found with id: " + followUsername));

        return user.getFollowing().contains(userToCheck) && userToCheck.getFollowing().contains(user);
    }
}
