package com.example.userauth.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void firestore() throws IOException {
        // 클래스패스 루트에서 직접 파일을 찾습니다
        InputStream serviceAccount = getClass().getResourceAsStream("/tabtalk-c3dc3-firebase-adminsdk-fbsvc-25c323ffdc.json");

        // 입력 스트림이 null인지 확인 (디버깅용)
        if (serviceAccount == null) {
            throw new IOException("Firebase 서비스 계정 키 파일을 찾을 수 없습니다");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // 이미 초기화되었는지 확인 (앱 재시작 시 오류 방지)
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}