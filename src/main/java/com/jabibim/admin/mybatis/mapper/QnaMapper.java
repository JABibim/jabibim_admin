package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.domain.Qna;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface QnaMapper {

    public int getListCount(String academyId);

    public List<Qna> getQnaList(HashMap<String, Object> map);

    public void setReadCountUpdate(String id);

    public Qna getDetail(String id);

    public Qna getPreData(int rnum, String academyId);

    public Qna getNextData(int rnum, String academyId);
}