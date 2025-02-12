package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.dto.*;
import org.springframework.stereotype.Service;

import com.jabibim.admin.mybatis.mapper.ClassMapper;
import com.jabibim.admin.mybatis.mapper.CourseMapper;
import com.jabibim.admin.mybatis.mapper.OrdersMapper;
import com.jabibim.admin.mybatis.mapper.StudyHistoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseDao;

    private final ClassMapper classDao;

    private final StudyHistoryMapper studyHistoryDao;

    private final OrdersMapper ordersDao;

    @Override
    public List<CourseInfoVO> getCourseInfoList(String academyId) {
        return courseDao.getCourseInfoList(academyId);
    }

    @Override
    public CourseDetailVO getCourseDetail(String courseId, String academyId) {
        CourseDetailVO courseDetail = courseDao.getCourseDetail(courseId, academyId);

        List<ClassInfoVO> classList = classDao.getClassList(courseId, academyId);

        int classCount = classDao.getClassCount(courseId, academyId);

        courseDetail.setClassList(classList);
        courseDetail.setClassCount(classCount);

        return courseDetail;
    }

    @Override
    public List<PurchasedCourseVO> getPurchasedCourseList(String studentId, String academyId) {
        List<PurchasedCourseVO> purchasedCourseList = ordersDao.getPurchasedCourseList(studentId, academyId);

        return purchasedCourseList;
    }

    @Override
    public ClassDetailVO getClassDetail(String classId, String academyId) {
        ClassDetailVO classDetail = classDao.getClassDetail(classId, academyId);
        System.out.println("==> classDetail : " + classDetail);

        return classDetail;
    }
}
