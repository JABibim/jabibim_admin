package com.jabibim.admin.service;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.course.request.InsertCourseReqDto;
import com.jabibim.admin.dto.content.course.request.SelectCourseListReqDto;
import com.jabibim.admin.dto.content.course.response.SelectCourseListResDto;
import com.jabibim.admin.func.S3Uploader;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.ContentMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentMapper contentDao;
    private final S3Uploader s3Uploader;

    public ContentServiceImpl(ContentMapper contentDao, S3Uploader s3Uploader) {
        this.contentDao = contentDao;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public void addCourse(String teacherId, String academyId, InsertCourseReqDto insertCourseReqDto, MultipartFile courseImage) {
        String newCourseUUID = UUIDGenerator.getUUID();
        if (courseImage.getOriginalFilename() == null) {
            throw new IllegalArgumentException("courseImage 또는 파일 이름이 null입니다.");
        }
        String fileName = "profile." + getExtension(courseImage.getOriginalFilename());
        String uploadedPath = s3Uploader.uploadFileToS3(courseImage, "course/" + newCourseUUID + "/profile/" + fileName);

        insertCourseReqDto.setCourseProfileOriginName(courseImage.getOriginalFilename());
        insertCourseReqDto.setCourseProfilePath(uploadedPath);
        insertCourseReqDto.setCourseId(newCourseUUID);
        insertCourseReqDto.setAcademyId(academyId);
        insertCourseReqDto.setTeacherId(teacherId);

        contentDao.addCourse(insertCourseReqDto);
    }

    @Override
    public List<SelectCourseListResDto> getCourseList(boolean isAdmin, String academyId, int page, int limit, SelectCourseListReqDto selectCourseListReqDto) {
        HashMap<String, Object> map = new HashMap<>();
        setSearchCondition(map, isAdmin, academyId, selectCourseListReqDto);
        // 페이징 처리를 위한 값
        map.put("limit", limit);
        map.put("offset", (page - 1) * limit);

        return contentDao.getCourses(map);
    }

    @Override
    public int getCourseListCount(boolean isAdmin, String academyId, SelectCourseListReqDto selectCourseListReqDto) {
        HashMap<String, Object> map = new HashMap<>();
        setSearchCondition(map, isAdmin, academyId, selectCourseListReqDto);

        return contentDao.getCoursesCnt(map);
    }

    @Override
    public void updateCourseActivation(String courseId, boolean isActive) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        map.put("courseActivation", isActive);

        contentDao.updateCourseActivation(map);
    }

    @Override
    public Course getCourseById(String courseId) {
        return contentDao.getCourseById(courseId);
    }

    @Override
    public void updateCourse(String teacherId, String academyId, String courseId, String courseName, String courseSubject, String isProfileChanged, MultipartFile courseImage, String courseInfo, String coursePrice, String courseTag, String courseDiff) {
        HashMap<String, Object> map = new HashMap<>();

        String newCourseProfilePath = "";
        boolean isNewProfile = Boolean.parseBoolean(isProfileChanged);
        if (isNewProfile) {
            newCourseProfilePath = uploadNewCourseProfile(courseId, courseImage);

            String newProfileOriginName = courseImage.getOriginalFilename();

            map.put("courseProfileOriginName", newProfileOriginName);
            map.put("courseProfilePath", newCourseProfilePath);
        }

        map.put("courseId", courseId);
        map.put("courseName", courseName);
        map.put("courseSubject", courseSubject);
        map.put("courseInfo", courseInfo);
        map.put("coursePrice", Integer.parseInt(coursePrice));
        map.put("courseTag", courseTag);
        map.put("courseDiff", courseDiff);

        contentDao.updateCourse(map);
    }

    private String uploadNewCourseProfile(String courseId, MultipartFile courseImage) {
        if (courseImage.getOriginalFilename() == null) {
            throw new IllegalArgumentException("courseImage 또는 파일 이름이 null입니다.");
        }
        String fileName = "profile." + getExtension(courseImage.getOriginalFilename());

        System.out.println("🚀 ====> courseId : " + courseId);
        System.out.println("🚀 ====> courseImage : " + courseImage);
        System.out.println("🚀 ====> fileName : " + fileName);

        // TODO 1. courseId로 courseProfilePath를 가져온다.
        String asisProfileImagePath = contentDao.getAsIsProfileImagePath(courseId);
        System.out.println("🚀 ====> asisProfileImagePath : " + asisProfileImagePath);

        // TODO 2. 가져온 courseProfilePath를 이용해 S3에서 해당 파일을 삭제한다.
        s3Uploader.deleteFileFromS3(asisProfileImagePath);

        // TODO 3. 새로운 courseImage를 S3에 업로드한다.
        String newProfileImageName = "profile." + getExtension(courseImage.getOriginalFilename());
        return s3Uploader.uploadFileToS3(courseImage, "course/" + courseId + "/profile/" + newProfileImageName);
    }

    private String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

    private void setSearchCondition(HashMap<String, Object> map, boolean isAdmin, String academyId, SelectCourseListReqDto selectCourseListReqDto) {
        // 어드민 여부에 따른 검색 조건
        map.put("isAdmin", isAdmin);
        map.put("academyId", academyId);

        // 상세 검색 조건
        map.put("useStatus", selectCourseListReqDto.getUseStatus()); // 전체("", "all"), 공개("used"), 비공개("unused")
        int searchCondition = selectCourseListReqDto.getSearchCondition(); // 0:전체, 1:과정명, 2:담당자, 3:등록일
        map.put("searchCondition", searchCondition);
        if (searchCondition == 1 || searchCondition == 2) {
            map.put("searchKeyword", selectCourseListReqDto.getSearchKeyword());
        } else if (searchCondition == 3) {
            map.put("startDate", selectCourseListReqDto.getStartDate());
            map.put("endDate", selectCourseListReqDto.getEndDate());
        }
    }
}