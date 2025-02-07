package com.jabibim.admin.func;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.calendar.model.Calendar;
import com.jabibim.admin.dto.auth.response.GoogleAuthTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GoogleCalendar {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.token.uri}")
    private String tokenUri;

    @Value("${google.authorization.uri}")
    private String authorizationUri;

    public Calendar createGoogleCalendar(String summary, String description, String timeZone) {
        Calendar googleCalendar = new Calendar();
        googleCalendar.setSummary(summary);
        googleCalendar.setDescription(description);
        googleCalendar.setTimeZone(timeZone);

        return googleCalendar;
    }

    public GoogleAuthTokenResponse requestTokens(String code) {
        try {
            URI tokenUri = new URI(this.tokenUri);

            String body = "code=" + code + "&client_id=" + this.clientId + "&client_secret=" + this.clientSecret + "&redirect_uri=" + this.redirectUri + "&grant_type=authorization_code";

            HttpRequest request = HttpRequest.newBuilder().uri(tokenUri).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(body)).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            // Access Token 및 Refresh Token 추출
            String accessToken = jsonResponse.get("access_token").asText();
            String refreshToken = jsonResponse.has("refresh_token") ? jsonResponse.get("refresh_token").asText() : null;

            long expiresIn = jsonResponse.get("expires_in").asLong();

            return new GoogleAuthTokenResponse(accessToken, refreshToken, expiresIn);
        } catch (Exception e) {
            throw new RuntimeException("인증 토큰을 얻어오는데 실패했습니다.", e);
        }
    }

    public GoogleAuthTokenResponse getNewAccessToken(String refreshToken) {
        System.out.println("===========getNewAccessToken Controller================");
        try {
            URI tokenUri = new URI(this.tokenUri);

            String body = "client_id=" + this.clientId + "&client_secret=" + this.clientSecret + "&refresh_token=" + refreshToken + "&grant_type=refresh_token";

            HttpRequest request = HttpRequest.newBuilder().uri(tokenUri).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(body)).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());
            // System.out.println("jsonResponse = " + jsonResponse); // jsonResponse = {"access_token":"ya29.a0AXeO80Q3DLcGbdwShhJvHge1FbkbLMGACOpraTZJikh6wZX8YLFxPXbZy1yLWmNhjUSnufY0EGqaQVqQkW1ySSHc__WfLi1a3qgu9gmVk6BVNtbu8MkifSZNLyJ9FU5jBWqNeO0GAQuZkX78eBYd4rcykkuedfAJ6_7LZbhxbQaCgYKAT0SARASFQHGX2Mi_hX8FOJIIkTzF413UHw6kA0177","expires_in":3599,"scope":"https://www.googleapis.com/auth/calendar","token_type":"Bearer"}

            String accessToken = jsonResponse.get("access_token").asText();
            long expiresIn = jsonResponse.get("expires_in").asLong();

            return new GoogleAuthTokenResponse(accessToken, refreshToken, expiresIn);
        } catch (Exception e) {
            throw new RuntimeException("재발급한 인증 토큰을 얻어오는데 실패했습니다.", e);
        }
    }
}