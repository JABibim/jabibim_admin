package com.jabibim.admin.security.service;

import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.mybatis.mapper.StudentMapper;
import com.jabibim.admin.security.details.JwtCustomUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

  @NonNull
  private StudentMapper dao;
  private final Logger logger = LoggerFactory.getLogger(JwtUserDetailService.class);

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    logger.info("loadUserByUsername");
    String email = username.split(",")[0];
    String academyId = username.split(",")[1];

    StudentUserVO user = dao.getStudentByEmail(email, academyId);
    logger.info("JwtUserDetailService" + user.toString());

    if (user != null) {
      return new JwtCustomUserDetails(user);
      // userDetails 에 담아서 넘기면 AuthenticationManager 가 검증 실시
    }
    return null;
  }
}
