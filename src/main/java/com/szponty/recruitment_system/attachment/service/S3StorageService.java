package com.szponty.recruitment_system.attachment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class S3StorageService implements StorageService {

    private final S3Presigner s3Presigner;
    private final String bucket;

    public S3StorageService(
            S3Presigner s3Presigner,
            @Value("${app.s3.bucket}") String bucket
    ) {
        this.s3Presigner = s3Presigner;
        this.bucket = bucket;
    }

    @Override
    public URL generateUploadUrl(String key, Duration ttl) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(putObjectPresignRequest).url();
    }

    @Override
    public URL generateDownloadUrl(String key, Duration ttl) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url();
    }
}
