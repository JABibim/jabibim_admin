package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import com.jabibim.admin.dto.TeacherProfileDTO;
import com.jabibim.admin.func.Files;
import com.jabibim.admin.func.S3Uploader;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;


@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherMapper dao;
    private final S3Uploader s3Uploader;

    public TeacherServiceImpl(TeacherMapper dao, TeacherMapper teacherMapper, S3Uploader s3Uploader) {
        this.dao = dao;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public int getTeacherCount(String state, String search_field, String search_word) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("state", state);
        params.put("search_field", search_field);
        params.put("search_word", search_word);
        return dao.getTeacherCount(params);
    }

    @Override
    public List<Teacher> getTeacherList(int page, int limit, String academyId, boolean isAdmin, String state, String search_field, String search_word) {
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;

        HashMap<String, Object> params = new HashMap<>();
        params.put("start", startrow);
        params.put("end", endrow);
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("state", state);
        params.put("search_field", search_field);
        params.put("search_word", search_word);

        return dao.getTeacherList(params);
    }

    @Override
    public TeacherProfileDTO teacherInfo(String id) {
        return dao.teacherInfo(id);
    }

    @Override
    public int update(Teacher teacher) {
        return dao.update(teacher);
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        return dao.getTeacherById(teacherId);
    }

    @Override
    public int updatePassword(Teacher teacher) {
        return dao.updatePassword(teacher);
    }

    @Override
    public List<TeacherCareer> getcareerList(boolean isAdmin, String academyId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("isAdmin", isAdmin);
        params.put("academyId", academyId);

        return dao.getcareerList(params);
    }

    @Override
    public int updateCareerActive(String asisCareerId, String tobeCareerId) {
        return dao.updateCareerActive(asisCareerId, tobeCareerId);
    }

    @Override
    @Transactional
    public void insertCareer(String academyId, String teacherId, String careerName, MultipartFile careerImage) {
        String newCareerId = UUIDGenerator.getUUID();
        if (careerImage.getOriginalFilename() == null) {
            throw new IllegalArgumentException("careerImage 또는 파일 이름이 null입니다.");
        }
        String fileName = "career." + Files.getExtension(careerImage.getOriginalFilename());
        String uploadPath = academyId + "/career/" + newCareerId + "/" + fileName;

        String uploadedPath = s3Uploader.uploadFileToS3(careerImage, uploadPath);

        HashMap<String, Object> map = new HashMap<>();
        map.put("careerId", newCareerId);
        map.put("careerName", careerName);
        map.put("careerFileOriginName", careerImage.getOriginalFilename());
        map.put("careerFilePath", uploadedPath);
        map.put("teacherId", teacherId);
        map.put("academyId", academyId);

        dao.insertCareer(map);
    }

    @Override
    public String getUploadPathByCareerId(String careerId) {
        return dao.getUploadPathByCareerId(careerId);
    }
}
