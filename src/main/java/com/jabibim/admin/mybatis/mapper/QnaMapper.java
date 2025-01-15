package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Qna;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Mapper
public interface QnaMapper {

    public int getListCount(String academyId);

    public List<Qna> getQnaList(HashMap<String, Object> map);

    public void setReadCountUpdate(String id);

    public Qna getDetail(String id);

    public Qna getPreData(String qnaId, String academyId);

    public Qna getNextData(String qnaId, String academyId);

    public int replyQna(Qna qna);

    public Optional<Qna> getQnaById(String qnaId);

    public void answerQna(String qnaId);

    public Qna getUpData(String id);

    public Qna isBoardWriter(HashMap<String, Object> map);

    public int qnaModify(Qna qnaData);

    public int qnaDelete(String updateId);

    public void qnaAnswerStat(String qnaId);
}