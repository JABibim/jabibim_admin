package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherMapper teacherMapper;
    private TeacherMapper dao;

    public TeacherServiceImpl(TeacherMapper dao, TeacherMapper teacherMapper) {
        this.dao = dao;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public Teacher teacherInfo(String id) {
        return dao.teacherInfo(id);
    }


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


}
