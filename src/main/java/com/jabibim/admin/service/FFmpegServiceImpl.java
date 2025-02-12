package com.jabibim.admin.service;

import com.jabibim.admin.func.FFmpegUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FFmpegServiceImpl implements FFmpegService {
    private final FFmpegUtil ffmpegUtil;

    public FFmpegServiceImpl(FFmpegUtil ffmpegUtil) {
        this.ffmpegUtil = ffmpegUtil;
    }

    @Override
    public void encoding(MultipartFile file) {
        // 임시 파일을 저장할 경로
        String saveDirectory = System.getProperty("user.dir") + File.separator
                               + "src" + File.separator
                               + "main" + File.separator
                               + "resources" + File.separator
                               + "static" + File.separator
                               + "temp" + File.separator;
        String fileName = file.getOriginalFilename();
        File savedFile = new File(saveDirectory + "/" + fileName);
        try {
            file.transferTo(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 파일을 인코딩하고 S3에 업로드
        ffmpegUtil.createM3U8Stream(savedFile.getAbsolutePath())
                .thenCompose(v -> ffmpegUtil.uploadEncodedFilesToS3());
    }
}
