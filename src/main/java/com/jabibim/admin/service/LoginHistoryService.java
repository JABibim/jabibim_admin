package com.jabibim.admin.service;

import java.util.HashMap;
import java.util.List;

import com.jabibim.admin.domain.LoginHistory;
import com.jabibim.admin.dto.LoginHistListVO;

public interface LoginHistoryService {
  int getLoginHistCount(HashMap<String, String> hm);

  List<LoginHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit);

  void insertLoginHistory(LoginHistory loginHistory);
}
