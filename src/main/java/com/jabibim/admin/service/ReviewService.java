package com.jabibim.admin.service;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

  int getSearchListCount(HashMap<String, String> hm, Authentication auth);

  List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit, Authentication auth);

  List<ReviewDetailVO> getReviewDetails(String reviewId, Authentication auth);

  boolean insertReply(Review review);

  int deleteReview(String reviewId);

  int deleteReply(String replyId, String replyPassword);

  boolean updateExposureStat(String reviewId, int isActive);

  boolean updateReply(Review reply, String replyPassword);
}
