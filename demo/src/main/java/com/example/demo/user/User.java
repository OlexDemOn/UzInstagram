package com.example.demo.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"}),
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password_hash;

    @Setter
    @Column(nullable = false)
    private String full_name;
    @Column(nullable = false)
    private String bio;
    @Column(nullable = false)
    private String profile_image_url;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password_hash = password;
        this.full_name = "";
        this.bio = "";
        this.profile_image_url = "";
    }

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
