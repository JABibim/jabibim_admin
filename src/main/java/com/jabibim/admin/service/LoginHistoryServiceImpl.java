package com.jabibim.admin.service;

import com.jabibim.admin.dto.SignInHistListVO;
import com.jabibim.admin.mybatis.mapper.LoginHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

  private final LoginHistoryMapper dao;

  @Override
  public int getLoginHistCount(HashMap<String, String> hm) {
    HashMap<String, Object> map = searchMap(hm);

//    String teacherEmail = securityUtil.getCurrentUsername();
//    String academyId = dao.getAcademyId(teacherEmail);
    map.put("academyId", "f236923c-4746-4b5a-8377-e7c5b53799c2");

    return dao.getLoginHistCount(map);
  }

  @Override
  public List<SignInHistListVO> getLoginHistList(HashMap<String, String> hm, int page, int limit) {
    HashMap<String, Object> map = searchMap(hm);

//    String teacherEmail = securityUtil.getCurrentUsername();
//    String academyId = dao.getAcademyId(teacherEmail);
    map.put("academyId", "f236923c-4746-4b5a-8377-e7c5b53799c2");

    map.put("limit", limit);
    map.put("offset", (page - 1) * limit);

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

}
