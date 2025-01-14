package com.jabibim.admin.service;

import com.jabibim.admin.domain.Qna;
import com.jabibim.admin.mybatis.mapper.QnaMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Service
public class QnaServiceImpl implements QnaService {
    private QnaMapper dao;

    public QnaServiceImpl(QnaMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount(String academyId) {

        return dao.getListCount(academyId);
    }

    @Override
    public List<Qna> getQnaList(int page, int limit, String academyId) {
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
        return dao.getQnaList(map);
    }

}