package com.jabibim.admin.security.handler;

import com.jabibim.admin.dto.OAuth.CustomOAuth2User;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.security.util.FnUtil;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TeacherService teacherService;

    public FormAuthenticationSuccessHandler(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("==========onAuthenticationSuccess====authentication===: " + authentication.getPrincipal());

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //세션에 정보 저장
        setSession(authentication, request);

        // 사용자 타입 확인
        Object principal = authentication.getPrincipal();
        String targetUrl = "/dashboard"; // 기본 리디렉션 경로

        // TODO 추후 IP가 필요한 경우 사용 가능 ( 시간은 추가해야함 )
        String ipAddr = FnUtil.getIpAddr(request);

        if (principal instanceof CustomOAuth2User) {
            HttpSession session = request.getSession();
            String email = session.getAttribute("email").toString();
            String academyId = teacherService.getAcademyIdByEmail(email);

            if (academyId == null) {
                targetUrl = "/authenticationCode"; // academyId가 null이면 /authenticationCode로 리디렉트
            }
        }

        // 저장된 요청이 있다면 해당 URL로 다이렉트
        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private void setSession(Authentication authentication, HttpServletRequest request) {
        HttpSession session = request.getSession();

        // CustomOAuth2User 객체가 Principal로 저장되어 있을 경우, 이를 AccountDto로 변환(구글 로그인 성공 경우)
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            AccountDto account = customOAuth2User.toAccountDto(); // CustomOAuth2User에서 AccountDto로 변환

            // 세션에 필요한 정보 저장
            session.setAttribute("isAdmin", account.getId().equals("ADMIN"));
            session.setAttribute("id", teacherService.getTeacherIdByEmail(account.getEmail()));
            session.setAttribute("aid", teacherService.getAcademyIdByEmail(account.getEmail()));
            session.setAttribute("name", account.getName());
            session.setAttribute("email", account.getEmail());
        } else {

            //일반계정 로그인
            AccountDto account = (AccountDto) authentication.getPrincipal();

            session.setAttribute("isAdmin", account.getId().equals("ADMIN"));
            session.setAttribute("id", account.getId());
            session.setAttribute("aid", account.getAcademyId());
            session.setAttribute("role", account.getRoles());
            session.setAttribute("name", account.getName());
            session.setAttribute("email", account.getEmail());
        }
    }
}
