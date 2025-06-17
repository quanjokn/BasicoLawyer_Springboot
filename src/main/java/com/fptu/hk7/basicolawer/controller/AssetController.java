package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@SecurityRequirement(name = "bearerAuth")
public class AssetController {

//    @PreAuthorize("hasRole(ADMIN)")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .status(false)
                            .message("File name is empty")
                            .code(400)
                            .build()
            );
        } else {
            String hashFileName = UUID.randomUUID() + "_" + fileName;
            String encodedFilename = URLEncoder.encode(hashFileName, StandardCharsets.UTF_8);
            Path uploadFile = Path.of("uploads", encodedFilename);
            Files.createDirectories(uploadFile.getParent());
            Files.copy(file.getInputStream(), uploadFile);

            return ResponseEntity.ok().body(
                    ApiResponse.<String>builder()
                            .status(true)
                            .message("File uploaded successfully")
                            .result("http://localhost:8080/uploads/" + encodedFilename)
                            .code(200)
                            .build()
            );
        }
    }
}
