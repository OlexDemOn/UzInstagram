package com.example.demo.user;

import org.springframework.core.io.Resource;

public class UserProfileDTO {
    private String username;
    private String full_name;
    private String profile_image;
    private String bio;


    public String getUsername() {
        return username;
    }

    public String getFull_name() {
        return full_name;
    }


    public String getBio() {
        return bio;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public UserProfileDTO(String username, String full_name, String profile_image, String bio) {
        this.username = username;
        this.full_name = full_name;
        this.profile_image = profile_image;
        this.bio = bio;
    }
}
