package com.jabibim.admin.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.jabibim.admin.domain.CalendarEvent;
import com.jabibim.admin.dto.ApiResponse;
import com.jabibim.admin.dto.calendar.response.SelectTeacherCalInfoReqDto;
import com.jabibim.admin.func.GoogleCalendarServiceFactory;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.CalendarService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@Controller
@RequestMapping(value = "/calendar")
public class CalendarController {
    private final CalendarService calendarEventService;


    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    public CalendarController(CalendarService calendarEventService) {
        this.calendarEventService = calendarEventService;
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
        modelAndView.addObject("calendarInfo", calendarEventService.getTeacherCalendarId(academyId, teacherId));
        System.out.println("calendarInfo : " + calendarEventService.getTeacherCalendarId(academyId, teacherId));

        return modelAndView;
    }

    @PostMapping(value = "/insert")
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> insertCalendar(@RequestBody String item, HttpServletRequest request, HttpSession session) {

        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");


        SelectTeacherCalInfoReqDto calendarInfo = calendarEventService.getTeacherCalendarId(academyId, teacherId);
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
            String location = (String) obj.get("location");

            // 구글 캘린더 이벤트 객체 생성
            Event event = new Event();
            event.setSummary(summary);
            event.setDescription(description);
            event.setLocation(location);

            // 이벤트 시작시간, 종료시간, 타임존 설정
            EventDateTime start = new EventDateTime();
            start.setDateTime(new DateTime(startTime));
            event.setStart(start);

            EventDateTime end = new EventDateTime();
            end.setDateTime(new DateTime(endTime));
            event.setEnd(end);

            // 구글 캘린더 서비스 인스턴스 생성
            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

            // 구글 캘린더에 이벤트 추가
            Event confirmed = calendarService.events().insert(calendarInfo.getGoogleCalendarId(), event).execute();
            System.out.println("Event created: " + confirmed);

            // 구글 캘린더 일정 정보 DB에 저장할 Calendar 객체 생성
            CalendarEvent calendar = new CalendarEvent();
            calendar.setCalendarEventId(UUIDGenerator.getUUID()); // UUID 생성
            calendar.setGoogleEventId(confirmed.getId());
            calendar.setEventCreatorEmail(confirmed.getCreator().getEmail());
            calendar.setEventTitle(confirmed.getSummary());
            calendar.setEventDescription(confirmed.getDescription());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            calendar.setEventStart(LocalDateTime.parse(confirmed.getStart().getDateTime().toStringRfc3339(), formatter));
            calendar.setEventEnd(LocalDateTime.parse(confirmed.getEnd().getDateTime().toStringRfc3339(), formatter));
            calendar.setEventLocation(confirmed.getLocation());
            calendar.setCalendarId(calendarInfo.getGoogleCalendarId());
            calendar.setAcademyId(academyId);
            calendar.setTeacherId(teacherId);

            // DB에 캘린더 저장
            calendarEventService.insertCalendar(calendar);

            System.out.println("==> before body setting!!");

            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "success");
            ApiResponse<HashMap<String, Object>> body = new ApiResponse<>(true, result, "새로운 일정이 추가되었습니다.");

            System.out.println("==> 성공처리 응답 전!!!");

            return ResponseEntity.ok(body);
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 401) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("message", "401");
                ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "일정 추가에 실패했습니다: " + e.getMessage());
                System.out.println("=======401====================== : " +  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "500");
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "일정 추가에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "500");
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "일정 추가에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
