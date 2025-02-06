package com.jabibim.admin.service;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface BoardService {

    int getListCount(String academyId);

    List<Board> getNoticeList(int page, int limit, String academyId);

    public List<CourseListDTO> getCourseList();

    default public int[] getCurrentDate() {
        LocalDate now = LocalDate.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int date = now.getDayOfMonth();

        return new int[]{year, month, date};
    }

    public void insertNotice(Board notice);

    public Board getDetail(String id);

    public Board getPreData(int rnum);

    public Board getNextData(int rnum);

    public void setReadCountUpdate(String id);

    public boolean isBoardWriter(String boardId, String boardPassword);

    public int boardModify(Board noticeData);

    int boardDelete(String boardId);

    String changeFile(MultipartFile uploadFile, String boardId, String academyId);

    void deleteBoardFile(String boardId);
}