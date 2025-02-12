package com.jabibim.admin.service;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.ResignListVO;
import com.jabibim.admin.mybatis.mapper.ResignMapper;
import com.jabibim.admin.security.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResignedStudentServiceImpl implements ResignedStudentService {

  private final ResignMapper dao;

  @Override
  public int getResignedStudentCount(HashMap<String, String> hm, Authentication auth) {

    HashMap<String, Object> map = searchMap(hm);
    AccountDto user = (AccountDto) auth.getPrincipal();
    String academyId = user.getAcademyId();
    map.put("academyId", academyId);

    return dao.getResignedStudentCount(map);

  }

  @Override
  public List<ResignListVO> getResignedStudentList(HashMap<String, String> hm, int page, int limit, Authentication auth) {
    HashMap<String, Object> map = searchMap(hm);

    AccountDto user = (AccountDto) auth.getPrincipal();
    String academyId = user.getAcademyId();
    map.put("academyId", academyId);
    map.put("offset", (page - 1) * limit);  // offset
    map.put("limit", limit);         // limit

    List<ResignListVO> list = dao.getResignedStudentList(map);

    return list;
  }


  @Override
  public List<Student> getResignedStudentData(String format) {
    return dao.getResignedStudentDataList(format);
  }

  @Override
  public int deleteResignedStudentData(Student student) {
    return dao.deleteResignedStudentData(student);
  }


  private static HashMap<String, Object> searchMap(HashMap<String, String> hm) {
    HashMap<String, Object> map = new HashMap<>();

    if (!hm.get("resignDate1").isEmpty()) {
      map.put("resignDate1", hm.get("resignDate1"));
      map.put("dateField", hm.get("dateField"));
    }


    if (!hm.get("resignDate2").isEmpty()) {
      map.put("resignDate2", hm.get("resignDate2"));
      if (!map.containsKey("dateField")) {
        map.put("dateField", hm.get("dateField"));
      }
    }

    if (!hm.get("searchWord").isEmpty()) {
      map.put("searchField", hm.get("searchField").split(""));
      map.put("searchWord", "%" + hm.get("searchWord") + "%");
      if (!map.containsKey("dateField")) {
        map.put("dateField", hm.get("dateField"));
      }
    }
    return map;
  }
}
