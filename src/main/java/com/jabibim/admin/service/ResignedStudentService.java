package com.jabibim.admin.service;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.ResignListVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;

public interface ResignedStudentService {
  List<ResignListVO> getResignedStudentList(HashMap<String, String> hm, int page, int limit, HttpSession session);

  int getResignedStudentCount(HashMap<String, String> hm, HttpSession session);

  List<Student> getResignedStudentData(String date);

  int deleteResignedStudentData(Student student);
}
