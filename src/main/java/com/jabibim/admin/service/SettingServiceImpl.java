package com.jabibim.admin.service;

import com.jabibim.admin.domain.Academy;
import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.dto.setting.AddAcademyDto;
import com.jabibim.admin.dto.setting.AddTeacherDto;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.AcademyMapper;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {
    @Value("${admin.settingPassword}")
    private String settingPassword;
    private final PasswordEncoder passwordEncoder;
    private final AcademyMapper academyDao;
    private final TeacherMapper teacherDao;

    public SettingServiceImpl(PasswordEncoder passwordEncoder, AcademyMapper academyDao, TeacherMapper teacherDao) {
        this.passwordEncoder = passwordEncoder;
        this.academyDao = academyDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public boolean checkAdminPass(String password) {
        return settingPassword.equals(password);
    }

    @Override
    public List<Academy> getAcademyList() {
        return academyDao.getAcademyList();
    }

    @Override
    public int getAcademyCountByBusinessRegisNum(String businessRegisNum) {
        return academyDao.getAcademyCountByBusinessRegisNum(businessRegisNum);
    }

    @Override
    public Academy addAcademy(AddAcademyDto addAcademyDto) {
        Academy academy = new Academy();

        String newAcademyId = UUIDGenerator.getUUID();
        academy.setAcademyId(newAcademyId);
        academy.setAcademyName(addAcademyDto.getAcademyName());
        academy.setAcademyAddress(addAcademyDto.getAcademyAddress());
        academy.setAcademyDetailAddr(addAcademyDto.getAcademyDetailAddr());
        academy.setAcademyPostalcode(addAcademyDto.getAcademyPostalcode());
        academy.setAcademyOwner(addAcademyDto.getAcademyOwner());
        academy.setAcademyContect(addAcademyDto.getAcademyContect());
        academy.setBusinessRegisNum(addAcademyDto.getBusinessRegisNum());
        academy.setRegisteredAt(LocalDate.parse(addAcademyDto.getRegisteredAt()).atStartOfDay());

        academyDao.addAcademy(academy);

        return academyDao.getAcademyById(newAcademyId);
    }

    @Override
    public List<Teacher> getTeacherList(String academyId) {
        return teacherDao.getTeacherListByAcademyId(academyId);
    }

    @Override
    public void addTeacher(AddTeacherDto addTeacherDto) {
        Teacher teacher = new Teacher();

        teacher.setAcademyId(addTeacherDto.getAcademyId());
        teacher.setTeacherId(UUIDGenerator.getUUID());
        teacher.setTeacherName(addTeacherDto.getTeacherName());
        teacher.setTeacherPhone(addTeacherDto.getTeacherPhone());
        teacher.setTeacherEmail(addTeacherDto.getTeacherEmail());
        teacher.setTeacherPassword(passwordEncoder.encode(addTeacherDto.getTeacherPassword()));
        teacher.setAuthRole(addTeacherDto.getAuthRole().getName());

        teacherDao.addTeacher(teacher);
    }
}
