package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    // Follow a user
    @PostMapping("/{username}/follow/{followUsername}")
    public ResponseEntity<String> followUser(@PathVariable String username, @PathVariable String followUsername) {
        userService.followUser(username, followUsername);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    // Unfollow a user
    @PostMapping("/{username}/unfollow/{followUsername}")
    public ResponseEntity<String> unfollowUser(@PathVariable String username, @PathVariable String followUsername) {
        userService.unfollowUser(username, followUsername);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }

    // Check if a user follows another user
    @GetMapping("/{username}/follows/{followUsername}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String username, @PathVariable String followUsername) {
        boolean isFollowing = userService.isFollowing(username, followUsername);
        return ResponseEntity.ok(isFollowing);
    }

    //check if users follow each other
    @GetMapping("/{username}/isFollowing/{followUsername}")
    public ResponseEntity<Boolean> isFollowingEachOther(@PathVariable String username, @PathVariable String followUsername) {
        boolean isFollowingEachOther = userService.isFollowingEachOther(username, followUsername);
        return ResponseEntity.ok(isFollowingEachOther);
    }
}