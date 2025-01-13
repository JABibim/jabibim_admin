package com.jabibim.admin.controller;


import com.jabibim.admin.domain.Board;
import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.dto.CourseListDTO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/board")
public class BoardController {

    @Value("${upload.path}")
    private String saveFolder;

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(value="/notice")
    public ModelAndView boardList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session
    ) {
        session.setAttribute("page", page);
        int limit =10;

        String academyId = (String) session.getAttribute("aid");

        int listcount = boardService.getListCount(academyId);

        List<Board> list = boardService.getNoticeList(page, limit, academyId);

        PaginationResult result = new PaginationResult(page, limit, listcount);

        mv.setViewName("board/notice/notice_list");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("noticeList", list);
        mv.addObject("limit",limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value="/list_ajax")
    public Map<String, Object> NoticeListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ){

        System.out.println("NoticeListAjax 호출됨!");

        String academyId = (String) session.getAttribute("aid");
        int listcount = boardService.getListCount(academyId); //총 리스트 수를 받아옴
        List<Board> list = boardService.getNoticeList(page, limit, academyId); // 리스트를 받아옴
        PaginationResult result = new PaginationResult(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("noticeList", list);
        map.put("limit",limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @GetMapping(value="/notice/write")
    public ModelAndView boardWrite(ModelAndView mv) {
        List<CourseListDTO> course = boardService.getCourseList();

        mv.setViewName("board/notice/notice_write");
        mv.addObject("courselist", course);
        return mv;
    }

    @PostMapping(value="/notice/add")
    public String addNotice(Board notice, HttpServletRequest request, HttpSession session) throws Exception {

        System.out.println("Controller layer - boardExposureStat: " + notice.getBoardExposureStat());
        MultipartFile uploadfile = notice.getUploadfile();
        if(!uploadfile.isEmpty()){
            String fileDBName = boardService.saveUploadedFile(uploadfile, saveFolder);
            notice.setBoardFileName(fileDBName); //바뀐 파일명으로 저장
            notice.setBoardFileOrigin(uploadfile.getOriginalFilename());//원래 파일명 저장
        }

        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");
        // 생성한 UUID를 Privacy 객체에 설정
        notice.setBoardId(UUIDGenerator.getUUID());
        notice.setAcademyId(academyId);
        notice.setTeacherId(teacherId);
        boardService.insertNotice(notice);
        logger.info(notice.toString());
        return "redirect:/board/notice";
    }

    @GetMapping(value="/notice/detail")
    public ModelAndView noticeDetail(String id, ModelAndView mv,
                                     HttpServletRequest request,
                                     @RequestHeader(value="referer", required=false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");
        logger.info("Session referer: " + sessionReferer);
        logger.info("Before URL: " + beforeURL);

        session.setAttribute("referer", "notice"); // referer 설정

        if (sessionReferer != null && sessionReferer.equals("notice")) {
            if (beforeURL != null) {
                // 쿼리 파라미터 제거
                String cleanedURL = beforeURL.split("\\?")[0];
                if (cleanedURL.endsWith("/board/notice")) {
                    logger.info("Read count method 실행 for board ID: " + id);
                    boardService.setReadCountUpdate(id);
                } else {
                    logger.warn("Cleaned URL does not end with '/board/notice'.");
                }
            } else {
                logger.warn("Before URL is null.");
            }
            session.removeAttribute("referer"); // 실행 후 삭제
        } else {
            logger.warn("Session referer does not match 'notice' or is null.");
        }

        Board notice = boardService.getDetail(id);
        int preRnum = notice.getRnum() +1;
        int nextRnum = notice.getRnum() -1;

        Board preData = boardService.getPreData(preRnum);
        Board nextData = boardService.getNextData(nextRnum);

        if (notice == null) {
            logger.info("상세보기 실패");

            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            logger.info("상세보기 성공");
            mv.setViewName("board/notice/notice_detail");
            mv.addObject("noticeData", notice);
            mv.addObject("preData", preData);
            mv.addObject("nextData", nextData);
        }
        return mv;
    }


    @GetMapping(value="/notice/modify")
    public String boardModify() {
        return "board/notice/notice_modify";
    }

    @GetMapping(value="/qna/detail")
    public String qnaDetail() {
        return "qna/qnaDetail";
    }

    @GetMapping(value="/qna")
    public String qnaList() {
        return "qna/qnaList";
    }

}