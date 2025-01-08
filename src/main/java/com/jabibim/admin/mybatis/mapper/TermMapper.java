package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.domain.Term;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface TermMapper {
    //  글의 갯수 구하기
    public int getListCount();

    public List<Term> getTermList(HashMap<String, Integer> map);

    // 글 등록하기
    public void insertTerm(Term term);

    public Term getLatestTermPolicy();

}
