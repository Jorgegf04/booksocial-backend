package com.example.booksocial_backend.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    private Path uploadDir;

    @Value("${upload.dir:uploads}")
    private String uploadDirPath;

    @Value("${upload.base-url:http://localhost:9999}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads: " + uploadDir, e);
        }
    }

    public String store(MultipartFile file) {
        String rawName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String original = StringUtils.cleanPath(rawName);
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
        String filename = UUID.randomUUID() + ext;

        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }

        return baseUrl + "/api/uploads/" + filename;
    }
}
