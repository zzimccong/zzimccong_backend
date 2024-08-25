package com.project.zzimccong.service.s3;

import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.s3.S3Repository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Repository s3Repository;
    private final RestaurantRepository restaurantRepository;

    @Value("${aws.s3.bucket.url}") // S3 버킷의 URL을 설정 파일에서 읽어옴
    private String s3BucketUrl;

    public S3ServiceImpl(S3Repository s3Repository, RestaurantRepository restaurantRepository) {
        this.s3Repository = s3Repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public String uploadRestaurantPhoto(MultipartFile file, Long restaurantId) {
        String originalFilename = file.getOriginalFilename();
        String fileName = "restaurants/" + restaurantId + "_" + originalFilename;
        File convertedFile = convertMultiPartFileToFile(file);
        s3Repository.uploadFileToS3(fileName, convertedFile.getAbsolutePath());
        convertedFile.delete();

        // 절대 경로로 변환
        String fileUrl = s3BucketUrl + "/" + fileName;

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // 필요에 따라 레스토랑의 사진 URL 필드를 업데이트합니다.
        restaurant.setMainPhotoUrl(fileUrl); // 예시로 메인 사진을 업데이트
        restaurantRepository.save(restaurant);

        return fileUrl;
    }


    @Override
    public void deleteRestaurantPhoto(String photoUrl, Long restaurantId) {
        // S3에서 파일 삭제
        String fileName = photoUrl.replace(s3BucketUrl + "/", "");
        s3Repository.deleteFileFromS3(fileName);

        // 데이터베이스에서 해당 URL을 null로 설정
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (photoUrl.equals(restaurant.getMainPhotoUrl())) {
            restaurant.setMainPhotoUrl(null);
        } else if (photoUrl.equals(restaurant.getPhoto1Url())) {
            restaurant.setPhoto1Url(null);
        } else if (photoUrl.equals(restaurant.getPhoto2Url())) {
            restaurant.setPhoto2Url(null);
        } else if (photoUrl.equals(restaurant.getPhoto3Url())) {
            restaurant.setPhoto3Url(null);
        } else if (photoUrl.equals(restaurant.getPhoto4Url())) {
            restaurant.setPhoto4Url(null);
        } else if (photoUrl.equals(restaurant.getPhoto5Url())) {
            restaurant.setPhoto5Url(null);
        }

        restaurantRepository.save(restaurant);
    }


    @Override
    public String getRestaurantPhotoUrl(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // 필요에 따라 레스토랑의 사진 URL을 반환합니다.
        return restaurant.getMainPhotoUrl();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipart file to file", e);
        }
        return convertedFile;
    }
}
