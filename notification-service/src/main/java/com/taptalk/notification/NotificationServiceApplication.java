package com.taptalk.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(
			new ClassPathResource("firebase-credentials.json").getInputStream()
		);
		
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(credentials)
			.build();
			
		return FirebaseApp.initializeApp(options);
	}
}
 