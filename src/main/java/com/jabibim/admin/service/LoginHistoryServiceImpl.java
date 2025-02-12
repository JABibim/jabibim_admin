package com.jabibim.admin.service;

import java.util.HashMap;
import java.util.List;

import com.jabibim.admin.security.dto.AccountDto;
import org.apache.http.auth.AUTH;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.jabibim.admin.domain.LoginHistory;
import com.jabibim.admin.dto.LoginHistListVO;
import com.jabibim.admin.mybatis.mapper.LoginHistoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

  private final LoginHistoryMapper dao;

  @Override
  public int getLoginHistCount(HashMap<String, String> hm, Authentication auth) {
    HashMap<String, Object> map = searchMap(hm);
    AccountDto user = (AccountDto) auth.getPrincipal();
    String academyId = user.getAcademyId();
    map.put("academyId", academyId);

    return dao.getLoginHistCount(map);
  }

  @Override
  public List<LoginHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit, Authentication auth) {
    HashMap<String, Object> map = searchMap(hm);

    AccountDto user = (AccountDto) auth.getPrincipal();
    String academyId = user.getAcademyId();


    map.put("limit", limit);
    map.put("offset", (page - 1) * limit);
    map.put("academyId", academyId);

    return dao.getLoginHistList(map);
  }



  private static HashMap<String, Object> searchMap(HashMap<String, String> hm) {
    HashMap<String, Object> map = new HashMap<>();

    if (!hm.get("signDate1").isEmpty()) {
      map.put("signDate1", hm.get("signDate1"));
    }

    if (!hm.get("signDate2").isEmpty()) {
      map.put("signDate2", hm.get("signDate2"));
    }

    if (!hm.get("logStatus").equals("sf")) {
      map.put("logStatus", hm.get("logStatus"));
    }

    if (!hm.get("searchWord").isEmpty()) {
      map.put("searchField", hm.get("searchField").split(""));
      map.put("searchWord", "%" + hm.get("searchWord") + "%");
    }
    return map;
  }

  @Override
  public void insertLoginHistory(LoginHistory loginHistory) {
    dao.insertLoginHistory(loginHistory);
  }
}
