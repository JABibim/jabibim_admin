package com.jabibim.admin.security.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import com.jabibim.admin.security.dto.AccountContext;
import com.jabibim.admin.security.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {
    private final TeacherMapper teacherDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherDao.getTeacherByEmail(username);

        if (teacher == null) {
            throw new UsernameNotFoundException("No user found with username" + username);
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(teacher.getAuthRole()));

        AccountDto accountDto = new AccountDto();
        accountDto.setId(teacher.getTeacherId());
        accountDto.setUsername(teacher.getTeacherEmail());
        accountDto.setPassword(teacher.getTeacherPassword());
        accountDto.setRoles(teacher.getAuthRole());

        return new AccountContext(accountDto, authorities);
    }
}
