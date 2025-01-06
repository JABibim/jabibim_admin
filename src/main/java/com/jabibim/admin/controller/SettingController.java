package com.jabibim.admin.controller;

import com.jabibim.admin.service.SettingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/setting")
public class SettingController {
    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping(value = "/manageAcademy")
    public String manageAcademy() {
        return "setting/manageAcademy";
    }

    @PostMapping(value = "/checkAdminPass")
    @ResponseBody
    public boolean checkAdminPass(String password) {
        return settingService.checkAdminPass(password);
    }

    @GetMapping(value = "/manageAcademy/getAcademyList")
    @ResponseBody
    public HashMap<String, Object> getAcademyList() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("academyList", settingService.getAcademyList());

        return result;
    }
}
