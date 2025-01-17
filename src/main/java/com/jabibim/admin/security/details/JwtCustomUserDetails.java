package com.jabibim.admin.security.details;

import com.jabibim.admin.dto.StudentUserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class JwtCustomUserDetails implements UserDetails {

  private final StudentUserVO user;

  public JwtCustomUserDetails(StudentUserVO user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();

    collection.add((GrantedAuthority) () -> user.getAuthRole());

    return collection;
  }

  @Override
  public String getPassword() {
    return user.getStudentPassword();
  }

  @Override
  public String getUsername() {
    return user.getStudentEmail();
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
}
