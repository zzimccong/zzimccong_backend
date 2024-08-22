package com.project.zzimccong.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoConfig {



    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoConfig() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String fetchAccessToken(String authorizeCode) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&code=" + authorizeCode +
                "&client_secret=" + clientSecret;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    reqURL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                return rootNode.path("access_token").asText();
            } else {
                log.error("액세스 토큰을 가져오는 데 실패했습니다: {}", response.getStatusCode());
                throw new RuntimeException("액세스 토큰을 가져오는 데 실패했습니다: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("액세스 토큰을 가져오는 중 오류 발생", e);
            throw new RuntimeException("액세스 토큰을 가져오는 중 오류 발생", e);
        }
    }

    public Map<String, Object> fetchUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    reqURL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode properties = rootNode.path("properties");
                JsonNode kakaoAccount = rootNode.path("kakao_account");

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("nickname", properties.path("nickname").asText());
                userInfo.put("email", kakaoAccount.path("email").asText());

                return userInfo;
            } else {
                log.error("사용자 정보를 가져오는 데 실패했습니다: {}", response.getStatusCode());
                throw new RuntimeException("사용자 정보를 가져오는 데 실패했습니다: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("사용자 정보를 가져오는 중 오류 발생", e);
            throw new RuntimeException("사용자 정보를 가져오는 중 오류 발생", e);
        }
    }
}
