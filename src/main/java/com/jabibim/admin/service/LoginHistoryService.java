package com.jabibim.admin.service;

import com.jabibim.admin.domain.LoginHistory;
import com.jabibim.admin.dto.LoginHistListVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface LoginHistoryService {
  int getLoginHistCount(HashMap<String, String> hm, HttpSession session);

  List<LoginHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit, HttpSession session);

  void insertLoginHistory(LoginHistory loginHistory);
}
