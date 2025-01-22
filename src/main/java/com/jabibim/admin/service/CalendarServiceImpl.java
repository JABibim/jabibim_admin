package com.jabibim.admin.service;

import com.google.api.services.calendar.Calendar;
import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;
import com.jabibim.admin.func.GoogleCalendar;
import com.jabibim.admin.func.GoogleCalendarServiceFactory;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.CalendarMapper;
import org.springframework.stereotype.Service;

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
}