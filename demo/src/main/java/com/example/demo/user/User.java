package com.example.demo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The type User.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"}),
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Setter
@Getter
@Schema(description = "User data response")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username", example = "test")
    private String username;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email", example = "test@example.com")
    private String email;

    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false)
    @Schema(description = "Full name", example = "First and second name")
    private String full_name;

    @Column(nullable = false)
    private String bio;
    @Column(nullable = false)
    private String profile_image_url;

    // Many-to-Many relationship for followers and following
    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )

    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    /**
     * Instantiates a new User.
     */
    public User() {}

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param email    the email
     * @param password the password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password_hash = password;
        this.full_name = "";
        this.bio = "";
        this.profile_image_url = "";
    }

    /**
     * Instantiates a new User.
     *
     * @param username          the username
     * @param email             the email
     * @param password_hash     the password hash
     * @param full_name         the full name
     * @param bio               the bio
     * @param profile_image_url the profile image url
     */
    public User(String username, String email, String password_hash, String full_name, String bio, String profile_image_url) {
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.full_name = full_name;
        this.bio = bio;
        this.profile_image_url = profile_image_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(username, user.username) || Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
