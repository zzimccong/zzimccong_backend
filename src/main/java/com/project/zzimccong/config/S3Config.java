package com.project.zzimccong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of("ap-northeast-3")) // region을 직접 설정하거나 properties에서 불러올 수 있습니다.
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }
}
