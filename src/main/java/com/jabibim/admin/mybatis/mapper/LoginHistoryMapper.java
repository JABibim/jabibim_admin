package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.dto.SignInHistListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface LoginHistoryMapper {
  int getSignInHistCount(HashMap<String, Object> map);

  List<SignInHistListVO> getSignInHistList(HashMap<String, Object> map);
}
