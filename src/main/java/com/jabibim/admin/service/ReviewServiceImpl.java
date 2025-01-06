package com.jabibim.admin.service;

import com.jabibim.admin.dto.ReviewListVO;
import com.jabibim.admin.mybatis.mapper.ReviewMapper;
import com.jabibim.admin.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

  private ReviewMapper dao;
  private SecurityUtil securityUtil;

  @Autowired
  public ReviewServiceImpl(ReviewMapper dao, SecurityUtil securityUtil) {
    this.dao = dao;
    this.securityUtil = securityUtil;
  }

  @Override
  public int getSearchListCount(HashMap<String, String> hm) {

    HashMap<String, Object> map = searchMap(hm);
//    String teacherEmail = securityUtil.getCurrentUsername();
//    String academyId = dao.getAcademyId(teacherEmail);
    map.put("academyId", "f236923c-4746-4b5a-8377-e7c5b53799c2");

    return dao.getSearchListCount(map);
  }

  @Override
  public List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit) {

    HashMap<String, Object> map = searchMap(hm);
//    String teacherEmail = securityUtil.getCurrentUsername();
//    String academyId = dao.getAcademyId(teacherEmail);
    map.put("academyId", "f236923c-4746-4b5a-8377-e7c5b53799c2");

    int startrow = (page - 1) * limit + 1;
    int endrow = startrow + limit - 1;

    map.put("start", startrow);
    map.put("end", endrow);

    List<ReviewListVO> list = dao.getSearchList(map);

    return list;
  }


  private static HashMap<String, Object> searchMap(HashMap<String, String> hm) {
    HashMap<String, Object> map = new HashMap<>();

    if (!hm.get("reviewDate1").isEmpty()) {
      map.put("reviewDate1", " TO_DATE('" + hm.get("reviewDate1") + "','YY/MM/DD') ");
    }

    if (!hm.get("reviewDate2").isEmpty()) {
      map.put("reviewDate2", " TO_DATE('" + hm.get("reviewDate2") + "','YY/MM/DD') ");
    }

    if (!hm.get("reply_status").equals("all")) {
      map.put("reply_status", hm.get("reply_status"));
    }

    if (!hm.get("rating").equals("all")) {
      map.put("rating", hm.get("rating"));
    }

    if (!hm.get("review_visible").equals("all")) {
      map.put("review_visible", hm.get("review_visible"));
    }

    if (!hm.get("search_word").isEmpty()) {
      map.put("review_searchField", hm.get("review_searchField").split(""));
      map.put("search_word", "%" + hm.get("search_word") + "%");
    }
    return map;
  }


}
