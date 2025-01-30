package com.jabibim.admin.service;

import com.jabibim.admin.dto.SignInHistListVO;

import java.util.HashMap;
import java.util.List;

public interface LoginHistoryService {
  int getLoginHistCount(HashMap<String, String> hm);

  List<SignInHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit);
}
