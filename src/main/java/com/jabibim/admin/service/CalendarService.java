package com.jabibim.admin.service;

import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;

public interface CalendarService {
    String addNewCalendar(String accessToken);

    void insertNewCalendarInfo(String academyId, String teacherId, String accessToken, String refreshToken, long expiresIn, String newCalendarId);

    SelectTeacherCalInfoReqDto getTeacherCalendarId(String academyId, String teacherId);
}