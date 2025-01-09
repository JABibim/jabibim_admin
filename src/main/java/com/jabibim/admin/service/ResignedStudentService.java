package com.jabibim.admin.service;

import com.jabibim.admin.dto.ResignListVO;

import java.util.HashMap;
import java.util.List;

public interface ResignedStudentService {
  List<ResignListVO> getResignedStudentList(HashMap<String, String> hm, int page, int limit);

  int getResignedStudentCount(HashMap<String, String> hm);
}
