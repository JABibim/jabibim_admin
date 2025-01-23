package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Academy;
import com.jabibim.admin.dto.common.ApiResponse;
import com.jabibim.admin.dto.setting.AddAcademyDto;
import com.jabibim.admin.dto.setting.AddTeacherDto;
import com.jabibim.admin.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Transactional(readOnly = true)
    @ResponseBody
    public boolean checkAdminPass(String password) {
        return settingService.checkAdminPass(password);
    }

    @GetMapping(value = "/manageAcademy/getAcademyList")
    @Transactional(readOnly = true)
    @ResponseBody
    public HashMap<String, Object> getAcademyList() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("academyList", settingService.getAcademyList());

        return result;
    }

    @GetMapping(value = "/manageAcademy/checkBusinessRegisNum")
    @Transactional(readOnly = true)
    @ResponseBody
    public boolean checkBusinessRegisNum(String businessRegisNum) {
        return settingService.getAcademyCountByBusinessRegisNum(businessRegisNum) == 0;
    }

    @PostMapping(value = "/manageAcademy/add")
    @Transactional
    @ResponseBody
    public HashMap<String, Object> addAcademy(@RequestBody AddAcademyDto addAcademyDto) {
        HashMap<String, Object> result = new HashMap<>();
        Academy newAcademy = settingService.addAcademy(addAcademyDto);
        result.put("academy", newAcademy);

        return result;
    }

    @GetMapping(value = "/manageTeacher")
    public String manageTeacher() {
        return "setting/manageTeacher";
    }

    @GetMapping(value = "/manageTeacher/getTeacherList")
    @Transactional(readOnly = true)
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> getTeacherList(@RequestParam String academyId) {
        try {
            HashMap<String, Object> result = new HashMap<>();
            result.put("teacherList", settingService.getTeacherList(academyId));
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "강사 목록을 불러오는데 성공했습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "강사 목록을 불러오는데 실패했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/manageTeacher/add")
    @Transactional
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> addTeacher(@RequestBody AddTeacherDto addTeacherDto) {
        try {
            settingService.addTeacher(addTeacherDto);
            ApiResponse<String> response = new ApiResponse<>(true, "success", "선생 추가에 성공했습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(true, "success", "선생 추가에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}