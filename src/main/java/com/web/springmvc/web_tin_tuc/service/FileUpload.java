package com.web.springmvc.web_tin_tuc.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUpload {
    private String UPLOAD_FOLDER = "C:\\Users\\Admin\\Desktop\\Web_tin_tuc\\src\\main\\resources\\static\\assets\\images";
    public boolean uploadImage(MultipartFile multipartFile, String id) {
        String uploadFolder = UPLOAD_FOLDER+"\\"+id;
        Path uploadPath = Paths.get(uploadFolder);
        if(!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        boolean isUpload = false;
        try {
            Files.copy(multipartFile.getInputStream(),
                    Paths.get(uploadFolder+ File.separator,
                    multipartFile.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }
}
