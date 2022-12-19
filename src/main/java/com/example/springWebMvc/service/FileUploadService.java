package com.example.springWebMvc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String save(MultipartFile multipartFile);
    void delete(String filename) throws IOException;
    String saveAvatar(MultipartFile multipartFile);
    void deleteAvatar(String filename) throws IOException;
}
