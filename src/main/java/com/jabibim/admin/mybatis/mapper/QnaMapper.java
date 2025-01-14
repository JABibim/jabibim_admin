package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Qna;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface QnaMapper {

    int getListCount(String academyId);

    List<Qna> getQnaList(HashMap<String, Object> map);

}