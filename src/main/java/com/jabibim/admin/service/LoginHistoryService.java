package com.jabibim.admin.service;

import com.jabibim.admin.domain.LoginHistory;
import com.jabibim.admin.dto.LoginHistListVO;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface LoginHistoryService {
  int getLoginHistCount(HashMap<String, String> hm, Authentication auth);

  List<LoginHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit, Authentication auth);

  void insertLoginHistory(LoginHistory loginHistory);
}
