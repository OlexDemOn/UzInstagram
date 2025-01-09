package com.example.demo.post;

import com.example.demo.dto.ApiResponse;
import com.example.demo.imgHandle.SaveImage;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse> getPosts(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 5;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostResponse> data = posts.getContent().stream().map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());

            java.nio.file.Path filePath = Paths.get("uploads/post/"+post.getId()).resolve("post-"+post.getId()+".jpeg");

            if (Files.exists(filePath)) {
                System.out.println("test pass:");
                try {
                    response.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("test broke:");
                response.setImageUrl("");
            }

            response.setTitle(post.getTitle());
            response.setCreatedAt(post.getCreatedAt());
            response.setUsername(post.getUser().getUsername());
            response.setDescription(post.getDescription());

            java.nio.file.Path filePathAvatar = Paths.get("uploads/avatar/"+post.getUser().getUsername()).resolve("avatar-"+post.getUser().getUsername()+".jpeg");

            if (Files.exists(filePathAvatar)) {
                System.out.println("test pass:");
                try {
                    response.setProfileImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePathAvatar)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("test broke:");
                response.setProfileImageUrl("https://www.istockphoto.com/photos/user-profile-image");
            }

            return response;
        }).toList();

        boolean hasMorePosts = posts.hasNext();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("posts", data);
        responseBody.put("hasMorePosts", hasMorePosts);

        ApiResponse response = new ApiResponse(true, "User profile fetched successfully", responseBody);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getPostByUser(@RequestParam(required = true) String username) {
        User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findAllByUser(user);

        List<PostResponse> data = posts.stream().map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());

            java.nio.file.Path filePath = Paths.get("uploads/post/"+post.getId()).resolve("post-"+post.getId()+".jpeg");

            if (Files.exists(filePath)) {
                System.out.println("test pass:");
                try {
                    response.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("test broke:");
                response.setImageUrl("");
            }

            response.setTitle(post.getTitle());
            response.setCreatedAt(post.getCreatedAt());
            response.setDescription(post.getDescription());

            return response;
        }).toList();

        ApiResponse response = new ApiResponse(true, "User profile fetched successfully", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestParam(required = true) String username,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = true) String title,
                                                  @RequestPart(name = "imageUrl", required = false) MultipartFile imageUrl) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String contentType = imageUrl.getContentType();
            assert contentType != null;
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
            }

            Post post = new Post();
            post.setDescription(description);
            post.setTitle(title);
            post.setUser(user);

            Post savedPost = postRepository.save(post);

            String filePath = SaveImage.savePostImage(imageUrl, savedPost.getId());

            savedPost.setImageUrl(filePath);
            postRepository.save(savedPost);

            ApiResponse response = new ApiResponse(true, "User profile fetched successfully", post);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}

