package com.jabibim.admin.func;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
public class S3Uploader {
    private static final Logger logger = LoggerFactory.getLogger(S3Uploader.class);
    @Autowired
    private AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile multipartFile, String filePath) {
        System.out.println("📍📍uploadFileToS3() multipartFile : " + multipartFile);
        System.out.println("📍📍uploadFileToS3() filePath : " + filePath);
        File uploadFile = null;
        try {
            uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
            System.out.println("📍📍uploadFileToS3() uploadFile : " + uploadFile);
        } catch (IOException ioe) {
            throw new RuntimeException("파일 변환 중 오류가 발생했습니다.");
        }

        String uploadedImageUrl = putS3(uploadFile, filePath);
        removeNewFile(uploadFile);

        return uploadedImageUrl;
    }

    private String putS3(File uploadFile, String filePath) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new IllegalArgumentException("S3 업로드 중 오류가 발생했습니다.", e);
        }

        return amazonS3Client.getUrl(bucket, filePath).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String dirPath = System.getProperty("user.dir") + File.separator
                         + "src" + File.separator
                         + "main" + File.separator
                         + "resources" + File.separator
                         + "static" + File.separator
                         + "temp" + File.separator
                         + file.getOriginalFilename();

        File convertFile = new File(dirPath);

        System.out.println("📍📍convert() dirPath : " + dirPath);
        System.out.println("📍📍convert() convertFile : " + convertFile);

        if (convertFile.createNewFile()) {
            try (
                    FileOutputStream fos = new FileOutputStream(convertFile)
            ) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            logger.info("(local)파일 삭제 성공.");
            return;
        }
        logger.info("(local)파일이 삭제되지 않았습니다.");
    }

    public void deleteFileFromS3(String asisFilePath) {
        try {
            int indexOfBucketName = asisFilePath.indexOf(bucket);
            String key = asisFilePath.substring(indexOfBucketName + bucket.length() + 1);

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                logger.error("S3 파일 삭제 중 오류가 발생했습니다. : " + e.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("S3 파일 삭제 중 오류가 발생: " + e.getMessage());
        }
    }
}
