package com.javarush.kuzin.caesarcipher.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
class FileManager {
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    @GetMapping("/download/{filename}")
    public byte[] downloadFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, filename);
        return Files.readAllBytes(path);
    }
}
