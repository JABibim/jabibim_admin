package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Privacy;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface PrivacyMapper {
    //  글의 갯수 구하기
    public int getListCount(String academyId);

    public List<Privacy> getPrivacyList(HashMap<String, Object> map);

    // 글 등록하기
    public void insertPrivacy(Privacy privacy);

    public Privacy getLatestPrivacyPolicy(String academyId);

    public Privacy getDetail(int rnum);

    public int getMaxRnum(String academyId);
}