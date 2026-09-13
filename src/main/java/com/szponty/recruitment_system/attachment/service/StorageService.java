package com.szponty.recruitment_system.attachment.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

public interface StorageService {
    URL generateUploadUrl(String key, Duration ttl);
    URL generateDownloadUrl(String key, Duration ttl);
    InputStream downloadObject(String key);
}
