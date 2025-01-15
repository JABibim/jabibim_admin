package com.jabibim.admin.service;

import com.jabibim.admin.domain.Qna;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface QnaService {

    int getListCount(String academyId);

    List<Qna> getQnaList(int page, int limit, String academyId);

    void setReadCountUpdate(String id);

    Qna getDetail(String id);

    Qna getPreData(String qnaId, String academyId);

    Qna getNextData(String qnaId, String academyId);

    default public String saveUploadedFile(MultipartFile uploadFile, String saveFolder) throws Exception {
        String originalFileName = uploadFile.getOriginalFilename();
        String fileDBName = fileDBName(originalFileName, saveFolder);

        System.out.println("🍤 saveUploadedFile() called!");
        System.out.println("===============================================> uploadFile : " + uploadFile);
        System.out.println("===============================================> saveFolder : " + saveFolder);
        System.out.println("===============================================> originalFileName : " + originalFileName);
        System.out.println("===============================================> fileDBName : " + fileDBName);

        // 파일 저장
        uploadFile.transferTo(new File(saveFolder + fileDBName));

        return fileDBName;
    }

    default public String fileDBName(String fileName, String saveFolder) {
        String dateFolder = createFolderByDate(saveFolder);
        String fileExtension = getFileExtension(fileName);
        String reFileName = generateUniqueFileName(fileExtension);

        return File.separator + dateFolder + File.separator + reFileName;
    }

    default public int[] getCurrentDate() {
        LocalDate now = LocalDate.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int date = now.getDayOfMonth();

        return new int[]{year, month, date};
    }

    default public String createFolderByDate(String baseFolder) {
        int[] currentDate = getCurrentDate();
        int year = currentDate[0];
        int month = currentDate[1];
        int date = currentDate[2];

        String dateFolder = year + "-" + month + "-" + date;
        String fullFolderPath = baseFolder + File.separator + dateFolder;
        File path = new File(fullFolderPath);

        if (!path.exists()) {
            path.mkdirs();
        }

        return dateFolder;
    }

    default public String getFileExtension(String fileName) {
        String extension = "";

        int lastCommaIdx = fileName.lastIndexOf(".");
        extension = fileName.substring(lastCommaIdx + 1);

        return extension;
    }

    default public String generateUniqueFileName(String fileExtension) {
        int[] currentDate = getCurrentDate();
        int year = currentDate[0];
        int month = currentDate[1];
        int date = currentDate[2];

        Random r = new Random();
        int random = r.nextInt(100000000);

        return "bbs" + year + month + date + random + "." + fileExtension;
    }

    public int replyQna(Qna qna);

    public void answerQna(String qnaId);

    public Optional<Qna> getQnaById(String detailId);

    public Qna getUpData(String id);

    public boolean isBoardWriter(String qnaId, String qnaPassword);

    public int qnaModify(Qna qnaData);

    public int qnaDelete(String updateId);

    public void qnaAnswerStat(String qnaId);
}