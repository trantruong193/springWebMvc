package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final Path storageFolder;
    private final Path customerAvatarFolder;
    @Autowired
    public FileUploadServiceImpl(ServletContext servletContext){
        try {
            // folder for product
            String path = servletContext.getRealPath("/WEB-INF/static/img");
            storageFolder = Paths.get(path);
            if(!Files.exists(storageFolder))
                Files.createDirectories(storageFolder);
            // folder for avatar
            String avatarPath = servletContext.getRealPath("/WEB-INF/static/img/avatar");
            customerAvatarFolder = Paths.get(avatarPath);
            if(!Files.exists(customerAvatarFolder))
                Files.createDirectories(customerAvatarFolder);

        }catch (IOException ioException){
            throw new RuntimeException("Cannot initialize storage" + ioException);
        }
    }
    @Override
    public String save(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = storageFolder.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            return multipartFile.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file" + e);
        }
    }
    @Override
    public void delete(String filename) {
        Path filePath = storageFolder.resolve(filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveAvatar(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = customerAvatarFolder.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            return multipartFile.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file" + e);
        }
    }

    @Override
    public void deleteAvatar(String filename) throws IOException{
        Path filePath = customerAvatarFolder.resolve(filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
