package com.jabibim.admin.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.jabibim.admin.domain.Board;
import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.dto.CourseListDTO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(value = "/notice")
    public ModelAndView boardList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session
    ) {
        session.setAttribute("referer", "notice");
        session.setAttribute("page", page);
        int limit = 10;

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
        mv.addObject("limit", limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value = "/list_ajax")
    public Map<String, Object> NoticeListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ) {
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
        map.put("limit", limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @GetMapping(value = "/notice/write")
    public ModelAndView boardWrite(ModelAndView mv) {
        List<CourseListDTO> course = boardService.getCourseList();

        mv.setViewName("board/notice/notice_write");
        mv.addObject("courselist", course);
        return mv;
    }

    @PostMapping(value = "/notice/add")
    public String addNotice(Board notice, HttpSession session) throws Exception {
        // 세션에서 academyId와 teacherId를 가져오기
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

    @GetMapping(value = "/notice/detail")
    public ModelAndView noticeDetail(String id, ModelAndView mv,
                                     HttpServletRequest request,
                                     @RequestHeader(value = "referer", required = false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");
        logger.info("referer: " + beforeURL);
        if (sessionReferer != null && sessionReferer.equals("notice")) {
            if (beforeURL != null && beforeURL.endsWith("notice")) {
                System.out.println("---------readCount 실행-----------");
                boardService.setReadCountUpdate(id);
            }
            session.removeAttribute("referer");
        }

        String academyId = (String) session.getAttribute("aid");
        Board notice = boardService.getDetail(id);
        System.out.println("Rnum : " + notice.getRnum());
        Board preData = boardService.getPreData(notice.getRnum(),academyId);
        Board nextData = boardService.getNextData(notice.getRnum(),academyId);


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


    @GetMapping(value = "/notice/modify")
    public ModelAndView boardModify(String id, ModelAndView mv,
                                    HttpServletRequest request) {

        Board notice = boardService.getDetail(id);
        List<CourseListDTO> course = boardService.getCourseList();

        if (notice == null) {
            logger.info("수정 보기 실패");

            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "수정 보기 실패입니다.");
        } else {
            logger.info("수정 보기 성공");
            mv.setViewName("board/notice/notice_modify");
            mv.addObject("noticeData", notice);
            mv.addObject("course", course);
        }
        return mv;
    }

    @PostMapping("/notice/modifyAction")
    public String noticeModifyAction(
            HttpSession session,
            Board noticeData,
            String check,
            String pathValue,
            Model mv,
            HttpServletRequest request,
            RedirectAttributes rAttr
    ) throws Exception {

        // 세션에서 academyId와 teacherId를 가져오기
        String academyId = (String) session.getAttribute("aid");
        String teacherId = (String) session.getAttribute("id");

        boolean userCheck = boardService.isBoardWriter(noticeData.getBoardId(), noticeData.getBoardPassword());

        if (!userCheck) {
            rAttr.addFlashAttribute("message", "비밀번호 오류입니다.");
            rAttr.addFlashAttribute("url", "history.back()");
            return "redirect:/message";
        }

        String url = "";
        MultipartFile uploadFile = noticeData.getUploadfile();

        if (check != null && !check.equals("")) { // 기존 파일 그대로 사용하는 경우
            logger.info("기존 파일 그대로 사용합니다.");

            noticeData.setBoardFileOriginName(check); // 원래 파일명으로 저장
            noticeData.setBoardFilePath(pathValue); // 원래 파일 경로로 저장 ( check와 동일하게, 기존 파일을 사용하는 경우 원래 파일의 경로를 그대로 사용 )
        } else {
            if (uploadFile != null && !uploadFile.isEmpty()) {
                logger.info("파일이 변경되었습니다.");

                String newUploadedFilePath = boardService.changeFile(uploadFile, noticeData.getBoardId(), academyId);

                noticeData.setBoardFileOriginName(uploadFile.getOriginalFilename());
                noticeData.setBoardFilePath(newUploadedFilePath); // s3에 업로드 후 새로운 파일 경로를 set
            } else {
                logger.info("선택 파일이 없습니다.");

                noticeData.setBoardFileOriginName(""); // ""로 초기화합니다.
                noticeData.setBoardFilePath(""); // ""로 초기화합니다.

                boardService.deleteBoardFile(noticeData.getBoardId());
            }
        }

        // DAO에서 수정 메서드 호출하여 수정합니다.
        int result = boardService.boardModify(noticeData);

        // 수정에 실패한 경우
        if (result == 0) {
            logger.info("게시판 수정 실패");

            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "게시판 수정 실패");

            url = "error/error";
        } else { // 수정 성공의 경우
            logger.info("게시판 수정 완료");

            url = "redirect:detail";
            rAttr.addAttribute("id", noticeData.getBoardId());
        }

        return url;

    }

    @PostMapping("/notice/delete")
    public String boardDeleteAction(String boardPassword, String boardId, Model mv, RedirectAttributes rAttr, HttpServletRequest request) {
        boolean userCheck = boardService.isBoardWriter(boardId, boardPassword);

        // 비밀번호가 일치하지 않는 경우
        if (!userCheck) {
            rAttr.addFlashAttribute("result", "passFail");
            rAttr.addAttribute("id", boardId);

            return "redirect:detail";
        }

        // 비밀번호가 일치하는 경우 삭제 처리합니다.
        int result = boardService.boardDelete(boardId);

        // 삭제 처리 실패한 경우
        if (result == 0) {
            logger.info("게시판 삭제 실패");

            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");

            return "error/error";
        } else {
            // 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
            logger.info("게시판 삭제 성공");

            rAttr.addFlashAttribute("result", "deleteSuccess");

            return "redirect:/board/notice";
        }
    }

    @GetMapping(value = "/notice/fileDownload/{boardId}")
    @ResponseBody
    public void fileDownload(
            @PathVariable String boardId,
            HttpServletResponse response
    ) {
        Board boardInfo = boardService.getDetail(boardId);
        String filePath = boardInfo.getBoardFilePath();
        int indexOfBucketName = filePath.indexOf(bucket);
        String key = filePath.substring(indexOfBucketName + bucket.length() + 1);

        S3Object s3Object = amazonS3.getObject(bucket, key);
        try (InputStream inputStream = s3Object.getObjectContent();
             OutputStream outputStream = response.getOutputStream()) {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + boardInfo.getBoardFileOriginName());
            response.setContentLengthLong(s3Object.getObjectMetadata().getContentLength());

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 에러 처리
        }
    }
}
