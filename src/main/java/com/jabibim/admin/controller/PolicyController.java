package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Privacy;
import com.jabibim.admin.service.PrivacyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/policy")
public class PolicyController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private PrivacyService privacyService;

    public PolicyController(PrivacyService privacyService) {
        this.privacyService = privacyService;
    }

    @GetMapping(value="/privacy")
    public String privacyList() {
        return "policy/privacy/privacy_list";
    }

    @GetMapping(value="/privacy/write")
    public String privacyWrite() {
        return "policy/privacy/privacy_write";
    }

    @PostMapping(value="/privacy/add")
    public String addPolicy(Privacy privacy, HttpServletRequest request) {
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
