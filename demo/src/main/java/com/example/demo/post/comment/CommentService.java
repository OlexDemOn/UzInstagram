package com.example.demo.post.comment;

import com.example.demo.post.Post;
import com.example.demo.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForPost(Post post) {
        return commentRepository.findByPost(post);
    }



}
