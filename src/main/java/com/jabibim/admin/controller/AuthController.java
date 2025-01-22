package com.jabibim.admin.controller;

import com.jabibim.admin.dto.auth.response.GoogleAuthTokenResponse;
import com.jabibim.admin.func.GoogleCalendar;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.CalendarService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    private final GoogleCalendar googleCalendar;
    private final CalendarService calendarService;
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.authorization.uri}")
    private String authorizationUri;

    public AuthController(GoogleCalendar googleCalendar, CalendarService calendarService) {
        this.googleCalendar = googleCalendar;
        this.calendarService = calendarService;
    }

    @GetMapping(value = "/oauth2/authorize")
    public String authorize() {
        String scope = "https://www.googleapis.com/auth/calendar";
        String responseType = "code";

        String url = this.authorizationUri + "?client_id=" + this.clientId + "&redirect_uri=" + this.redirectUri + "&response_type=" + responseType + "&scope=" + scope + "&access_type=offline";

        return "redirect:" + url;
    }

    @GetMapping(value = "/google/callback")
    public String authGoogleCallback(
            Authentication authentication,
            @RequestParam("code") String code
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        GoogleAuthTokenResponse tokens = googleCalendar.requestTokens(code);
        String teacherId = account.getId();
        String academyId = account.getAcademyId();
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();
        long expiresIn = tokens.getExpiresIn();

        // 1. 새로운 캘린더 생성하기
        String newCalendarId = calendarService.addNewCalendar(accessToken);

        // 2. 계정 정보와 신규 캘린더 정보 DB에 적재하기
        calendarService.insertNewCalendarInfo(academyId, teacherId, accessToken, refreshToken, expiresIn, newCalendarId);

        return "calendar/popup-success";
    }
}
