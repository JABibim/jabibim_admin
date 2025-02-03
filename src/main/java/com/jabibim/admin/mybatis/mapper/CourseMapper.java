package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.dto.CourseInfoVO;

@Mapper
public interface CourseMapper {

  public List<CourseInfoVO> getCourseInfoList(String academyId);

  public CourseDetailVO getCourseDetail(String courseId, String academyId);

}
