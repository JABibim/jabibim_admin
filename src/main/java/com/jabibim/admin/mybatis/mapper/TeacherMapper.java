package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper  //쿼리 쓰는곳
public interface TeacherMapper {

    int getTeacherCount(HashMap<String, Object> params);

    List<Teacher> getTeacherList(HashMap<String, Object> map);
}
