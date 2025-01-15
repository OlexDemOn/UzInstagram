package com.example.demo.post;

import com.example.demo.dto.ApiResponse;
import com.example.demo.imgHandle.SaveImage;
import com.example.demo.post.comment.Comment;
import com.example.demo.post.comment.CommentDTO;
import com.example.demo.post.comment.CommentRequest;
import com.example.demo.post.comment.CommentService;
import com.example.demo.post.like.LikesService;
import com.example.demo.user.User;
import com.example.demo.user.UserController;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikesService likesService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @RequestHeader(value="Authorization") String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post= postRepository.findPostById(postId);
        likesService.likePost(post, user);
        return ResponseEntity.ok("Post liked");
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @RequestHeader(value="Authorization") String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findPostById(postId);
        likesService.unlikePost(post, user);
        return ResponseEntity.ok("Post unliked");
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPosts(@RequestParam(defaultValue = "0") int page, @RequestHeader(value="Authorization") String username) {
        int pageSize = 5;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<PostResponse> data = posts.getContent().stream()
                .filter(post -> post.isOpened() || userService.isFollowingEachOther(username, post.getUser().getUsername()) || Objects.equals(username, post.getUser().getUsername()))
                .map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());

            java.nio.file.Path filePath = Paths.get("uploads/post/"+post.getId()).resolve("post-"+post.getId()+".jpeg");

            if (Files.exists(filePath)) {
                try {
                    response.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setImageUrl("");
            }

            int likeCount = likesService.getLikeCount(post);
            boolean isLiked = likesService.isPostLikedByUser(post, user);

            response.setTitle(post.getTitle());
            response.setCreatedAt(post.getCreatedAt());
            response.setUsername(post.getUser().getUsername());
            response.setDescription(post.getDescription());
            response.setLikesCount(likeCount);
            response.setLikedByCurrentUser(isLiked);
            response.setOpened(post.isOpened());

            java.nio.file.Path filePathAvatar = Paths.get("uploads/avatar/"+post.getUser().getUsername()).resolve("avatar-"+post.getUser().getUsername()+".jpeg");

            if (Files.exists(filePathAvatar)) {
                try {
                    response.setProfileImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePathAvatar)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
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
    public ResponseEntity<ApiResponse> getPostByUser(@RequestParam(required = true) String username, @RequestHeader(value="Authorization") String usernameCurrent) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userCurrent = userRepository.findByUsername(usernameCurrent)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findAllByUserOrderByCreatedAtDesc(user);

        List<PostResponse> data = posts.stream()
                .filter(post -> post.isOpened() ||
                        userService.isFollowingEachOther(usernameCurrent, username) ||
                        Objects.equals(username, usernameCurrent))
                .map(post -> {

            PostResponse response = new PostResponse();
            response.setId(post.getId());

            java.nio.file.Path filePath = Paths.get("uploads/post/"+post.getId()).resolve("post-"+post.getId()+".jpeg");

            if (Files.exists(filePath)) {
                try {
                    response.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setImageUrl("");
            }
            int likeCount = likesService.getLikeCount(post);
            boolean isLiked = likesService.isPostLikedByUser(post, userCurrent);

            response.setTitle(post.getTitle());
            response.setCreatedAt(post.getCreatedAt());
            response.setDescription(post.getDescription());
            response.setUsername(post.getUser().getUsername());
            response.setLikesCount(likeCount);
            response.setLikedByCurrentUser(isLiked);
            response.setOpened(post.isOpened());

            return response;
        }).toList();


        Map<String, String> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        profile.put("fullName", user.getFull_name());
        profile.put("bio", user.getBio());
        profile.put("profileImageUrl", user.getProfile_image_url());
        profile.put("followers", String.valueOf(user.getFollowers().size()));
        profile.put("following", String.valueOf(user.getFollowing().size()));
        java.nio.file.Path filePathAvatar = Paths.get("uploads/avatar/"+user.getUsername()).resolve("avatar-"+user.getUsername()+".jpeg");

        if (Files.exists(filePathAvatar)) {
            try {
                profile.put("profileImageUrl", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePathAvatar)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            profile.put("profileImageUrl", "https://www.istockphoto.com/photos/user-profile-image");
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("posts", data);
        responseBody.put("profile", profile);

        ApiResponse response = new ApiResponse(true, "User profile fetched successfully", responseBody);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/byId")

    public ResponseEntity<ApiResponse> getPostByIds( @RequestHeader(value="Authorization") String username, @RequestBody(required = true) List<Long> ids) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findByIdIn(ids);
        System.out.println(posts.size());

        List<PostResponse> data = posts.stream()
                .filter(post -> post.isOpened() || userService.isFollowingEachOther(username, post.getUser().getUsername()) || Objects.equals(username, post.getUser().getUsername()))
                .map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());

            java.nio.file.Path filePath = Paths.get("uploads/post/"+post.getId()).resolve("post-"+post.getId()+".jpeg");

            if (Files.exists(filePath)) {
                try {
                    response.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setImageUrl("");
            }
            int likeCount = likesService.getLikeCount(post);
            boolean isLiked = likesService.isPostLikedByUser(post, user);

            java.nio.file.Path filePathAvatar = Paths.get("uploads/avatar/"+user.getUsername()).resolve("avatar-"+user.getUsername()+".jpeg");

            if (Files.exists(filePathAvatar)) {
                try {
                    response.setProfileImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePathAvatar)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setProfileImageUrl("https://www.istockphoto.com/photos/user-profile-image");
            }

            response.setTitle(post.getTitle());
            response.setCreatedAt(post.getCreatedAt());
            response.setDescription(post.getDescription());
            response.setUsername(post.getUser().getUsername());
            response.setLikesCount(likeCount);
            response.setLikedByCurrentUser(isLiked);
            response.setOpened(post.isOpened());
            response.setUsername(post.getUser().getUsername());
            response.setLikedByCurrentUser(isLiked);
            response.setLikesCount(likeCount);

            return response;
        }).toList();


        ApiResponse response = new ApiResponse(true, "User profile fetched successfully", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> createPost(@RequestHeader("Authorization") String username,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = true) String title,
                                                  @RequestParam(required = true) Boolean postType,
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
            post.setOpened(postType);

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

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(commentRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentService.addComment(post, user, commentRequest.getContent());

        CommentDTO commentDTO = toCommentDTO(comment);

        return ResponseEntity.ok(commentDTO);
    }


    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse> getComments(@PathVariable Long postId) {
        Post post = postRepository.findPostById(postId); // Fetch the post entity
        List<CommentDTO> comments = commentService.getCommentsForPost(post) // Fetch comments
                .stream()
                .map(this::toCommentDTO) // Map each comment to CommentDTO
                .collect(Collectors.toList());

        if (!comments.isEmpty()){
            ApiResponse response = new ApiResponse(true, "Comments fetched successfully", comments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponse response = new ApiResponse(false, "Comments not found");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setOwnerUsername(comment.getUser().getUsername());

        java.nio.file.Path filePath = Paths.get("uploads/avatar/"+comment.getUser().getUsername()).resolve("avatar-"+comment.getUser().getUsername()+".jpeg");
        if (Files.exists(filePath)) {
            try {
                dto.setOwnerProfileImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            dto.setOwnerProfileImageUrl("");
        }
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

}

