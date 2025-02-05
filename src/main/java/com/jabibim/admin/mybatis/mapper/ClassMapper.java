package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.dto.ClassInfoVO;

@Mapper
public interface ClassMapper {
  List<ClassInfoVO> getClassList(String courseId, String academyId);

  int getClassCount(String courseId, String academyId);
}
