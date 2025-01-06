package com.jabibim.admin.service;

import com.jabibim.admin.dto.ReviewListVO;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {

  int getSearchListCount(HashMap<String, String> hm);

  List<ReviewListVO> getSearchList(HashMap<String, String> hm, int page, int limit);




}
