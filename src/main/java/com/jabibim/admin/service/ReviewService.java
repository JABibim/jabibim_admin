package com.jabibim.admin.service;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

  int getSearchListCount(HashMap<String, String> hm);

  List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit);

  List<ReviewDetailVO> getReviewDetails(String reviewId);

  boolean insertReply(Review review);

  int deleteReview(String reviewId);

  int deleteReply(String replyId, String replyPassword);
}
