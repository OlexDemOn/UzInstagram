package com.example.demo.post.like;


import com.example.demo.post.Post;
import com.example.demo.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikesService {

    private final LikesRepository likeRepository;
    private final LikesRepository likesRepository;

    public LikesService(LikesRepository likeRepository, LikesRepository likesRepository) {
        this.likeRepository = likeRepository;
        this.likesRepository = likesRepository;
    }

    public void likePost(Post post, User user) {
        Optional<Likes> existingLike = likeRepository.findByPostAndUser(post, user);
        if (existingLike.isEmpty()) {
            Likes like = new Likes();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    public void unlikePost(Post post, User user) {
        likeRepository.findByPostAndUser(post, user)
                .ifPresent(likeRepository::delete);
    }

    public int getLikeCount(Post post) {
        return likesRepository.countByPost(post);
    }

    public boolean isPostLikedByUser(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }
}
