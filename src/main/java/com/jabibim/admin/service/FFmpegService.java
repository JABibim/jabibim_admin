package com.jabibim.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface FFmpegService {
    CompletableFuture<String> encoding(String uploadPathPrefix, MultipartFile file, String classFileId);
}
