package com.example.demo.user;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"}),
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public Long getId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password_hash;
    }

    public void setPassword(String password) {
        this.password_hash = password;
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
