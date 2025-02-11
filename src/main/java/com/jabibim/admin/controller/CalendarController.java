package com.jabibim.admin.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.jabibim.admin.domain.CalendarEvent;
import com.jabibim.admin.dto.common.ApiResponse;
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
import java.util.Map;


@Controller
@RequestMapping(value = "/calendar")
public class CalendarController {
    private final CalendarService calendarEventService;
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    public CalendarController(CalendarService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping(value = "")
    public ModelAndView calendarList(
            Authentication authentication,
            HttpSession session
    ) {
        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");

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

            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

            // 구글 캘린더에 이벤트 추가
            Event confirmed = calendarService.events().insert(calendarInfo.getGoogleCalendarId(), event).execute();
            System.out.println("✅ Event created successfully: " + confirmed);

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

            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "success");
            ApiResponse<HashMap<String, Object>> body = new ApiResponse<>(true, result, "새로운 일정이 추가되었습니다.");

            return ResponseEntity.ok(body);
        } catch (GoogleJsonResponseException e) {

            if (e.getStatusCode() == 401) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("message", 401);

                ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(
                        false,
                        result,
                        "Unauthorized: 토큰이 만료되었습니다. 다시 로그인하세요."
                );
                System.out.println("======= 401 Unauthorized 발생 =======");
                // 401 상태 코드로 응답
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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

    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updateCalendar(@RequestBody HashMap<String, Object> item, HttpSession session) {

        String googleEventId = (String) item.get("googleEventId");
        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");

        SelectTeacherCalInfoReqDto calendarInfo = calendarEventService.getCalendarInfo(googleEventId);
        String accessToken = calendarInfo.getAccessToken();

        try {
            // 필요한 값들을 item에서 직접 가져오기
            String summary = (String) item.get("eventTitle");
            String description = (String) item.get("eventDescription");
            String startTime = (String) item.get("eventStart") + ":00";
            String endTime = (String) item.get("eventEnd") + ":00";
            String location = (String) item.get("eventLocation");
            String timeZone = (String) item.get("timeZone");

            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

            // 기존 이벤트 가져오기
            Event event = calendarService.events().get(calendarInfo.getCalendarId(), googleEventId).execute();
            event.setSummary(summary);
            event.setDescription(description);
            event.setLocation(location);
            System.out.println("✅ Event created successfully: " + event);

            try {
                EventDateTime start = new EventDateTime();
                start.setDateTime(new DateTime(startTime));
                start.setTimeZone(timeZone);
                event.setStart(start);

                EventDateTime end = new EventDateTime();
                end.setDateTime(new DateTime(endTime));
                end.setTimeZone(timeZone);
                event.setEnd(end);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
            }
            // 업데이트 요청
            Event updatedEvent = calendarService.events()
                    .update(calendarInfo.getCalendarId(), googleEventId, event)
                    .execute();

            CalendarEvent calendar = new CalendarEvent();
            calendar.setGoogleEventId(updatedEvent.getId());
            calendar.setEventTitle(updatedEvent.getSummary());
            calendar.setEventDescription(updatedEvent.getDescription());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            calendar.setEventStart(LocalDateTime.parse(updatedEvent.getStart().getDateTime().toStringRfc3339(), formatter));
            calendar.setEventEnd(LocalDateTime.parse(updatedEvent.getEnd().getDateTime().toStringRfc3339(), formatter));
            calendar.setEventLocation(updatedEvent.getLocation());

            calendarEventService.updateCalendar(calendar); // DB 업데이트 실행


            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "success");

            return ResponseEntity.ok(result);

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 401) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("message", "401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("message", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteEvent(@RequestParam("eventId") String eventId) {
        Map<String, Object> result = new HashMap<>();

        SelectTeacherCalInfoReqDto calendarInfo = calendarEventService.getCalendarInfo(eventId);
        String accessToken = calendarInfo.getAccessToken();
        System.out.println("========delete method 이동 =============");

        Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);

        try {
            // 이벤트 조회 및 삭제
            Event event = calendarService.events().get(calendarInfo.getCalendarId(), eventId).execute();
            String googleEventId = event.getId();
            System.out.println("googleEventId: " + googleEventId);

            calendarService.events().delete(calendarInfo.getCalendarId(), googleEventId).execute();
            System.out.println("이벤트가 성공적으로 삭제되었습니다.");
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 401) {
                result.put("message", "401 Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
            result.put("message", "Google API Error: " + e.getStatusCode());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } catch (Exception e) {
            result.put("message", "500 Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // DB에서 이벤트 삭제 처리
        int isDeleted = calendarEventService.deleteEvent(eventId);

        if (isDeleted == 0) {
            result.put("success", false);
            result.put("message", "이벤트 삭제 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        } else {
            result.put("success", true);
            result.put("message", "이벤트 삭제 성공");
            return ResponseEntity.ok(result);
        }
    }

}
