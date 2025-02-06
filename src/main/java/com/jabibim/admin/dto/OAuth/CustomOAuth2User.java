package com.jabibim.admin.dto.OAuth;

import com.jabibim.admin.security.dto.AccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;

    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {

        this.oAuth2Response = oAuth2Response;
        this.role = role;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return role;
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return oAuth2Response.getName();
    }

    public String getUsername() {

        return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
    }

    public AccountDto toAccountDto() {
        return AccountDto.builder()
                .id(UUID.randomUUID().toString()) // OAuth 인증에서는 ID가 없을 수도 있음
                .academyId(null) // 필요하면 설정
                .username(oAuth2Response.getProviderId()) // OAuth 제공자의 고유 ID
                .name(oAuth2Response.getName()) // 사용자 이름
                .email(oAuth2Response.getEmail()) // 이메일
                .password(null) // 비밀번호는 OAuth 로그인에서는 필요 없음
                .roles(role) // OAuth 권한 정보
                .build();
    }

}
