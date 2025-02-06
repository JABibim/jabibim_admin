package com.jabibim.admin.dto.OAuth;

public interface OAuth2Response {   //google에서 보낸 데이터를 받을 틀

    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();
    //프로필경로
    String getAccessTokenScopes();

}