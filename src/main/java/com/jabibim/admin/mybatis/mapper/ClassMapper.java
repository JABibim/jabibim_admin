package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import com.jabibim.admin.dto.ClassDetailVO;
import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.dto.ClassInfoVO;

@Mapper
public interface ClassMapper {
  List<ClassInfoVO> getClassList(String courseId, String academyId);

  int getClassCount(String courseId, String academyId);

  ClassDetailVO getClassDetail(String classId, String academyId);
}
