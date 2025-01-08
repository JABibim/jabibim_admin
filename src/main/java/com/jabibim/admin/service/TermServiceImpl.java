package com.jabibim.admin.service;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.domain.Term;
import com.jabibim.admin.mybatis.mapper.TermMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TermServiceImpl implements TermService {
    private TermMapper dao;

    public TermServiceImpl(TermMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount() {
        return dao.getListCount();
    }

    @Override
    public List<Term> getTermList(int page, int limit) {
        // double i =1/0;
        HashMap<String, Integer> map = new HashMap<>();

        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        map.put("start", startRow);
        map.put("end", endRow);

        return dao.getTermList(map);
    }

    @Override
    public void insertTerm(Term term) {
        dao.insertTerm(term);
    }

    @Override
    public Term getLatestTermPolicy() {
        // 최신 본문을 데이터베이스에서 가져옴
        return dao.getLatestTermPolicy();
    }

    @Override
    public Term getDetail(int rnum) {
        return dao.getDetail(rnum);
    }

    @Override
    public int getMaxRnum() {
        return dao.getMaxRnum();
    }


}