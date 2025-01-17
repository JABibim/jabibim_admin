package com.jabibim.admin.service;

import com.jabibim.admin.domain.Privacy;

import java.util.List;

public interface PrivacyService {

    // 글의 갯수 구하기
    public int getListCount(String academyId);

    // 글 목록 보기
    public List<Privacy> getPrivacyList(int page, int limit, String academyId);

    public void insertPrivacy(Privacy privacy);

    public Privacy getLatestPrivacyPolicy(String academyId);

    public Privacy getDetail(int rnum);

    public int getMaxRnum(String academyId);
}