package com.project.zzimccong.service.Image;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;

@Service
public class ImageService {

    // ImageService.java
    public String convertImageToBase64(String imageUrl) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(imageUrl);
            byte[] imageBytes = restTemplate.getForObject(uri, byte[].class);
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            // 로그 추가
            e.printStackTrace();
            throw new Exception("Failed to convert image to Base64", e);
        }
    }

}
