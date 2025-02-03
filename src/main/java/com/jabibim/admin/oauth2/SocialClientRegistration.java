package com.jabibim.admin.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Component;

@Component     // 이 클래스를 Spring Bean으로 등록
public class SocialClientRegistration {

    public ClientRegistration googleClientRegistration() {       //서비스별 OAuth2 클라이언트의 등록 정보를 가지는 클래스

        return ClientRegistration.withRegistrationId("google")
                .clientId("547757846015-dlmrd94327s9bnojsvpjekr0ac7t13vl.apps.googleusercontent.com")
                .clientSecret("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")
                .redirectUri("http://localhost:5000/login/oauth2/code/google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")   //구글은 provider 설정 안해도되는데 커스텀에서는 다해줘야함
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .issuerUri("https://accounts.google.com")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .build();
    }
}
