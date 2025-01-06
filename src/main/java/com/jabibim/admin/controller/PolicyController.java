package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.domain.PrivacyPage;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.PrivacyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public PolicyController(PrivacyService privacyService) {
        this.privacyService = privacyService;
    }

    @GetMapping(value="/privacy")
    public ModelAndView privacyList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session) {
        session.setAttribute("referer", "list");
        int limit =10;

        int listcount = privacyService.getListCount();
        List<Privacy> list = privacyService.getPrivacyList(page, limit);

        PrivacyPage result = new PrivacyPage(page, limit, listcount);

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
            @RequestParam(defaultValue = "10") int limit
    ){

        System.out.println("PrivacyListAjax 호출됨!");

        int listcount = privacyService.getListCount(); //총 리스트 수를 받아옴
        List<Privacy> list = privacyService.getPrivacyList(page, limit); // 리스트를 받아옴
        PrivacyPage result = new PrivacyPage(page, limit, listcount);

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
    public Map<String, Object> getPreviousContent() {
        Map<String, Object> response = new HashMap<>();

        try {
            // 이전 본문 내용을 데이터베이스에서 가져오는 로직
            Privacy previousPolicy = privacyService.getLatestPrivacyPolicy(); // 최신 본문 불러오기
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
    public String privacyWrite() {

        return "policy/privacy/privacy_write";
    }

    @PostMapping(value="/privacy/add")
    public String addPolicy(Privacy privacy, HttpServletRequest request) {
        // UUID 생성
        String privacyTermId = UUIDGenerator.getUUID();

        // 생성한 UUID를 Privacy 객체에 설정
        privacy.setPrivacyTermId(privacyTermId);
        privacyService.insertPrivacy(privacy);
        logger.info(privacy.toString());
        return "redirect:/policy/privacy";
    }

    @GetMapping(value="/privacy/detail")
    public String privacyDetail() {
        return "policy/privacy/privacy_detail";
    }

    @GetMapping(value="/service")
    public String serviceList() {
        return "policy/service/service_list";
    }

    @GetMapping(value="/service/write")
    public String serviceWrite() {
        return "policy/service/service_write";
    }

    @GetMapping(value="/service/detail")
    public String serviceDetail() {
        return "policy/service/service_detail";
    }

}
