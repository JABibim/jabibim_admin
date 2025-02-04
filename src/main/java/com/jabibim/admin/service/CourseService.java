package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.dto.CourseInfoVO;
import com.jabibim.admin.dto.PurchasedCourseVO;

public interface CourseService {
  List<CourseInfoVO> getCourseInfoList(String academyId);

  CourseDetailVO getCourseDetail(String courseId, String academyId);

  List<PurchasedCourseVO> getPurchasedCourseList(String studentId, String academyId);
}
