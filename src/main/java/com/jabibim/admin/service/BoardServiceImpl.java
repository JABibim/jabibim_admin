package com.jabibim.admin.service;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import com.jabibim.admin.func.S3Uploader;
import com.jabibim.admin.mybatis.mapper.BoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

import static com.jabibim.admin.func.Files.getExtension;

@Service
public class BoardServiceImpl implements BoardService {
    private BoardMapper dao;
    private final S3Uploader s3Uploader;

    public BoardServiceImpl(BoardMapper dao, S3Uploader s3Uploader) {
        this.dao = dao;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public int getListCount(String academyId) {
        return dao.getListCount(academyId);
    }

    @Override
    public List<Board> getNoticeList(int page, int limit, String academyId) {
        // Map 객체 선언
        HashMap<String, Object> map = new HashMap<>();

        // 페이징 처리: 시작 행과 끝 행 계산
        int startRow = (page - 1) * limit;
        int endRow = limit;

        // Map에 값 추가
        map.put("start", startRow);
        map.put("end", endRow);
        map.put("academyId", academyId);

        // DAO 호출 및 결과 반환
        return dao.getNoticeList(map);
    }

    @Override
    public List<CourseListDTO> getCourseList() {
        HashMap<String, Object> map = new HashMap<>();

        return dao.getCourseList(map);
    }

    @Override
    public void insertNotice(Board notice) {
        MultipartFile uploadFile = notice.getUploadfile();
        if (!uploadFile.isEmpty()) {
            if (uploadFile.getOriginalFilename() == null) {
                throw new IllegalArgumentException("업로드하려는 파일의 이름이 null입니다.");
            }

            String fileName = "notice." + getExtension(uploadFile.getOriginalFilename());
            String filePath = notice.getAcademyId() + "/notice/" + notice.getBoardId() + "/" + fileName;
            String uploadedPath = s3Uploader.uploadFileToS3(uploadFile, filePath);

            notice.setBoardFileOriginName(uploadFile.getOriginalFilename());
            notice.setBoardFilePath(uploadedPath);
        }

        dao.insertNotice(notice);
    }

    @Override
    public Board getDetail(String id) {
        return dao.getDetail(id);
    }

    @Override
    public Board getPreData(int rnum, String academyId) {
        return dao.getPreData(rnum, academyId);
    }

    @Override
    public Board getNextData(int rnum, String academyId) {
        return dao.getNextData(rnum,academyId);
    }

    @Override
    public void setReadCountUpdate(String id) {
        dao.setReadCountUpdate(id);
    }

    @Override
    public boolean isBoardWriter(String boardId, String boardPassword) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", boardId);
        map.put("pass", boardPassword);

        Board result = dao.isBoardWriter(map);

        return result != null;
    }

    @Override
    public int boardModify(Board noticeData) {
        return dao.boardModify(noticeData);
    }

    @Override
    public int boardDelete(String boardId) {
        int result = 0;
        Board notice = dao.getDetail(boardId);
        if (notice != null) {
            result = dao.boardDelete(notice);
        }

        return result;
    }

    @Override
    public String changeFile(MultipartFile uploadFile, String boardId, String academyId) {
        Board boardDetail = this.getDetail(boardId);
        if (boardDetail == null) {
            throw new IllegalArgumentException("해당 공지사항이 존재하지 않습니다.");
        }
        String newFilePath = "";
        try {
            String oldFilePath = boardDetail.getBoardFilePath();

            // s3에 있던 기존 파일 제거
            if (oldFilePath != null && !oldFilePath.isEmpty()) {
                s3Uploader.deleteFileFromS3(oldFilePath);
            }

            // s3에 새로운 파일 업로드
            if (uploadFile.getOriginalFilename() == null) {
                throw new IllegalArgumentException("업로드하려는 파일의 이름이 null입니다.");
            }

            String fileName = "notice." + getExtension(uploadFile.getOriginalFilename());
            String filePath = academyId + "/notice/" + boardDetail.getBoardId() + "/" + fileName;

            newFilePath = s3Uploader.uploadFileToS3(uploadFile, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    @Override
    public void deleteBoardFile(String boardId) {
        Board notice = dao.getDetail(boardId);
        if (notice != null) {
            String filePath = notice.getBoardFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                s3Uploader.deleteFileFromS3(filePath);
            }
        }
    }
}