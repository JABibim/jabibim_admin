package com.jabibim.admin.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCalendarConfig {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Bean
    public GoogleCredential amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
