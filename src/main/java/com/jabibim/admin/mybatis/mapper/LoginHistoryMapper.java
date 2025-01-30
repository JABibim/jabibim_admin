package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.dto.SignInHistListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface LoginHistoryMapper {
  int getLoginHistCount(HashMap<String, Object> map);

  List<SignInHistListVO> getLoginHistList(HashMap<String, Object> map);

  void insertLoginHistory(HashMap<String, Object> map);
}
