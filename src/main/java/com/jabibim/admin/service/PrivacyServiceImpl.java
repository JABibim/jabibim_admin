package com.jabibim.admin.service;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.mybatis.mapper.PrivacyMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PrivacyServiceImpl implements PrivacyService {
    private PrivacyMapper dao;

    public PrivacyServiceImpl(PrivacyMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount(String academyId) {

        return dao.getListCount(academyId);
    }

    @Override
    public List<Privacy> getPrivacyList(int page, int limit, String academyId) {
       // double i =1/0;
        HashMap<String, Object> map = new HashMap<>();

        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        map.put("start", startRow);
        map.put("end", endRow);
        map.put("academyId", academyId);

        return dao.getPrivacyList(map);
    }

    @Override
    public void insertPrivacy(Privacy privacy) {
        dao.insertPrivacy(privacy);
    }

    @Override
    public Privacy getLatestPrivacyPolicy(String academyId) {
        // 최신 본문을 데이터베이스에서 가져옴
        return dao.getLatestPrivacyPolicy(academyId);
    }

    @Override
    public Privacy getDetail(int rnum) {
        return dao.getDetail(rnum);
    }

    @Override
    public int getMaxRnum(String academyId) {
        return dao.getMaxRnum(academyId);
    }

}