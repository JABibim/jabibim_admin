package com.jabibim.admin.mybatis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.domain.LoginHistory;
import com.jabibim.admin.dto.LoginHistListVO;

@Mapper
public interface LoginHistoryMapper {
  int getLoginHistCount(HashMap<String, Object> map);

  List<LoginHistListVO> getLoginHistList(HashMap<String, Object> map);

  void insertLoginHistory(LoginHistory loginHistory);

}
