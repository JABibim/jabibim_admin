package com.jabibim.admin.service;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.ReviewMapper;
import com.jabibim.admin.security.dto.AccountDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

  private ReviewMapper dao;

  @Autowired
  public ReviewServiceImpl(ReviewMapper dao) {
    this.dao = dao;
  }

  @Override
  public int getSearchListCount(HashMap<String, String> hm, HttpSession session) {

    HashMap<String, Object> map = searchMap(hm);

    String academyId = (String) session.getAttribute("aid");

    map.put("academyId", academyId);

    return dao.getSearchListCount(map);
  }

  @Override
  public List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit, HttpSession session) {

    HashMap<String, Object> map = searchMap(hm);

    String academyId = (String) session.getAttribute("aid");

    map.put("academyId", academyId);

    map.put("offset", (page - 1) * limit);  // offset
    map.put("limit", limit);         // limit

    List<ReviewListVO> list = dao.getSearchList(map);

    return list;
  }

  @Override
  public List<ReviewDetailVO> getReviewDetails(String reviewId, HttpSession session) {

    int ref = dao.getReviewRef(reviewId);

    String academyId = (String) session.getAttribute("aid");


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

  @Override
  public boolean updateExposureStat(String reviewId, int isActive) {
    Optional<Review> review = dao.getReviewById(reviewId);
    if (review.isPresent()) {
      Review r = review.get();
      r.setReviewExposureStat(isActive);
      int result = dao.updateExposureStat(r);
      if (result == 1) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean updateReply(Review reply, String replyPassword) {
    Optional<Review> origin = dao.getReviewById(reply.getReviewId());
    if (origin.isPresent() && origin.get().getReviewPassword().equals(replyPassword)) {
      Review r = origin.get();
      r.setReviewContent(reply.getReviewContent());
      r.setReviewSubject(reply.getReviewSubject());
      int result = dao.updateReply(r);
      if (result == 1) {
        return true;
      }
    }
    return false;
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
