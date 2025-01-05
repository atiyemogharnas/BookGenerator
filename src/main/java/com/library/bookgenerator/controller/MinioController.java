package com.library.bookgenerator.controller;

import com.library.bookgenerator.service.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadFileToMinio(@RequestPart(value = "file") MultipartFile file) {
        try {
            minioService.uploadFileToMinio(file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/download/{fileName}")
    public String getFileFromMinio(@PathVariable String fileName) {
        minioService.getFile(fileName);
        return "download success";

    }
}
