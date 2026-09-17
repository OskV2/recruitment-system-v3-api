package com.szponty.recruitment_system;

import com.szponty.recruitment_system.attachment.service.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URL;
import java.time.Duration;

@SpringBootApplication
@EnableAsync
public class RecruitmentSystemApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				SpringApplication.run(RecruitmentSystemApplication.class, args);
		System.out.println("Running");

		S3Client s3Client = context.getBean(S3Client.class);
		System.out.println(s3Client.listBuckets());

		StorageService storageService = context.getBean(StorageService.class);
		URL url = storageService.generateUploadUrl("test/manual-check.txt", Duration.ofMinutes(5));
		System.out.println(url);
	}

}
