package com.jabibim.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jabibim.admin.dto.ClassInfoVO;
import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.dto.CourseInfoVO;
import com.jabibim.admin.mybatis.mapper.ClassMapper;
import com.jabibim.admin.mybatis.mapper.CourseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseMapper courseDao;

  private final ClassMapper classDao;

  @Override
  public List<CourseInfoVO> getCourseInfoList(String academyId) {
    return courseDao.getCourseInfoList(academyId);
  }

  @Override
  public CourseDetailVO getCourseDetail(String courseId, String academyId) {
    CourseDetailVO courseDetail = courseDao.getCourseDetail(courseId, academyId);

    List<ClassInfoVO> classList = classDao.getClassList(courseId, academyId);

    int classCount = classDao.getClassCount(courseId, academyId);

    courseDetail.setClassList(classList);
    courseDetail.setClassCount(classCount);

    return courseDetail;
  }
}
