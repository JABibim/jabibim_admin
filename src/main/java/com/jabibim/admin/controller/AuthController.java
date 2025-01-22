package com.jabibim.admin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.auth.response.GoogleAuthTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    // TODO 아래 값들 다 환경변수로 빼야함 개발단계에서만 하드코딩
    private static final String CLIENT_ID = "357050162427-umuoaikm8vui92ukl3mhpj0khfsfm85h.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:5000/auth/google/callback";
    private static final String AUTHORIZATION_URI = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

    /**
     * 구글 OAuth2 인증 요청 ( 인증정보를 가져오기 위한 API )
     */
    @GetMapping(value = "/oauth2/authorize")
    public String authorize() {
        String scope = "https://www.googleapis.com/auth/calendar";
        String responseType = "code";

        String url = AUTHORIZATION_URI + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=" + responseType + "&scope=" + scope + "&access_type=offline";

        return "redirect:" + url;
    }


    @GetMapping(value = "/google/callback")
    public String authGoogleCallback(@RequestParam("code") String code) {
        GoogleAuthTokenResponse tokens = requestTokens(code);
        // 🚀🚀 accessToken : ya29.a0ARW5m76Sn3wTpp3D3izDCLLzh-xYsVPKwoHCmg8OOlmo9bZhSotRRQju2NQ5MweDTutIRbjsJOTfJZ6Zhh-BsWEGug4Nq3AypwM4KWKFaRsPlbCk77pXMyGoGJ3dolgvxpgIj9N37vt35qfttpDpX4QZAgxvuZT1iPRx9MxxaCgYKAfgSARASFQHGX2MiLBT8ylZrCSKVQNH8oV2MIA0175
        // 🚀🚀 refreshToken : 1//0eU8bqhyedebjCgYIARAAGA4SNwF-L9Ir5fO5I61eW5xe2DP6--0fWYCkd-5Ob-2NmBii-ecM88q6UyvuVBzedZLNhPaEaHJOD7k
        // 🚀🚀 expiresIn : 3599

        return "calendar/popup-success";
    }


    // TODO func으로 옮기기
    private GoogleAuthTokenResponse requestTokens(String code) {
        try {
            URI tokenUri = new URI(TOKEN_URI);

            String body = "code=" + code + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + REDIRECT_URI + "&grant_type=authorization_code";

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
}
