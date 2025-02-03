package com.jabibim.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
            System.out.println("🚀🚀🚀 JSON 파싱 시작 🚀🚀🚀");
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(item.toString());
            System.out.println("🚀🚀🚀 JSON 파싱 끝 🚀🚀🚀");

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

            System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 시작 🚀🚀🚀");
            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);
            System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 완료 🚀🚀🚀");

            // 구글 캘린더에 이벤트 추가
            System.out.println("🚀🚀🚀 구글 캘린더에 이벤트 추가 🚀🚀🚀");
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

    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updateCalendar(@RequestBody HashMap<String, Object> item, HttpSession session) {

        String googleEventId = (String) item.get("googleEventId");
        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");

        SelectTeacherCalInfoReqDto calendarInfo = calendarEventService.getCalendarInfo(googleEventId);
        String accessToken = calendarInfo.getAccessToken();
        System.out.println("========update method 이동 =============");

        try {
            // 필요한 값들을 item에서 직접 가져오기
            String summary = (String) item.get("eventTitle");
            String description = (String) item.get("eventDescription");
            String startTime = (String) item.get("eventStart") + ":00";
            String endTime = (String) item.get("eventEnd") + ":00";
            String location = (String) item.get("eventLocation");
            String timeZone = (String) item.get("timeZone");

            System.out.println("🚀 data 🚀");
            System.out.println(summary);
            System.out.println(description);
            System.out.println(startTime);
            System.out.println(endTime);
            System.out.println(location);
            System.out.println(timeZone);

            System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 시작 🚀🚀🚀");
            Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);
            System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 완료 🚀🚀🚀");

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
                System.out.println("Event before update: " + event); // update 요청 전에 event 객체 확인
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e);
            }
            // 업데이트 요청
            System.out.println("🚀🚀🚀 구글 캘린더에 이벤트 수정 🚀🚀🚀");
            Event updatedEvent = calendarService.events()
                    .update(calendarInfo.getCalendarId(), googleEventId, event)
                    .execute();
            System.out.println("Event update: " + updatedEvent);

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

            System.out.println("==> 성공처리 응답 전!!!");
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
    @ResponseBody
    public Map<String, Object> deleteEvent(@RequestParam("eventId") String eventId, HttpServletRequest request) {
        SelectTeacherCalInfoReqDto calendarInfo = calendarEventService.getCalendarInfo(eventId);
        String accessToken = calendarInfo.getAccessToken();
        System.out.println("========delete method 이동 =============");

        System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 시작 🚀🚀🚀");
        Calendar calendarService = GoogleCalendarServiceFactory.createCalendarService(accessToken);
        System.out.println("🚀🚀🚀 구글 캘린더 서비스 생성 완료 🚀🚀🚀");

        // 예시로, 이벤트를 조회한 후 해당 ID를 얻고 삭제하는 방법
        try {
            // 이벤트 조회 (eventId는 실제 이벤트 ID로 바꿔주세요)
            Event event = calendarService.events().get(calendarInfo.getCalendarId(), eventId).execute();
            String googleEventId = event.getId();  // 이벤트의 ID를 얻을 수 있습니다.
            System.out.println("googleEventId: " + googleEventId);

            // 이벤트 삭제
            calendarService.events().delete(calendarInfo.getCalendarId(), googleEventId).execute();
            System.out.println("이벤트가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이벤트 삭제 중 오류 발생");
        }

        // DB에서 삭제 처리 (DB 관련 작업)
        int isDeleted = calendarEventService.deleteEvent(eventId);

        // 응답을 Map 객체로 반환하여 JSON 형식으로 클라이언트에 전달
        Map<String, Object> response = new HashMap<>();

        if (isDeleted == 0) {
            // 삭제 실패
            response.put("success", false);
            response.put("message", "삭제 실패");
            return response;
        } else {
            // 삭제 성공
            response.put("success", true);
            response.put("message", "삭제 성공");
            return response;
        }
    }


}
