package com.jabibim.admin.service;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.ResignListVO;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface ResignedStudentService {
  List<ResignListVO> getResignedStudentList(HashMap<String, String> hm, int page, int limit, Authentication auth);

  int getResignedStudentCount(HashMap<String, String> hm, Authentication auth);

  List<Student> getResignedStudentData(String date);

  int deleteResignedStudentData(Student student);
}
