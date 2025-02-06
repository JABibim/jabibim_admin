package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.dto.OAuth.CustomOAuth2User;
import com.jabibim.admin.dto.OAuth.GoogleResponse;
import com.jabibim.admin.dto.OAuth.OAuth2Response;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final TeacherMapper dao;

    public CustomOAuth2UserService(TeacherMapper dao) {
        this.dao = dao;
    }

    //DefaultOAuth2UserService OAuth2UserService의 구현체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    //사용자가 Google 로그인(OAuth인증)을 하면 DefaultOAuth2UserService를 호출해 사용자 정보를 가져옴. 이과정에서 CustomOAuth2UserService가 오버라이드 되어 실행된다
        //Google에서 가져온 사용자 정보를 기반으로 데이터베이스 조회 및 삽입/업데이트가 된다.

        OAuth2User oAuth2User = super.loadUser(userRequest);  //<- userRequest Google 사용자 정보 데이터 넘어옴
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();   //naver인지 google인지 판단
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }
        Teacher existData = dao.getTeacherByProviderId(oAuth2Response.getProviderId());   //DB에 이미 존재하는지 안하는지

        String role = "ROLE_LECTURER";
        if(existData == null) {            //처음 로그인할때

            Teacher teacher = new Teacher();
            teacher.setTeacherId(UUID.randomUUID().toString());
            teacher.setTeacherEmail(oAuth2Response.getEmail());
            teacher.setTeacherName(oAuth2Response.getName());
            teacher.setProvider(oAuth2Response.getProvider());
            teacher.setProviderId(oAuth2Response.getProviderId());
            teacher.setOauthPicture(oAuth2Response.getAccessTokenScopes());

            dao.insertOauthTeacher(teacher);
        }
        else{       //존재하면
            existData.setTeacherName(oAuth2Response.getName());
            //TODO 프로필 이미지 업뎃 추가


            role = existData.getAuthRole();

            dao.updateOauthTeacher(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}
