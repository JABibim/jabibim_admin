package com.jab.admin.mybatis.mapper;

import com.jab.admin.dto.ReviewListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ReviewMapper {

  String getAcademyId(String teacherEmail);

  int getSearchListCount(HashMap<String, Object> map);

  List<ReviewListVO> getSearchList(HashMap<String, Object> map);
}
