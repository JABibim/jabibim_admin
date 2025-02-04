package com.jabibim.admin.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.dto.LastStudyHistoryVO;

@Mapper
public interface StudyHistoryMapper {
  public LastStudyHistoryVO getLastStudyHistory(String studentId, String academyId);
}
