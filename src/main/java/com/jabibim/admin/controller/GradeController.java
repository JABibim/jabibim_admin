package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Grade;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.GradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping(value ="/grade")
public class GradeController {

    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping(value="/addGrade")
    @ResponseBody
    public ResponseEntity<?> addGrade(@RequestBody Grade grade, Authentication auth) {
        try {
            //필요한 정보 선언
            AccountDto account = (AccountDto) auth.getPrincipal();
            String academyId = account.getAcademyId();
            String gradeId = UUIDGenerator.getUUID();
            
            gradeService.addGrade(grade, academyId, gradeId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "등급이 성공적으로 추가되었습니다."
            ));
        } catch (Exception e){
            logger.error("등급 추가중 에러 발생 : ", e);
            return ResponseEntity.status(500).body(Map.of(
                    "status", "fail",
                    "message", "등급 추가 실패 : " + e.getMessage()
            ));
        }
    }

}
