package com.example.springWebMvc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String save(MultipartFile multipartFile);
    boolean delete(String filename) throws IOException;
}
