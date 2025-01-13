package com.jabibim.admin.service;

import com.jabibim.admin.dto.SignInHistListVO;

import java.util.HashMap;
import java.util.List;

public interface SignInHistService {
  int getSignInHistCount(HashMap<String, String> hm);

  List<SignInHistListVO> getSignInHistList(HashMap<String, String> hm, int page, int limit);
}
