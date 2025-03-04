package com.jabibim.admin.front.security.custom;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jabibim.admin.dto.StudentUserVO;

public class JwtCustomUserDetails implements UserDetails {

  private final String email;
  private final String password;
  private Collection<? extends GrantedAuthority> authorities;
  private final StudentUserVO user;

  public JwtCustomUserDetails(StudentUserVO user, Collection<? extends GrantedAuthority> authorities) {
    this.user = user;
    this.email = user.getStudentEmail();
    this.password = user.getStudentPassword();
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public StudentUserVO getUser() {
    return user;
  }
}
