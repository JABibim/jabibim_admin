package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface BoardMapper {

    public List<CourseListDTO> getCourseList(HashMap<String, Object> map);

    public void insertNotice(Board notice);
}