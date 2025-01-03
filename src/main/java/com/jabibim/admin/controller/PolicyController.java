package com.jabibim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/policy")
public class PolicyController {

    @GetMapping(value="/privacy")
    public String privacyList() {
        return "policy/privacy/privacy_list";
    }

    @GetMapping(value="/privacy/write")
    public String privacyWrite() {
        return "policy/privacy/privacy_write";
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
