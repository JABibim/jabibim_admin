package com.jabibim.admin.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface DashboardMapper {

   public int getWaitRefundCount(boolean isAdmin, String academyId);

   public Map<String, Object> getCourseStatus(boolean isAdmin, String academyId);
}
