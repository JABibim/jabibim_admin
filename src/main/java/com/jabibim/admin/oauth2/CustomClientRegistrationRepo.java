package com.jabibim.admin.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration      // 이 클래스는 설정 클래스임을 나타냄
public class CustomClientRegistrationRepo {

    private final SocialClientRegistration socialClientRegistration;

    public CustomClientRegistrationRepo(SocialClientRegistration socialClientRegistration) {

        this.socialClientRegistration = socialClientRegistration;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(socialClientRegistration.googleClientRegistration());
        //InMemory랑 JDBC 이렇게 저장하는거 두개가 있는데 구글 하나밖에 없으니깐 메모리 안넘침 그래서 이거쓰는거임

    }
}
