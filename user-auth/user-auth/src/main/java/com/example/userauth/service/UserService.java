package com.example.userauth.service;

import com.example.userauth.model.User;
import com.example.userauth.repository.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final Firestore db = FirestoreClient.getFirestore();

    @Autowired
    private UserRepository userRepository;

    /**
     * Firestore에서 사용자 목록을 가져옵니다.
     */
    public List<User> getAllUsers(int page, int limit, String search, String status, String sortBy, String sortOrder) {
        List<User> users = new ArrayList<>();
        try {
            CollectionReference usersRef = db.collection("users");
            Query query = usersRef;

            // 검색 기능 적용 (이름 또는 이메일에 검색어 포함)
            if (search != null && !search.isEmpty()) {
                query = query.whereGreaterThanOrEqualTo("user_name", search)
                        .whereLessThanOrEqualTo("user_name", search + "\uf8ff");
            }

            // 상태 필터링 적용
            if (status != null && !status.equals("all")) {
                query = query.whereEqualTo("status", status);
            }

            // 정렬 적용
            query = query.orderBy(sortBy, sortOrder.equals("desc") ? Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                    .limit(limit);

            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot querySnapshot = future.get();

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                users.add(convertToUser(document));
            }

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("사용자 목록 가져오기 오류: " + e.getMessage());
        }

        return users;
    }

    /**
     * Firestore 문서를 User 객체로 변환합니다.
     */
    private User convertToUser(DocumentSnapshot document) {
        User user = new User();
        user.setUserId(document.getId());
        user.setUserName(document.getString("user_name"));
        user.setEmail(document.getString("email"));
        user.setVerify((List<String>) document.get("verify"));
        user.setCreatedAt(document.getTimestamp("created_at"));
        user.setLastLoginAt(document.getTimestamp("last_login_at"));
        user.setReportCount(document.getLong("report_count") != null ? document.getLong("report_count").intValue() : 0);
        user.setCountryCode(document.getString("country_code"));

        return user;
    }

    /**
     * Firestore에서 전체 사용자 수를 조회합니다.
     */
    public int getTotalUserCount() {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.size();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("사용자 수 조회 오류: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Firestore에서 특정 사용자 정보를 가져옵니다 (상세 정보 포함).
     */
    public User getUserDetailById(String userId) {
        try {
            DocumentReference docRef = db.collection("users").document(userId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return convertToUserWithDetails(document);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("사용자 상세 정보 가져오기 오류: " + e.getMessage());
            return null;
        }
    }

    /**
     * Firestore 문서를 User 객체로 변환합니다 (상세 정보 포함).
     */
    private User convertToUserWithDetails(DocumentSnapshot document) {
        User user = new User();
        user.setUserId(document.getId());
        user.setUserName(document.getString("user_name"));
        user.setEmail(document.getString("email"));
        user.setProfilePicture(document.getString("profile_picture"));
        user.setPostCount(document.getLong("post_count") != null ? document.getLong("post_count").intValue() : 0);
        user.setReportCount(document.getLong("report_count") != null ? document.getLong("report_count").intValue() : 0);
        user.setBio(document.getString("bio"));
        user.setFollowers((List<String>) document.get("followers"));
        user.setFollowing((List<String>) document.get("following"));
        user.setCreatedAt(document.getTimestamp("created_at"));
        user.setLastLoginAt(document.getTimestamp("last_login_at"));
        String phone_number = (String) document.get("phone_number");
        user.setPhoneNumber(document.getString("phone_number"));
        user.setLocation(document.getString("location"));
        user.setGender(document.getString("gender"));
        user.setBirthdate(document.getString("birthdate"));
        user.setCountryCode(document.getString("country_code"));
        user.setNativeLanguage(document.getString("native_language"));
        user.setPreferredLanguage(document.getString("preferred_language"));
        List<String> interestKeywords = (List<String>) document.get("interest_keywords");
        user.setInterestKeywords((List<String>) document.get("interest_keywords"));

        // 콘솔에 출력
        System.out.println("Interest Keywords: " + interestKeywords);
        System.out.println("phone_number: " + phone_number);
        return user;
    }

    public String updateUserStatus(String userId, String status, String reason, Integer durationDays) {
        // 사용자 조회
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 상태 변경
        User foundUser = user.get();  // Optional에서 User 객체를 꺼냄
        foundUser.setStatus(status);
        foundUser.setReason(reason);
        foundUser.setDurationDays(durationDays);

        // 상태 변경된 사용자 정보 저장
        userRepository.save(foundUser);  // User 객체를 저장
        return "사용자 상태가 변경되었습니다";
    }
}
