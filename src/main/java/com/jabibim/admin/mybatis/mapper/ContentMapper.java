package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentMapper {
    List<Course> getCourseList(boolean isAdmin, String search);
}
