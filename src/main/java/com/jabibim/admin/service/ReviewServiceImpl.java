package com.jabibim.admin.service;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.ReviewMapper;
import com.jabibim.admin.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

//    int startrow = (page - 1) * limit + 1;
//    int endrow = startrow + limit - 1;

    map.put("offset", (page - 1) * limit);  // offset
    map.put("limit", limit);         // limit

    List<ReviewListVO> list = dao.getSearchList(map);

    return list;
  }

  @Override
  public List<ReviewDetailVO> getReviewDetails(String reviewId) {

    int ref = dao.getReviewRef(reviewId);
    //    String teacherEmail = securityUtil.getCurrentUsername();
    //    String academyId = dao.getAcademyId(teacherEmail);

    String academyId = "f236923c-4746-4b5a-8377-e7c5b53799c2";

    List<ReviewDetailVO> list = dao.getReviewDetails(ref, academyId);
    return list;
  }

  @Override
  public boolean insertReply(Review reply) {

    Optional<Review> origin = dao.getReviewById(reply.getReviewId());

    if (origin.isPresent()) {
      // 원본 글의 정보를 기반으로 답변 글 정보 갱신
      // 답변 글에 새 UUID 등록
      reply.setReviewId(UUIDGenerator.getUUID());
      reply.setReviewReRef(origin.get().getReviewReRef());
      reply.setReviewReLev(1);
      reply.setAcademyId(origin.get().getAcademyId());
      reply.setCourseId(origin.get().getCourseId());
      reply.setTeacherId(origin.get().getTeacherId());
      reply.setStudentId(origin.get().getStudentId());
      dao.insertReply(reply);
      return true;
    }
    return false;
  }

  @Override
  public int deleteReview(String reviewId) {
    Optional<Review> review = dao.getReviewById(reviewId);
    if (review.isPresent()) {
      int result = dao.deleteReview(review.get());
      return result;
    }
    return 0;
  }

  @Override
  public int deleteReply(String replyId, String replyPassword) {
    Optional<Review> review = dao.getReviewById(replyId);
    if (review.isPresent() && review.get().getReviewPassword().equals(replyPassword)) {
      int result = dao.deleteReview(review.get());
      return result;
    } else if (review.isPresent() && !review.get().getReviewPassword().equals(replyPassword)) {
      return -1;
    }
    return 0;
  }


  private static HashMap<String, Object> searchMap(HashMap<String, String> hm) {
    HashMap<String, Object> map = new HashMap<>();

    if (!hm.get("reviewDate1").isEmpty()) {
      map.put("reviewDate1", hm.get("reviewDate1"));
    }

    if (!hm.get("reviewDate2").isEmpty()) {
      map.put("reviewDate2", hm.get("reviewDate2"));
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
