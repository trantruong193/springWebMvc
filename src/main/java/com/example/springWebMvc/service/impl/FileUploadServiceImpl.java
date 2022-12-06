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
    @Autowired
    public FileUploadServiceImpl(ServletContext servletContext){
        try {
            String path = servletContext.getRealPath("/WEB-INF/static/img");
            storageFolder = Paths.get(path);
            if(!Files.exists(storageFolder))
                Files.createDirectories(storageFolder);
        }catch (IOException ioException){
            throw new RuntimeException("Cannot initialize storage" + ioException);
        }
    }
    @Override
    public String save(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = storageFolder.resolve(multipartFile.getOriginalFilename());
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            return multipartFile.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file" + e);
        }
    }
    @Override
    public boolean delete(String filename) {
        Path filePath = storageFolder.resolve(filename);
        try {
            Files.deleteIfExists(filePath);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
