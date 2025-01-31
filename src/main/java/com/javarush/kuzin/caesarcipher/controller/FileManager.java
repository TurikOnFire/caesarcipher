package com.javarush.kuzin.caesarcipher.controller;

import com.javarush.kuzin.caesarcipher.CeasarCipher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
class FileManager {
    private static final String UPLOAD_DIR = "uploads";
    private static final int SHIFT = 3;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
        Files.createDirectories(path.getParent());

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String encryptedContent = CeasarCipher.encrypt(content, SHIFT);

        Files.write(path, encryptedContent.getBytes(StandardCharsets.UTF_8));
        return "Файл с названием: " + file.getOriginalFilename() + " успешно загружен.";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, filename);

        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileContent = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}
