package com.jabibim.admin.service;

import com.jabibim.admin.func.FFmpegUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class FFmpegServiceImpl implements FFmpegService {
    private final FFmpegUtil ffmpegUtil;

    public FFmpegServiceImpl(FFmpegUtil ffmpegUtil) {
        this.ffmpegUtil = ffmpegUtil;
    }

    @Override
    public CompletableFuture<String> encoding(String uploadPathPrefix, MultipartFile file, String classFileId) {
        // 파일 인코딩을 위한 전처리 작업으로 원본 파일을 임시파일로 저장 ( 서버 내에 저장됨, s3가 아님 )
        String saveDirectory = System.getProperty("user.dir") + File.separator
                               + "src" + File.separator
                               + "main" + File.separator
                               + "resources" + File.separator
                               + "static" + File.separator
                               + "temp" + File.separator
                               + "raw";
        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("file 또는 파일 이름이 null입니다.");
        }
        String fileName = file.getOriginalFilename();
        File savedFile = new File(saveDirectory + "/" + fileName);
        try {
            file.transferTo(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 파일을 인코딩하고 S3에 업로드
        return ffmpegUtil.createM3U8Stream(savedFile.getAbsolutePath(), classFileId)
                .thenCompose(v -> ffmpegUtil.uploadEncodedFilesToS3(uploadPathPrefix, classFileId));
    }
}
