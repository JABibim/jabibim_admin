package com.jabibim.admin.controller;

import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.CalendarService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        modelAndView.setViewName("calendar/calendar3");
        modelAndView.addObject("calendarInfo", calendarService.getTeacherCalendarId(academyId, teacherId));

        return modelAndView;
    }
}
