package com.example.userauth.service;

import com.example.userauth.dto.UserListResponse;
import com.example.userauth.model.Report;
import com.example.userauth.model.User;
import com.example.userauth.repository.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final Firestore db = FirestoreClient.getFirestore();


    // Firestore에서 사용자 목록을 가져옴
    public UserListResponse getAllUsers(int page, int limit, String search, String status, String sortBy, String sortOrder) {
        List<User> users = new ArrayList<>();
        int totalCount = 0;
        int currentPage = page;
        int totalPages = 0;

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

            // 총 사용자 수 계산
            totalCount = querySnapshot.size();

            // 페이지 계산
            totalPages = (int) Math.ceil((double) totalCount / limit);

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                users.add(convertToUser(document));
            }

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("사용자 목록 가져오기 오류: " + e.getMessage());
        }

        // API 응답 형태로 반환
        // UserListResponse 객체를 생성하여 응답 반환
        UserListResponse response = new UserListResponse();
        response.setSuccess(true);
        response.setUsers(users);
        response.setTotal_count(totalCount);
        response.setCurrent_page(currentPage);
        response.setTotal_pages(totalPages);

        return response;
    }

    private String formatTimestampToISO(Timestamp timestamp) {
        if (timestamp == null) return null;
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(timestamp.toDate());
    }


    // Firestore 문서를 User 객체로 변환
    private User convertToUser(DocumentSnapshot document) {
        User user = new User();
        user.setUser_id(document.getId());
        user.setUser_name(document.getString("user_name"));
        user.setEmail(document.getString("email"));
        user.setVerify((List<String>) document.get("verify"));
        user.setCreated_at(formatTimestampToISO(document.getTimestamp("created_at")));  // 수정됨
        user.setLast_login_at(formatTimestampToISO(document.getTimestamp("last_login_at")));  // 수정됨
        user.setReport_count(document.getLong("report_count") != null ? document.getLong("report_count").intValue() : 0);
        user.setCountry_code(document.getString("country_code"));
        user.setTotal_count(document.get("total_count") != null ? document.getLong("total_count").intValue() : 0);
        user.setCurrent_page(document.get("current_page") != null ? document.getLong("current_page").intValue() : 0);
        user.setTotal_pages(document.get("total_pages") != null ? document.getLong("total_pages").intValue() : 0);
        String status = document.contains("status") ? document.getString("status") : "Active";
        user.setStatus(status);

        return user;
    }


    // Firestore에서 전체 사용자 수를 조회
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

    // Firestore에서 특정 사용자 정보를 가져옴
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

    // Firestore 문서를 User 객체로 변환
    private User convertToUserWithDetails(DocumentSnapshot document) {
        User user = new User();
        user.setUser_id(document.getId());
        user.setUser_name(document.getString("user_name"));
        user.setEmail(document.getString("email"));
        user.setProfile_picture(document.getString("profile_picture"));
        user.setPost_count(document.getLong("post_count") != null ? document.getLong("post_count").intValue() : 0);
        user.setReport_count(document.getLong("report_count") != null ? document.getLong("report_count").intValue() : 0);
        user.setBio(document.getString("bio"));
        user.setFollowers((List<String>) document.get("followers"));
        user.setFollowing((List<String>) document.get("following"));
        user.setCreated_at(formatTimestampToISO(document.getTimestamp("created_at")));  // 수정됨
        user.setLast_login_at(formatTimestampToISO(document.getTimestamp("last_login_at")));  // 수정됨
        String phone_number = (String) document.get("phone_number");
        user.setphone_number(document.getString("phone_number"));
        user.setLocation(document.getString("location"));
        user.setGender(document.getString("gender"));
        user.setBirthdate(document.getString("birthdate"));
        user.setLast_login_at(formatTimestampToISO(document.getTimestamp("last_login_at")));
        String country_code = (String) document.get("country_code");
        user.setCountry_code(document.getString("country_code"));
        user.setNative_language(document.getString("native_language"));
        user.setPreferred_language(document.getString("preferred_language"));
        List<String> interestKeywords = (List<String>) document.get("interest_keywords");
        user.setInterest_keywords((List<String>) document.get("interest_keywords"));
        String status = document.contains("status") ? document.getString("status") : "Active";
        user.setStatus(status);
        // 콘솔에 출력
        System.out.println("Interest Keywords: " + interestKeywords);
        System.out.println("phone_number: " + phone_number);
        return user;
    }

    public String updateUserStatus(String user_id, String status, String reason, Integer durationDays) {
        // 사용자 조회
        System.out.println("Received user_id: " + user_id); // 디버깅용 로그 추가

        // Firestore에서 직접 사용자 조회
        User foundUser = getUserDetailById(user_id);  // Firestore에서 직접 사용자 정보 조회

        if (foundUser == null) {
            System.err.println("사용자를 찾을 수 없습니다: " + user_id);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 상태 변경
        foundUser.setStatus(status);
        foundUser.setReason(reason);
        foundUser.setDuration_days(durationDays);

        // 상태 변경된 사용자 정보 Firestore에 반영
        updateUserInFirestore(foundUser);  // Firestore에 사용자 업데이트

        try {
            updateUserInFirestore(foundUser);  // Firestore에 사용자 업데이트
            return "사용자 상태가 변경되었습니다";
        } catch (Exception e) {
            System.err.println("사용자 상태 변경 실패: " + e.getMessage());
            throw new RuntimeException("사용자 상태 변경 중 오류 발생", e);
        }
    }

    public void updateUserInFirestore(User user) {
        // Firestore에서 해당 사용자 문서의 참조를 얻어옴
        DocumentReference docRef = db.collection("users").document(user.getUser_id());

        // 사용자 정보를 Firestore에 업데이트
        ApiFuture<WriteResult> writeResult = docRef.set(user);

        try {
            // Firestore에 데이터 업데이트가 완료되기를 기다림
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            // 예외 처리: 오류 발생 시 로그 출력
            e.printStackTrace();
        }
    }

    public Map<String, Object> getWithdrawalReasons(String startDate, String endDate) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> byReason = new ArrayList<>();
        List<Map<String, Object>> trend = new ArrayList<>();

        try {
            // 날짜 포맷을 지정합니다.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // startDate와 endDate를 Date로 변환
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // Date 객체를 Timestamp로 변환 (Timestamp.of())
            Timestamp startTimestamp = Timestamp.of(start);
            Timestamp endTimestamp = Timestamp.of(end);

            // Firestore에서 탈퇴 사유 데이터를 필터링하여 조회
            CollectionReference usersRef = db.collection("users");
            Query query = usersRef
                    .whereGreaterThanOrEqualTo("withdrawal_date", startTimestamp)
                    .whereLessThanOrEqualTo("withdrawal_date", endTimestamp);

            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot querySnapshot = future.get();

            // 탈퇴 사유 계산
            Map<String, Integer> reasonCountMap = new HashMap<>();
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String reason = document.getString("withdrawal_reason");
                if (reason != null) {
                    reasonCountMap.put(reason, reasonCountMap.getOrDefault(reason, 0) + 1);
                }
            }

            // 탈퇴 사유 별 통계 생성
            int total = querySnapshot.size();
            for (Map.Entry<String, Integer> entry : reasonCountMap.entrySet()) {
                Map<String, Object> reasonData = new HashMap<>();
                reasonData.put("reason", entry.getKey());
                reasonData.put("count", entry.getValue());
                reasonData.put("percentage", (entry.getValue() * 100.0) / total);
                byReason.add(reasonData);
            }

            // 탈퇴 추세 생성 (날짜 별 탈퇴 횟수)
            // Trend 데이터를 날짜 별로 집계하는 로직을 추가할 수 있습니다.
            // 이 예시에서는 trend 데이터를 비워두고 있습니다. 필요한 경우 추가 로직을 작성하세요.

            response.put("success", true);
            response.put("data", Map.of(
                    "total", total,
                    "by_reason", byReason,
                    "trend", trend
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "탈퇴 사유 통계 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return response;
    }


}
