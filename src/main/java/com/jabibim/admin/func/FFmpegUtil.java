package com.jabibim.admin.func;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@EnableAsync
@Component
@RequiredArgsConstructor
public class FFmpegUtil {
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + File.separator
                                             + "src" + File.separator
                                             + "main" + File.separator
                                             + "resources" + File.separator
                                             + "static" + File.separator
                                             + "temp" + File.separator
                                             + "encode" + File.separator;
    private final FFmpeg ffmpeg;

    private final FFprobe ffprobe;

    @Autowired
    private AmazonS3Client amazonS3Client;

    public FFmpegProbeResult getProbeResult(String filePath) {
        FFmpegProbeResult ffmpegProbeResult;

        try {
            ffmpegProbeResult = ffprobe.probe(filePath);

            System.out.println("비트레이트 : " + ffmpegProbeResult.getStreams().get(0).bit_rate);
            System.out.println("채널 : " + ffmpegProbeResult.getStreams().get(0).channels);
            System.out.println("코덱 명 : " + ffmpegProbeResult.getStreams().get(0).codec_name);
            System.out.println("코덱 유형 : " + ffmpegProbeResult.getStreams().get(0).codec_type);
            System.out.println("해상도(너비) : " + ffmpegProbeResult.getStreams().get(0).width);
            System.out.println("해상도(높이) : " + ffmpegProbeResult.getStreams().get(0).height);
            System.out.println("포맷(확장자) : " + ffmpegProbeResult.getFormat());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ffmpegProbeResult;
    }

    @Async
    public CompletableFuture<Void> createM3U8Stream(String filePath, String classFileId) {
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filePath) // 원본 파일
                .overrideOutputFiles(true)
                .addOutput(OUTPUT_DIR + classFileId + ".m3u8")
                .setFormat("hls")
                .addExtraArgs("-codec", "copy")
                .addExtraArgs("-crf", "28")
                .addExtraArgs("-hls_time", "10")
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-threads", "2")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // FFmpeg 실행 후 완료될 때까지 대기
        executor.createJob(builder).run();

        new File(filePath).delete();

        return CompletableFuture.completedFuture(null); // 작업 완료 후 반환
    }

    @Async
    public CompletableFuture<String> uploadEncodedFilesToS3(String uploadPathPrefix, String classFileId) {
        File[] files = new File(OUTPUT_DIR).listFiles();
        assert files != null;
        for (File f : files) {
            amazonS3Client.putObject(new PutObjectRequest(
                    "jabibimbucket"
                    , uploadPathPrefix + File.separator + f.getName()
                    , f)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            if (f.exists() && !f.getName().contains("README")) {
                f.delete();
            }
        }

        // ==> uploadPathPrefix : cd1918cc-820d-4ac8-be7c-c46a5f943047/course/ba6ceecb-d7e6-40c9-87fa-fe00b343fde7/class/1d1b1098-928f-4ccf-b144-5a75b51e84a0/classFile/72b5fc7d-fd65-4766-a97b-a64d71b520e5
        String m3u8Path = uploadPathPrefix + File.separator + classFileId + ".m3u8";
        String path = amazonS3Client.getUrl("jabibimbucket", m3u8Path).toString();

        return CompletableFuture.completedFuture(path); // 작업 완료 후 반환
    }
}

/**
 * FFmpeg 명령어 빌드
 * -i : 입력 파일 경로
 * -codec copy : 비디오 및 오디오 스트림을 복사
 * -hls_time 10 : HLS 세그먼트 길이를 10초로 설정
 * -hls_list_size 0 : m3u8 파일에 세그먼트 목록을 모두 포함
 * -threads 2 : 사용할 스레드 수 (0은 시스템 기본값 사용)
 * - crf 28 : 비디오 품질을 제어하는 옵션 (낮은 값일수록 높은 품질, 28정도는 품질을 유지하며 성능을 개선할 수 있다고함.)
 * %s : 출력 파일 경로
 */
//        String command = String.format("ffmpeg -i %s -codec copy -crf 28 -hls_time 10 -hls_list_size 0 -threads 2 %s",
//                inputFilePath, outputPath + "output.m3u8");
//        try {
//            // FFmpeg 명령어 실행
//            Process process = new ProcessBuilder(command.split(" ")).start();
//
//            // 프로세스 출력 스트림 읽기 (표준 출력 및 오류 출력)
//            new Thread(() -> {
//                try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 표준 출력 로그
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            // 프로세스 오류 스트림 읽기
//            new Thread(() -> {
//                try (var reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        System.err.println(line);  // 오류 출력 로그
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            // 프로세스 종료 대기
//            process.waitFor();
//
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException("Error during M3U8 stream creation", e);
//        }