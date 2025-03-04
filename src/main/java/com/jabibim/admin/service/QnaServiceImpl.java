package com.jabibim.admin.service;

import com.jabibim.admin.domain.Qna;
import com.jabibim.admin.mybatis.mapper.QnaMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void setReadCountUpdate(String id) {
        dao.setReadCountUpdate(id);
    }

    @Override
    public Qna getDetail(String id) {
        return dao.getDetail(id);
    }

    @Override
    public Qna getPreData(String qnaId, String academyId) {
        return dao.getPreData(qnaId, academyId);
    }

    @Override
    public Qna getNextData(String qnaId, String academyId) {
        return dao.getNextData(qnaId, academyId);
    }

    @Override
    public String saveUploadedFile(MultipartFile uploadFile, String saveFolder) throws Exception {
        return QnaService.super.saveUploadedFile(uploadFile, saveFolder);
    }

    @Override
    public int replyQna(Qna qna) {
            return dao.replyQna(qna);
    }

    @Override
    public void answerQna(String qnaId) {
        dao.answerQna(qnaId);
    }

    @Override
    public Optional<Qna> getQnaById(String detailId) {
        return dao.getQnaById(detailId);
    }

    @Override
    public Qna getUpData(String id) {
        return dao.getUpData(id);
    }

    @Override
    public boolean isBoardWriter(String qnaId, String qnaPassword) {
        HashMap<String, Object> map = new HashMap<>();


        map.put("id", qnaId);
        map.put("pass", qnaPassword);

        Qna result = dao.isBoardWriter(map);

        return result != null;
    }

    @Override
    public int qnaModify(Qna qnaData) {
        return dao.qnaModify(qnaData);
    }

    @Override
    public int qnaDelete(String updateId) {

        return dao.qnaDelete(updateId);
    }

    @Override
    public void qnaAnswerStat(String qnaId) {
        dao.qnaAnswerStat(qnaId);

    }

}