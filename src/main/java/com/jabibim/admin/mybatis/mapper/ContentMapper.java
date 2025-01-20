package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.classes.response.SelectCourseClassDetailListResDto;
import com.jabibim.admin.dto.content.classes.response.SelectCourseClassListResDto;
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

    List<String> getCourseClassFileList(String courseId);

    void deleteCourse(String courseId);

    void deleteClass(String courseId);

    void deleteClassFile(String courseId);

    List<SelectCourseClassListResDto> getCourseClassList(HashMap<String, Object> map);

    List<SelectCourseClassDetailListResDto> getCourseClassDetailList(HashMap<String, Object> map);

    int getCourseClassDetailListCount(HashMap<String, Object> map);

    void addNewClassInfo(HashMap<String, Object> map);

    int getMaxClassSeq(String courseId);

    void addNewClassFileInfo(HashMap<String, Object> map);
}
