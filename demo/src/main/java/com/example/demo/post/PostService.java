package com.example.demo.post;

import com.example.demo.post.like.LikesService;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {


    private final UserService userService;
    private final LikesService likesService;

    public PostService(UserService userService, LikesService likesService) {
        this.userService = userService;
        this.likesService = likesService;
    }

    public List<PostResponse> filterAvailablePosts(List<Post> posts, User user){
        return posts.stream()
                .filter(post -> post.isOpened() || userService.isFollowingEachOther(user.getUsername(), post.getUser().getUsername()) || Objects.equals(user.getUsername(), post.getUser().getUsername()))
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
    }
}
