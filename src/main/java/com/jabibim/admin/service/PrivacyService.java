package com.jabibim.admin.service;

import com.jabibim.admin.domain.Privacy;

import java.util.List;

public interface PrivacyService {
    // 글의 갯수 구하기
    public int getListCount();

    // 글 목록 보기
    public List<Privacy> getPrivacyList(int page, int limit);

    public void insertPrivacy(Privacy privacy);

    Privacy getLatestPrivacyPolicy();
}