package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface BoardMapper {

    public int getListCount(String academyId);

    public List<Board> getNoticeList(HashMap<String, Object> map);

    public List<CourseListDTO> getCourseList(HashMap<String, Object> map);

    public void insertNotice(Board notice);

    public Board getDetail(String id);

    public Board getPreData(int rnum);

    public Board getNextData(int rnum);

    public void setReadCountUpdate(String id);

    public Board isBoardWriter(HashMap<String, Object> map);

    public int boardModify(Board noticeData);

    public int boardDelete(Board notice);
}