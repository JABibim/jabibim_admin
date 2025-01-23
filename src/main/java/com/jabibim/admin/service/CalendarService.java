package com.jabibim.admin.service;

import com.jabibim.admin.dto.auth.response.GoogleAuthTokenResponse;
import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;

public interface CalendarService {
    String addNewCalendar(String accessToken);

    void insertNewCalendarInfo(String academyId, String teacherId, String accessToken, String refreshToken, long expiresIn, String newCalendarId);

    SelectTeacherCalInfoReqDto getTeacherCalendarId(String academyId, String teacherId);

    String getRefreshToken(String academyId, String teacherId);

    int updateReIssueTokenInfo(String academyId, String teacherId, GoogleAuthTokenResponse newTokens);
}