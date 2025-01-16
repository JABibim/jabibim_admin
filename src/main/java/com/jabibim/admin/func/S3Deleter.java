package com.jabibim.admin.func;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class S3Deleter {
    private static final Logger logger = LoggerFactory.getLogger(S3Deleter.class);
    private final S3AsyncClient s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public S3Deleter(S3AsyncClient s3Client) {
        this.s3Client = s3Client;
    }

    public void deleteFiles(List<String> fileList) {
        logger.info("S3 파일 삭제 >> 삭제할 파일 수 : " + fileList.size() + " files / 버킷 이름 : " + bucket);

        // 병렬 처리를 위한 CompletableFutures 생성
        List<CompletableFuture<Void>> futures = fileList.stream().map(this::deleteFileAsync).toList();

        // 모든 작업 완료를 기다림
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allFutures.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Error occurred during S3 deletion: " + ex.getMessage());
            } else {
                logger.info("All files deleted successfully!");
            }
        }).join();
    }

    private CompletableFuture<Void> deleteFileAsync(String fileKey) {
        int indexOfBucketName = fileKey.indexOf(bucket);
        String key = fileKey.substring(indexOfBucketName + bucket.length() + 1);

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();

        return s3Client.deleteObject(deleteRequest).thenRun(() -> System.out.println("Deleted file: " + key)).exceptionally(ex -> {
            logger.error("Failed to delete file: " + key + ". Error: " + ex.getMessage());
            return null;
        });
    }
}
