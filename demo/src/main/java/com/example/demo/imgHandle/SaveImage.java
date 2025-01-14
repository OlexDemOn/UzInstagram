package com.example.demo.imgHandle;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;


public class SaveImage {

    static String avatarPath = "avatar";
    static String postPath = "post";

    public static String saveAvatarImage(MultipartFile file, String username) throws IOException {
        Path uploadPath =  Paths.get("uploads/" + avatarPath + "/" + username);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String newFileName = avatarPath + "-" + username + ".jpeg";
        Path newFilePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilePath.toString();
    }


    public static String savePostImage(MultipartFile file, Long postId) throws IOException {
        Path uploadPath =  Paths.get("uploads/" + postPath + "/" + postId.toString());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String newFileName = postPath + "-" + postId.toString() + ".jpeg";
        Path newFilePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilePath.toString();
    }
}
