package com.jabibim.admin.front.security.custom;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;
import com.jabibim.admin.mybatis.mapper.StudentMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

  @NonNull
  private StudentMapper dao;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public UserDetails loadUserByUsername(String username) {
    // 기본 메서드는 남겨두되 사용하지 않음
    throw new UnsupportedOperationException("기본 loadUserByUsername은 사용할 수 없습니다.");
  }

  // 새로운 메서드 추가
  public UserDetails loadUserByLoginRequest(LoginRequest loginRequest) {
    logger.info("loadUserByLoginRequest");

    StudentUserVO user = dao.getStudentByEmail(
        loginRequest.getEmail(),
        loginRequest.getAcademyId());
    logger.info("JwtUserDetailService " + user.toString());

    if (user == null) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    Collection<? extends GrantedAuthority> authorities = Collections
        .singletonList(new SimpleGrantedAuthority(user.getAuthRole()));

    return new JwtCustomUserDetails(user, authorities);
  }
}
