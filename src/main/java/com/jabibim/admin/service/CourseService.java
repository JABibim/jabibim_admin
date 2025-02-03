package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.dto.CourseInfoVO;

public interface CourseService {
  List<CourseInfoVO> getCourseInfoList(String academyId);

  CourseDetailVO getCourseDetail(String courseId, String academyId);
}
