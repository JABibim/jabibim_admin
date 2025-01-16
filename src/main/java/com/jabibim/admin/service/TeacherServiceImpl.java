package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import com.jabibim.admin.dto.TeacherProfileDTO;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;

  
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherMapper teacherMapper;
    private TeacherMapper dao;

    public TeacherServiceImpl(TeacherMapper dao, TeacherMapper teacherMapper) {
        this.dao = dao;
        this.teacherMapper = teacherMapper;
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
    public String saveProfileImage(MultipartFile file, String uploadDir) throws IOException{
        //디렉토리 생성
        File dir = new File(uploadDir);
        System.out.println("uploadDir==================" + uploadDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        //파일 이름 설정(중복 방지를 위해 UUID 사용(
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension; //이름 중복 방지용 랜덤 UUID

        //파일 저장
        File saveFolder = new File(dir, newFileName);
        file.transferTo(saveFolder);

        return newFileName;
    }

    public int updateProfileImage(String teacherId, String teacherImgName)  {
        return teacherMapper.updateProfileImage(teacherId, teacherImgName);
    }

    @Override
    public List<TeacherCareer> getcareerList(boolean isAdmin, String academyId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("isAdmin", isAdmin);
        params.put("academyId", academyId);

        return dao.getcareerList(params);
    }

    @Override
    public void resetAllCareers() {
        dao.resetAllCareers();
    }

    @Override
    public int updateCareerActive(String careerName, int displayStatus) {
        System.out.println("updateCareerActive 호출됨: careerName=" + careerName + ", displayStatus=" + displayStatus);

        return dao.updateCareerActive(careerName, displayStatus);
    }


}
