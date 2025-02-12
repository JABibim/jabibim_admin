package com.jabibim.admin.service;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

  int getSearchListCount(HashMap<String, String> hm, HttpSession session);

  List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit, HttpSession session);

  List<ReviewDetailVO> getReviewDetails(String reviewId, HttpSession session);

  boolean insertReply(Review review);

  int deleteReview(String reviewId);

  int deleteReply(String replyId, String replyPassword);

  boolean updateExposureStat(String reviewId, int isActive);

  boolean updateReply(Review reply, String replyPassword);
}
