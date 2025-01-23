package com.jabibim.admin.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;
import com.jabibim.admin.func.GoogleCalendarServiceFactory;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.CalendarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping(value = "")
    public ModelAndView boardList(
            Authentication authentication
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        String teacherId = account.getId();
        String academyId = account.getAcademyId();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calendar/calendar5");
        modelAndView.addObject("calendarInfo", calendarService.getTeacherCalendarId(academyId, teacherId));
        System.out.println("calendarInfo : "+calendarService.getTeacherCalendarId(academyId, teacherId));

        return modelAndView;
    }

    @PostMapping(value = "/insert", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> insertCalendar(@RequestBody String item, HttpServletRequest request, HttpSession session, PrintWriter printWriter) {

        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");

         SelectTeacherCalInfoReqDto calendarInfo = calendarService.getTeacherCalendarId(academyId, teacherId);
         String accessToken = calendarInfo.getAccessToken();
        System.out.println("========insert method 이동 =============");

        try {
            // JSON 파싱
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(item.toString());

            String summary = (String) obj.get("summary");
            String description = (String) obj.get("description");
            String startTime = (String) obj.get("startDate");
            String endTime = (String) obj.get("endDate");
            String timeZone = (String) obj.get("timeZone");

            // 구글 캘린더 이벤트 객체 생성
            Event event = new Event();
            event.setSummary(summary);
            event.setDescription(description);

            // 이벤트 시작시간, 종료시간, 타임존 설정
            EventDateTime start = new EventDateTime();
            start.setDateTime(new DateTime(startTime));
            start.setTimeZone(timeZone);
            event.setStart(start);

            EventDateTime end = new EventDateTime();
            end.setDateTime(new DateTime(endTime));
            end.setTimeZone(timeZone);
            event.setEnd(end);

            // 구글 캘린더 서비스 인스턴스 생성
            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

            // 구글 캘린더에 이벤트 추가
            Event confirmed = calendarService.events().insert(calendarInfo.getGoogleCalendarId(), event).execute();
            System.out.println("Event created: " + confirmed);


//
//            // DB에 저장할 Calendar 객체 생성
//            Calendar calendar = new Calendar();
//            calendar.setCalendarEventId(UUIDGenerator.getUUID()); // UUID 생성
//            calendar.setAcademyId(academyId);
//            calendar.setTeacherId(teacherId);
//            calendar.setCalendarId(calendarId);
//
//            // DB에 캘린더 저장
//            calendarService.insertCalendar(calendar);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event added successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 응답 반환
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error occurred while adding event.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
