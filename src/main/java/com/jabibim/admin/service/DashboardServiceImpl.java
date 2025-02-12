package com.jabibim.admin.service;

import com.jabibim.admin.mybatis.mapper.DashboardMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private DashboardMapper dao;

    public DashboardServiceImpl(DashboardMapper dao){
        this.dao = dao;
    }

    @Override
    public int getWaitRefundCount(boolean isAdmin, String academyId) {
        return dao.getWaitRefundCount(isAdmin, academyId);
    }

    @Override
    public Map<String, Object> getCourseStatus(boolean isAdmin, String academyId) {
        return dao.getCourseStatus(isAdmin, academyId);
    }
}
