package com.jabibim.admin.service;

import com.jabibim.admin.domain.Term;

import java.util.List;

public interface TermService {

    // 글의 갯수 구하기
    public int getListCount(String academyId);

    // 글 목록 보기
    public List<Term> getTermList(int page, int limit, String academyId);

    public void insertTerm(Term term);

    public Term getLatestTermPolicy(String academyId);

    Term getDetail(int rnum);

    int getMaxRnum(String academyId);
}