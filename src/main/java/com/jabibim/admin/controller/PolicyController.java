package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.domain.PolicyPage;
import com.jabibim.admin.domain.Term;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.PrivacyService;
import com.jabibim.admin.service.TermService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/policy")
public class PolicyController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private PrivacyService privacyService;
    private TermService termService;

    public PolicyController(PrivacyService privacyService, TermService termService) {
        this.privacyService = privacyService;
        this.termService = termService;
    }

    @GetMapping(value="/privacy")
    public ModelAndView privacyList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session) {
        int limit =10;

        String academyId = (String) session.getAttribute("aid");

        int listcount = privacyService.getListCount(academyId);
        List<Privacy> list = privacyService.getPrivacyList(page, limit, academyId);

        PolicyPage result = new PolicyPage(page, limit, listcount);

        mv.setViewName("policy/privacy/privacy_list");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("privacylist", list);
        mv.addObject("limit",limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value="/list_ajax")
    public Map<String, Object> PrivacyListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ){

        System.out.println("PrivacyListAjax 호출됨!");

        String academyId = (String) session.getAttribute("aid");

        int listcount = privacyService.getListCount(academyId); //총 리스트 수를 받아옴
        List<Privacy> list = privacyService.getPrivacyList(page, limit, academyId); // 리스트를 받아옴
        PolicyPage result = new PolicyPage(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("privacylist", list);
        map.put("limit",limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @ResponseBody
    @PostMapping("/privacy/previous")
    public Map<String, Object> getPreviousContent(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String academyId = (String) session.getAttribute("aid");

        try {
            // 이전 본문 내용을 데이터베이스에서 가져오는 로직
            Privacy previousPolicy = privacyService.getLatestPrivacyPolicy(academyId); // 최신 본문 불러오기
            if (previousPolicy != null && previousPolicy.getPrivacyTermContent() != null) {
                response.put("privacyTermContent", previousPolicy.getPrivacyTermContent());
            } else {
                response.put("privacyTermContent", null); // 본문 내용이 없을 경우
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            response.put("privacyTermContent", null);
        }

        return response;
    }

    @GetMapping(value="/privacy/write")
    public ModelAndView privacyWrite(HttpSession session,ModelAndView mv) {
        String teacherName = (String) session.getAttribute("name");

        mv.setViewName("policy/privacy/privacy_write");
        mv.addObject("name", teacherName);

        return mv;
    }

    @PostMapping(value="/privacy/add")
    public String addPolicy(Privacy privacy, HttpServletRequest request,HttpSession session) {
        // UUID 생성
        String privacyTermId = UUIDGenerator.getUUID();
        String aid = (String) session.getAttribute("aid");


        // 생성한 UUID를 Privacy 객체에 설정
        privacy.setPrivacyTermId(privacyTermId);
        privacy.setAcademyId(aid);
        privacyService.insertPrivacy(privacy);
        logger.info(privacy.toString());
        return "redirect:/policy/privacy";
    }

    @GetMapping(value="/privacy/detail")
    public ModelAndView privacyDetail(int rnum, ModelAndView mv,
                                      HttpServletRequest request, HttpSession session) {

        String academyId = (String) session.getAttribute("aid");
        Privacy privacy = privacyService.getDetail(rnum);
        int maxRnum = privacyService.getMaxRnum(academyId);

        if (privacy == null) {
            logger.info("상세보기 실패");

            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            logger.info("상세보기 성공");
            mv.setViewName("policy/privacy/privacy_detail");
            mv.addObject("privacyData", privacy);
            mv.addObject("maxRnum", maxRnum);
        }
        return mv;
    }

    @GetMapping(value="/service")
    public ModelAndView serviceList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session) {
        int limit =10;

        String academyId = (String) session.getAttribute("aid");
        int listcount = termService.getListCount(academyId);
        List<Term> list = termService.getTermList(page, limit,academyId);

        PolicyPage result = new PolicyPage(page, limit, listcount);

        mv.setViewName("policy/service/service_list");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("servicelist", list);
        mv.addObject("limit",limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value="/list_ajax2")
    public Map<String, Object> ServiceListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ){

        String academyId = (String) session.getAttribute("aid");
        int listcount = termService.getListCount(academyId); //총 리스트 수를 받아옴
        List<Term> list = termService.getTermList(page, limit,academyId); // 리스트를 받아옴
        PolicyPage result = new PolicyPage(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("servicelist", list);
        map.put("limit",limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @ResponseBody
    @PostMapping("/service/previous")
    public Map<String, Object> getContent(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String academyId = (String) session.getAttribute("aid");

        try {
            // 이전 본문 내용을 데이터베이스에서 가져오는 로직
            Term previousPolicy = termService.getLatestTermPolicy(academyId); // 최신 본문 불러오기
            if (previousPolicy != null && previousPolicy.getServiceTermContent() != null) {
                response.put("serviceTermContent", previousPolicy.getServiceTermContent());
            } else {
                response.put("serviceTermContent", null); // 본문 내용이 없을 경우
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            response.put("serviceTermContent", null);
        }
        return response;
    }

    @PostMapping(value="/service/add")
    public String addService(Term term, HttpServletRequest request, HttpSession session) {
        // UUID 생성
        String serviceTermId = UUIDGenerator.getUUID();
        String aid = (String) session.getAttribute("aid");

        // 생성한 UUID를 Privacy 객체에 설정
        term.setServiceTermId(serviceTermId);
        term.setAcademyId(aid);
        termService.insertTerm(term);
        logger.info(term.toString());
        return "redirect:/policy/service";
    }

    @GetMapping(value="/service/write")
    public ModelAndView serviceWrite(HttpSession session,ModelAndView mv) {
        String teacherName = (String) session.getAttribute("name");

        mv.setViewName("policy/service/service_write");
        mv.addObject("name", teacherName);

        return mv;
    }

    @GetMapping(value="/service/detail")
    public ModelAndView serviceDetail(int rnum, ModelAndView mv,
                                      HttpServletRequest request, HttpSession session) {

        String academyId = (String) session.getAttribute("aid");
        Term term = termService.getDetail(rnum);
        int maxRnum = termService.getMaxRnum(academyId);

        if (term == null) {
            logger.info("상세보기 실패");

            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            logger.info("상세보기 성공");
            mv.setViewName("policy/service/service_detail");
            mv.addObject("serviceData", term);
            mv.addObject("maxRnum", maxRnum);
        }
        return mv;
    }

}
