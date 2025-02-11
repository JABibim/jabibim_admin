package com.jabibim.admin.service;

public interface DashboardService {
    public int getWaitRefundCount(boolean isAdmin, String academyId);

    public Object getCourseStatus(boolean isAdmin, String academyId);
}
