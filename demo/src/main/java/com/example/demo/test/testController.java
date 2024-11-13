package com.example.demo.test;

import com.example.demo.dto.ApiResponse;
import com.example.demo.user.*;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class testController {
    String uploadDir = "uploads/";
    
}