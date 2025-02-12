package com.jabibim.admin.config;

import io.jsonwebtoken.lang.Assert;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFmpegConfig {
    @Value("${ffmpeg.location}")
    private String ffmpegLocation;

    @Value("${ffprobe.location}")
    private String ffprobeLocation;

    /**
     * (gpt 설명) Assert.isTrue(ffmpeg.isFFmpeg())는 FFmpeg 객체가 실제 FFmpeg 바이너리인지 확인하는 코드입니다. isFFmpeg()가 true를 반환하면 유효한 FFmpeg 바이너리로 간주하고, 그렇지 않으면 예외를 던집니다.
     * @return FFmpeg 객체
     * @throws IOException
     */
    @Bean(name="ffmpeg")
    public FFmpeg ffmpeg() throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegLocation);
        Assert.isTrue(ffmpeg.isFFmpeg());

        return ffmpeg;
    }

    @Bean(name="ffprobe")
    public FFprobe ffprobe() throws IOException {
        FFprobe ffprobe = new FFprobe(ffprobeLocation);
        Assert.isTrue(ffprobe.isFFprobe());

        return ffprobe;
    }
}
