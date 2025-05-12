package com.example.userauth.service;

import com.example.userauth.model.Admin;
import com.example.userauth.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserStatisticsService {

    private static final Firestore db = FirestoreClient.getFirestore();

    // 사용자 통계 대시보드 데이터를 가져옴
    public Map<String, Object> getUserStatistics(String startDate, String endDate, String interval) {
        Map<String, Object> response = new HashMap<>();
        try {
            // SecurityContextHolder에서 인증된 사용자 정보를 가져옵니다.
            String email = getAuthenticatedUserEmail();
            if (email == null) {
                response.put("success", false);
                response.put("error", "사용자가 인증되지 않았습니다.");
                return response;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            Timestamp startTimestamp = Timestamp.of(start);
            Timestamp endTimestamp = Timestamp.of(end);

            // 사용자 통계
            int totalUsers = getTotalUserCount();
            int newUsers = getNewUserCount(startTimestamp, endTimestamp);
            int activeUsers = getActiveUserCount(startTimestamp, endTimestamp);

            // 사용자 트렌드
            List<Map<String, Object>> userTrend = getUserTrend(startTimestamp, endTimestamp, interval);

            // 리포트 통계
            Map<String, Object> reportStats = getReportStatistics(startTimestamp, endTimestamp);

            response.put("success", true);
            response.put("total_users", totalUsers);
            response.put("new_users", newUsers);
            response.put("active_users", activeUsers);
            response.put("user_trend", userTrend);
            response.put("report_stats", reportStats);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "사용자 통계 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return response;
    }

    // 현재 인증된 사용자 이메일을 가져오는 메서드
    private String getAuthenticatedUserEmail() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof Admin) {
                return ((Admin) principal).getEmail();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 전체 사용자 수를 가져옴
    private int getTotalUserCount() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 새로운 사용자 수를 가져옴
    private int getNewUserCount(Timestamp start, Timestamp end) throws ExecutionException, InterruptedException {
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("created_at", start)
                .whereLessThanOrEqualTo("created_at", end);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 활성 사용자 수를 가져옴
    private int getActiveUserCount(Timestamp start, Timestamp end) throws ExecutionException, InterruptedException {
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("last_login_at", start)
                .whereLessThanOrEqualTo("last_login_at", end);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 사용자 트렌드를 일별, 주간, 월간으로 가져옴
    private List<Map<String, Object>> getUserTrend(Timestamp start, Timestamp end, String interval) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> trend = new ArrayList<>();
        // 이 부분은 주어진 interval에 맞춰서 데이터를 집계해야 합니다.
        // 예시로 일별 데이터를 처리하는 방법을 보여줍니다.

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start.toDate());

        while (calendar.getTime().before(end.toDate())) {
            Date currentDate = calendar.getTime();
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);

            int count = getUserCountByDate(currentDate);
            int newUsers = getNewUserCountByDate(currentDate);
            int activeUsers = getActiveUserCountByDate(currentDate);

            Map<String, Object> trendData = new HashMap<>();
            trendData.put("date", formattedDate);
            trendData.put("count", count);
            trendData.put("new_users", newUsers);
            trendData.put("active_users", activeUsers);

            trend.add(trendData);

            // 날짜를 1일씩 증가시킴
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return trend;
    }

    // 특정 날짜의 사용자 수를 가져옴
    private int getUserCountByDate(Date date) throws ExecutionException, InterruptedException {
        // 특정 날짜에 가입한 사용자 수를 계산하는 로직
        Timestamp timestamp = Timestamp.of(date);
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("created_at", timestamp)
                .whereLessThanOrEqualTo("created_at", timestamp);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 특정 날짜의 새로운 사용자 수를 가져옴
    private int getNewUserCountByDate(Date date) throws ExecutionException, InterruptedException {
        // 특정 날짜에 새로 가입한 사용자 수를 계산하는 로직
        Timestamp timestamp = Timestamp.of(date);
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("created_at", timestamp)
                .whereLessThanOrEqualTo("created_at", timestamp);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 특정 날짜의 활성 사용자 수
    private int getActiveUserCountByDate(Date date) throws ExecutionException, InterruptedException {
        // 특정 날짜에 로그인한 사용자 수를 계산하는 로직
        Timestamp timestamp = Timestamp.of(date);
        Query query = db.collection("users")
                .whereGreaterThanOrEqualTo("last_login_at", timestamp)
                .whereLessThanOrEqualTo("last_login_at", timestamp);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot.size();
    }

    // 리포트 통계
    private Map<String, Object> getReportStatistics(Timestamp start, Timestamp end) throws ExecutionException, InterruptedException {
        Map<String, Object> reportStats = new HashMap<>();
        // 리포트 데이터를 집계하는 로직
        Query query = db.collection("reports")
                .whereGreaterThanOrEqualTo("created_at", start)
                .whereLessThanOrEqualTo("created_at", end);

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();

        int total = querySnapshot.size();
        int pending = 0;
        int resolved = 0;
        Map<String, Integer> categoryCountMap = new HashMap<>();

        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            String status = doc.getString("status");
            if ("pending".equals(status)) {
                pending++;
            } else if ("resolved".equals(status)) {
                resolved++;
            }

            String category = doc.getString("category");
            if (category != null) {
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
            }
        }

        reportStats.put("total", total);
        reportStats.put("pending", pending);
        reportStats.put("resolved", resolved);

        // 카테고리별 리포트 수
        List<Map<String, Object>> byCategory = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryCountMap.entrySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", entry.getKey());
            categoryData.put("count", entry.getValue());
            byCategory.add(categoryData);
        }

        reportStats.put("by_category", byCategory);

        return reportStats;
    }
}
