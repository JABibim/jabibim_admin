package com.jabibim.admin.service;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import com.jabibim.admin.mybatis.mapper.BoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private BoardMapper dao;

    public BoardServiceImpl(BoardMapper dao) {
        this.dao = dao;
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
    public List<CourseListDTO> getCourseList(){
        HashMap<String, Object> map = new HashMap<>();

        return dao.getCourseList(map);
    }

    @Override
    public void insertNotice(Board notice) {
        System.out.println("Service layer - boardExposureStat: " + notice.getBoardExposureStat());
        dao.insertNotice(notice);
    }

    @Override
    public String saveUploadedFile(MultipartFile uploadFile, String saveFolder) throws Exception {
        return BoardService.super.saveUploadedFile(uploadFile, saveFolder);
    }

    @Override
    public Board getDetail(String id){
        return dao.getDetail(id);
    }

    @Override
    public Board getPreData(int rnum) {
        return dao.getPreData(rnum);
    }

    @Override
    public Board getNextData(int rnum) {
        return dao.getNextData(rnum);
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


}