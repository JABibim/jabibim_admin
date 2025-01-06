package com.jab.admin.service;

import com.jab.admin.dto.ReviewListVO;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

  int getSearchListCount(HashMap<String, String> hm);

  List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit);




}
