package com.jabibim.admin.service;

import com.jabibim.admin.func.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3FileServiceImpl implements S3FileService {
    private final S3Uploader s3Uploader;

    public S3FileServiceImpl(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Override
    public String uploadFile(MultipartFile file, String dirName) {
        return s3Uploader.uploadFileToS3(file, dirName);
    }
}
