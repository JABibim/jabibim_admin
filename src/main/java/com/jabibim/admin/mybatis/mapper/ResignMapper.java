package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.ResignListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ResignMapper {

  int getResignedStudentCount(HashMap<String, Object> map);

  List<ResignListVO> getResignedStudentList(HashMap<String, Object> map);

  List<Student> getResignedStudentDataList(String format);

  int deleteResignedStudentData(Student student);
}
