package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.dto.ReviewListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewMapper {

  String getAcademyId(String teacherEmail);

  int getSearchListCount(HashMap<String, Object> map);

  List<ReviewListVO> getSearchList(HashMap<String, Object> map);

  int getReviewRef(String reviewId);

  List<ReviewDetailVO> getReviewDetails(int ref, String academyId);

  Optional<Review> getReviewById(String reviewId);

  void insertReply(Review reply);

  int deleteReview(Review review);
}
