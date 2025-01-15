package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.course.request.InsertCourseReqDto;
import com.jabibim.admin.dto.content.course.response.SelectCourseListResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ContentMapper {
    void addCourse(InsertCourseReqDto insertCourseReqDto);

    List<SelectCourseListResDto> getCourses(HashMap<String, Object> map);

    int getCoursesCnt(HashMap<String, Object> map);

    void updateCourseActivation(HashMap<String, Object> map);

    Course getCourseById(String courseId);

    String getAsIsProfileImagePath(String courseId);

    void updateCourse(HashMap<String, Object> map);
}
