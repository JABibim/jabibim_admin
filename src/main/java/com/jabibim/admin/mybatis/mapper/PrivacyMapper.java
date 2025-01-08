package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Privacy;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface PrivacyMapper {
    //  글의 갯수 구하기
    public int getListCount();

    public List<Privacy> getPrivacyList(HashMap<String, Integer> map);

    // 글 등록하기
    public void insertPrivacy(Privacy privacy);

    public Privacy getLatestPrivacyPolicy();

    public Privacy getDetail(int rnum);

    int getMaxRnum();
}