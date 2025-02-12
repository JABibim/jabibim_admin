package com.jabibim.admin.service;

import com.jabibim.admin.func.FFmpegUtil;
import com.jabibim.admin.func.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FFmpegServiceImpl implements FFmpegService {
    private final FFmpegUtil ffmpegUtil;
    private final S3Uploader s3Uploader;

    public FFmpegServiceImpl(FFmpegUtil ffmpegUtil, S3Uploader s3Uploader) {
        this.ffmpegUtil = ffmpegUtil;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public void encoding(String uploadPathPrefix, MultipartFile file) {
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

        // 원본파일도 백업용으로 s3 업로드
        s3Uploader.uploadFileToS3(file, uploadPathPrefix + File.separator + "raw" + File.separator + fileName + "." + getExtension(fileName));

        // 파일을 인코딩하고 S3에 업로드
        ffmpegUtil.createM3U8Stream(savedFile.getAbsolutePath())
                .thenCompose(v -> ffmpegUtil.uploadEncodedFilesToS3(uploadPathPrefix));
    }

    private String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1).toLowerCase();
    }
}
