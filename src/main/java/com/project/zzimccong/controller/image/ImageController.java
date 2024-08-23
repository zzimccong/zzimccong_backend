package com.project.zzimccong.controller.image;

import com.project.zzimccong.service.Image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/api/image-to-base64")
    public ResponseEntity<String> getImageAsBase64(@RequestParam String url) {
        try {
            String base64Image = imageService.convertImageToBase64(url);
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error converting image to Base64");
        }
    }
}
