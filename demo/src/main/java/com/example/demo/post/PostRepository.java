package com.example.demo.post;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(PageRequest pageable);

    List<Post> findAllByUser(User user);
}
