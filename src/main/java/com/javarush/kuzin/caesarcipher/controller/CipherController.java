package com.javarush.kuzin.caesarcipher.controller;

import com.javarush.kuzin.caesarcipher.CeasarCipher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CipherController {

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String index(Model model) {
        File uploadDir = new File(UPLOAD_DIR);
        File[] files = uploadDir.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        model.addAttribute("files", fileNames);
        return "index";
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestParam String text, @RequestParam int shift, @RequestParam String language, Model model) {
        String encryptedText = CeasarCipher.encrypt(text, shift, language);
        model.addAttribute("result", encryptedText);
        return "result";
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestParam String text, @RequestParam int shift, @RequestParam String language, Model model) {
        String decryptedText = CeasarCipher.decrypt(text, shift, language);
        model.addAttribute("result", decryptedText);
        return "result";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam int shift, @RequestParam String language, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "index";
        }

        String fileName = file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        String content = new String(bytes);
        String encryptedContent = CeasarCipher.encrypt(content, shift, language);

        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, encryptedContent.getBytes());

        model.addAttribute("message", "File uploaded and encrypted successfully: " + fileName);
        return index(model);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String fileName) throws IOException {
        File file = new File(UPLOAD_DIR + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/frequency-analysis")
    public String frequencyAnalysis(@RequestParam String text, @RequestParam String language, Model model) throws IOException {
        String decryptedText = CeasarCipher.frequencyAnalysisDecrypt(text, language);
        model.addAttribute("result", decryptedText);

        // Сохранение результата в файл
        String fileName = "decrypted_result.txt";
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, decryptedText.getBytes());

        model.addAttribute("downloadFile", fileName);
        return "result";
    }
}