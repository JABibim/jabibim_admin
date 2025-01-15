package com.jabibim.admin.controller;

import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.domain.Qna;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.QnaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value="/board")
public class QnaController {

    @Value("${upload.path}")
    private String saveFolder;

    private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

    private QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping(value="/qna")
    public ModelAndView qnaList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session
    ){
        session.setAttribute("referer", "qna");
        session.setAttribute("page", page);
        int limit =10;

        String academyId = (String) session.getAttribute("aid");

        int listcount = qnaService.getListCount(academyId);

        List<Qna> list = qnaService.getQnaList(page, limit, academyId);

        PaginationResult result = new PaginationResult(page, limit, listcount);

        mv.setViewName("qna/qnaList");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("qnaList", list);
        mv.addObject("limit",limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value="/qna_list_ajax")
    public Map<String, Object> QnaListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ){

        String academyId = (String) session.getAttribute("aid");
        int listcount = qnaService.getListCount(academyId); //총 리스트 수를 받아옴
        List<Qna> list = qnaService.getQnaList(page, limit, academyId); // 리스트를 받아옴
        PaginationResult result = new PaginationResult(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("qnaList", list);
        map.put("limit",limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @GetMapping(value="/qna/detail")
    public ModelAndView qnaDetail(String id, ModelAndView mv,
                                     HttpServletRequest request,
                                     @RequestHeader(value="referer", required=false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");
        String academyId = (String) session.getAttribute("aid");

        logger.info("referer: " + beforeURL);
        if (sessionReferer != null && sessionReferer.equals("qna")) {
            if (beforeURL != null && beforeURL.endsWith("qna")) {
                System.out.println("---------readCount 실행-----------");
                qnaService.setReadCountUpdate(id);
            }
            session.removeAttribute("referer");
        }

        Qna qna = qnaService.getDetail(id);
        System.out.println("Rnum : " + qna.getRnum());
        Qna preData = qnaService.getPreData(qna.getRnum(), academyId);
        Qna nextData = qnaService.getNextData(qna.getRnum(), academyId);

        Qna upData = qnaService.getUpData(id);

        if (qna == null) {
            logger.info("상세보기 실패");

            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            logger.info("상세보기 성공");
            mv.setViewName("qna/qnaPage");
            mv.addObject("qnaData", qna);
            mv.addObject("updateData", upData);
            mv.addObject("preData", preData);
            mv.addObject("nextData", nextData);
        }
        return mv;
    }

    @PostMapping(value="/qna/reply")
    public ModelAndView replyQna(Qna qna,
                                 ModelAndView mv,
                                 HttpServletRequest request,
                                 HttpSession session,
                                 RedirectAttributes rAttr) throws Exception {

        MultipartFile uploadfile = qna.getUploadfile();
        if(!uploadfile.isEmpty()){
            String fileDBName = qnaService.saveUploadedFile(uploadfile, saveFolder);
            qna.setQnaFileName(fileDBName); //바뀐 파일명으로 저장
            qna.setQnaFileOrigin(uploadfile.getOriginalFilename());//원래 파일명 저장
        }

        String detailId = qna.getQnaId();
        Optional<Qna> data = qnaService.getQnaById(detailId);
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");
        // 생성한 UUID를 Privacy 객체에 설정

        qna.setQnaId(UUIDGenerator.getUUID());
        qna.setQnaReRef(data.get().getQnaReRef());
        qna.setQnaReLev(1);
        qna.setClassId(data.get().getClassId());
        qna.setCourseId(data.get().getCourseId());
        qna.setAcademyId(academyId);
        qna.setTeacherId(teacherId);
        qna.setStudentId(data.get().getStudentId());
        int result = qnaService.replyQna(qna);

        if(result == 0){
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "QNA 답변 처리 실패");
        } else{
            qnaService.answerQna(detailId);
            rAttr.addAttribute("id", detailId);
            mv.setViewName("redirect:detail");
        }
        return mv;
    }

    @PostMapping("/qna/update")
    public String qnaModifyAction(
            Qna qnaData,
            String check,
            String updateId,
            Model mv,
            HttpServletRequest request,
            RedirectAttributes rAttr
    ) throws Exception {
        System.out.println("Qna ID: " + qnaData.getQnaId());
        System.out.println("UpdateData : " + updateId);
        System.out.println("Qna Password: " + qnaData.getQnaPassword());
        boolean userCheck = qnaService.isBoardWriter(qnaData.getQnaId(), qnaData.getQnaPassword());

        String qnaId = qnaData.getQnaId();
        if(userCheck == false){
            rAttr.addFlashAttribute("message", "비밀번호 오류입니다.");
            rAttr.addFlashAttribute("url", "history.back()");
            return "redirect:/message";
        }

        String url = "";
        MultipartFile uploadFile = qnaData.getUploadfile();
        if (check != null && !check.equals("")) { // 기존 파일 그대로 사용하는 경우
            logger.info("기존 파일 그대로 사용합니다.");

            qnaData.setQnaFileOrigin(check);

        } else {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                logger.info("파일이 변경되었습니다.");

                String fileDBName = qnaService.saveUploadedFile(uploadFile, saveFolder);

                qnaData.setQnaFileName(fileDBName); // 바뀐 파일명으로 저장
                qnaData.setQnaFileOrigin(uploadFile.getOriginalFilename()); // 원래 파일명 저장

            } else {
                logger.info("선택 파일이 없습니다.");

                qnaData.setQnaFileName(""); // ""로 초기화합니다.
                qnaData.setQnaFileOrigin(""); // ""로 초기화 합니다.
            }
        }

        qnaData.setQnaId(updateId);
        // DAO에서 수정 메서드 호출하여 수정합니다.
        int result = qnaService.qnaModify(qnaData);

        // 수정에 실패한 경우
        if (result == 0) {
            logger.info("게시판 수정 실패");

            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "게시판 수정 실패");

            url = "error/error";
        } else { // 수정 성공의 경우
            logger.info("게시판 수정 완료");

            url = "redirect:detail";
            rAttr.addAttribute("id", qnaId);
        }

        return url;

    }

    @PostMapping("/qna/delete")
    public String qnaDeleteAction(String qnaId,String qnaPassword2, String updateId, Model mv, RedirectAttributes rAttr, HttpServletRequest request) {
        boolean userCheck = qnaService.isBoardWriter(updateId, qnaPassword2);

        // 비밀번호가 일치하지 않는 경우
        if (!userCheck) {
            rAttr.addFlashAttribute("result", "passFail");
            rAttr.addAttribute("id", qnaId);

            return "redirect:detail";
        }

        // 비밀번호가 일치하는 경우 삭제 처리합니다.
        int result = qnaService.qnaDelete(updateId);

        // 삭제 처리 실패한 경우
        if (result == 0) {
            logger.info("게시판 삭제 실패");

            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");

            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
            logger.info("게시판 삭제 성공");
            qnaService.qnaAnswerStat(qnaId);

            rAttr.addFlashAttribute("result", "deleteSuccess");

            return "redirect:/board/qna";
        }
    }
}
