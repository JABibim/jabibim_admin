package com.jabibim.admin.func;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
public class S3Uploader {
    @Autowired
    private AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile multipartFile, String filePath) {
        System.out.println("🚀 multipartFile = " + multipartFile);

        File uploadFile = null;
//        try {
//            uploadFile = convert(munltipartFile)
//                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//        } catch (IOException ioe ) {
//            throw new RuntimeException("파일 변환 중 오류가 발생했습니다.");
//        }

        return "";
    }
}
