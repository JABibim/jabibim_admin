package com.jabibim.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {
    String uploadFile(MultipartFile file, String dirName);
}
