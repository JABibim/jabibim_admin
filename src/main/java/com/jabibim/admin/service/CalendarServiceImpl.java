package com.jabibim.admin.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.AclRule;
import com.jabibim.admin.domain.CalendarEvent;
import com.jabibim.admin.dto.auth.response.GoogleAuthTokenResponse;
import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;
import com.jabibim.admin.func.GoogleCalendar;
import com.jabibim.admin.func.GoogleCalendarServiceFactory;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.CalendarMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class CalendarServiceImpl implements CalendarService {
    private final CalendarMapper dao;

    public CalendarServiceImpl(CalendarMapper dao) {
        this.dao = dao;
    }

    @Override
    public String addNewCalendar(String accessToken) {
        try {
            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

            com.google.api.services.calendar.model.Calendar newCalendar = new GoogleCalendar().createGoogleCalendar("JABIBIM", "중앙비빔 캘린더", "Asia/Seoul");
            com.google.api.services.calendar.model.Calendar createdCalendar = calendarService.calendars().insert(newCalendar).execute();

            AclRule publicAclRule = new AclRule()
                    .setRole("reader")
                    .setScope(new AclRule.Scope().setType("default"));

            calendarService.acl().insert(createdCalendar.getId(), publicAclRule).execute();

            return createdCalendar.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JABIBIM Calendar를 생성하는데 실패했습니다.", e);
        }
    }

    @Override
    public void insertNewCalendarInfo(String academyId, String teacherId, String accessToken, String refreshToken, long expiresIn, String newCalendarId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("calendarId", UUIDGenerator.getUUID());
        map.put("academyId", academyId);
        map.put("teacherId", teacherId);
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        map.put("expiresIn", expiresIn);
        map.put("googleCalendarId", newCalendarId);

        dao.insertNewCalendarInfo(map);
    }

    @Override
    public SelectTeacherCalInfoReqDto getTeacherCalendarId(String academyId, String teacherId) {
        return dao.getTeacherCalendarId(academyId, teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRefreshToken(String academyId, String teacherId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("academyId", academyId);
        map.put("teacherId", teacherId);

        return dao.getRefreshToken(map);
    }

    @Override
    @Transactional
    public int updateReIssueTokenInfo(String academyId, String teacherId, GoogleAuthTokenResponse newTokens) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("academyId", academyId);
        map.put("teacherId", teacherId);
        map.put("newAccessToken", newTokens.getAccessToken());
        map.put("refreshToken", newTokens.getRefreshToken());
        map.put("newExpiresIn", newTokens.getExpiresIn());

        return dao.updateReIssueTokenInfo(map);
    }

    @Override
    public void insertCalendar(CalendarEvent calendar) {
        dao.insertCalendar(calendar);

    }
}