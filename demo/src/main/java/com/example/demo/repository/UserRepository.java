// src/main/java/com/example/userapi/repository/UserRepository.java

package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // Method to find by either username or email
    Optional<User> findByUsernameOrEmail(String username, String email);
}
