package com.jabibim.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface FFmpegService {
    void encoding(String uploadPathPrefix, MultipartFile file);
}
