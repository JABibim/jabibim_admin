package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface CalendarMapper {
    void insertNewCalendarInfo(HashMap<String, Object> map);
    SelectTeacherCalInfoReqDto getTeacherCalendarId(String academyId, String teacherId);
}