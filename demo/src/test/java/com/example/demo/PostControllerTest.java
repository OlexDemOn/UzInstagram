package com.example.demo;

import com.example.demo.dto.ApiResponse;
import com.example.demo.imgHandle.SaveImage;
import com.example.demo.post.Post;
import com.example.demo.post.PostController;
import com.example.demo.post.PostRepository;
import com.example.demo.post.PostResponse;
import com.example.demo.post.like.LikesService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private LikesService likesService;

    @Test
    public void testGetPosts() throws IOException {
        // Arrange
        String username = "testUser";
        int page = 0;
        int pageSize = 5;
        String avatarPath = "uploads/avatar/testUser/avatar-testUser.jpeg";
        String postImagePath = "uploads/post/1/post-1.jpeg";

        User mockUser = new User();
        mockUser.setUsername(username);

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setTitle("Sample Post");
        mockPost.setCreatedAt(new Date());
        mockPost.setUser(mockUser);
        mockPost.setOpened(true);
        mockPost.setDescription("Sample Description");

        Page<Post> mockPage = new PageImpl<>(List.of(mockPost), PageRequest.of(page, pageSize), 1);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, pageSize, Sort.by("createdAt").descending())))
                .thenReturn(mockPage);
        Mockito.when(likesService.getLikeCount(mockPost)).thenReturn(10);
        Mockito.when(likesService.isPostLikedByUser(mockPost, mockUser)).thenReturn(true);

        Files.createDirectories(Paths.get("uploads/avatar/testUser"));
        Files.createDirectories(Paths.get("uploads/post/1"));
        Files.write(Paths.get(avatarPath), "mockAvatarData".getBytes());
        Files.write(Paths.get(postImagePath), "mockPostData".getBytes());

        // Act
        ResponseEntity<ApiResponse> response = postController.getPosts(page, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse apiResponse = response.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("User profile fetched successfully", apiResponse.getMessage());

        Map<String, Object> responseBody = (Map<String, Object>) apiResponse.getData();
        List<PostResponse> posts = (List<PostResponse>) responseBody.get("posts");
        assertFalse(posts.isEmpty());
        PostResponse postResponse = posts.get(0);

        assertEquals("Sample Post", postResponse.getTitle());
        assertEquals("Sample Description", postResponse.getDescription());
        assertEquals(10, postResponse.getLikesCount());
        assertTrue(postResponse.isLikedByCurrentUser());
        assertTrue(postResponse.isOpened());
        assertEquals("data:image/jpeg;base64," + Base64.getEncoder().encodeToString("mockPostData".getBytes()), postResponse.getImageUrl());
        assertEquals("data:image/jpeg;base64," + Base64.getEncoder().encodeToString("mockAvatarData".getBytes()), postResponse.getProfileImageUrl());

        boolean hasMorePosts = (boolean) responseBody.get("hasMorePosts");
        assertFalse(hasMorePosts);

        // Clean up
        Files.deleteIfExists(Paths.get(avatarPath));
        Files.deleteIfExists(Paths.get(postImagePath));
        Files.deleteIfExists(Paths.get("uploads/avatar/testUser"));
        Files.deleteIfExists(Paths.get("uploads/post/1"));
    }

    @Test
    public void testLikePost() {
        // Arrange
        Long postId = 1L;
        String username = "testUser";

        User mockUser = new User();
        mockUser.setUsername(username);

        Post mockPost = new Post();
        mockPost.setId(postId);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(postRepository.findPostById(postId)).thenReturn(mockPost);

        // Act
        ResponseEntity<ApiResponse> response = postController.likePost(postId, username);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post liked", Objects.requireNonNull(response.getBody()).getMessage());

        Mockito.verify(likesService).likePost(mockPost, mockUser);
    }

    @Test
    public void testUnlikePost() {
        // Arrange
        Long postId = 1L;
        String username = "testUser";

        User mockUser = new User();
        mockUser.setUsername(username);

        Post mockPost = new Post();
        mockPost.setId(postId);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(postRepository.findPostById(postId)).thenReturn(mockPost);

        // Act
        ResponseEntity<ApiResponse> response = postController.unlikePost(postId, username);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post unliked", Objects.requireNonNull(response.getBody()).getMessage());

        Mockito.verify(likesService).unlikePost(mockPost, mockUser);
    }

    @Test
    public void testCreatePost_Success() throws IOException {
        // Arrange
        String username = "testUser";
        String description = "Test Description";
        String title = "Test Title";
        Boolean postType = true;

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImage.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockImage.getBytes()).thenReturn("mockImageBytes".getBytes());

        User mockUser = new User();
        mockUser.setUsername(username);

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setDescription(description);
        mockPost.setTitle(title);
        mockPost.setUser(mockUser);
        mockPost.setOpened(postType);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<SaveImage> mockedSaveImage = Mockito.mockStatic(SaveImage.class)) {
            mockedSaveImage.when(() -> SaveImage.savePostImage(mockImage, mockPost.getId()))
                    .thenReturn("mock/path/to/image.jpg");

            // Act
            ResponseEntity<ApiResponse> response = postController.createPost(username, description, title, postType, mockImage);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("User profile fetched successfully", response.getBody().getMessage());

            Post savedPost = (Post) response.getBody().getData();
            assertEquals("mock/path/to/image.jpg", savedPost.getImageUrl());

            // Verify interactions
            Mockito.verify(userRepository).findByUsername(username);
            Mockito.verify(postRepository, Mockito.times(2)).save(Mockito.any(Post.class));
        }
    }


    @Test
    public void testCreatePost_InvalidImageFormat() throws IOException {
        // Arrange
        String username = "testUser";
        String description = "Test Description";
        String title = "Test Title";
        Boolean postType = true;

        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        Mockito.when(mockImage.getContentType()).thenReturn("application/pdf"); // Invalid content type

        User mockUser = new User();
        mockUser.setUsername(username);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<ApiResponse> response = postController.createPost(username, description, title, postType, mockImage);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Only JPEG or PNG images are allowed", response.getBody().getMessage());

        // Verify interactions
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(postRepository, Mockito.never()).save(Mockito.any(Post.class));
    }

}
