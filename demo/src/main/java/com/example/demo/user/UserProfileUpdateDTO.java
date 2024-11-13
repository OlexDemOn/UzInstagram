package com.example.demo.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserProfileUpdateDTO {

    private String username;
    private String fullName;
    private String bio;
    private MultipartFile profileImg;

    public UserProfileUpdateDTO(String username, String fullName, String bio, MultipartFile profileImg) {
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profileImg = profileImg;
    }
}
