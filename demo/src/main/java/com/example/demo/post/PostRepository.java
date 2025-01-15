package com.example.demo.post;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(PageRequest pageable);

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    Post findPostById(Long postId);

    @Query("SELECT p FROM Post p WHERE p.id IN :ids")
    List<Post> findByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.likes l " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(l) DESC")
    List<Post> findTop10PostsByLikes(Pageable pageable);

    List<Post> findAllByOpenedOrderByCreatedAtDesc(Boolean opened);
}
