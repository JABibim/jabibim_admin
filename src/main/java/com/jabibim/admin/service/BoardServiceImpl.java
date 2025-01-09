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
    public List<CourseListDTO> getCourseList(){
        HashMap<String, Object> map = new HashMap<>();

        return dao.getCourseList(map);
    }

    @Override
    public void insertNotice(Board notice) {
        dao.insertNotice(notice);
    }

    @Override
    public String saveUploadedFile(MultipartFile uploadFile, String saveFolder) throws Exception {
        return BoardService.super.saveUploadedFile(uploadFile, saveFolder);
    }


}