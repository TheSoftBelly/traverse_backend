package com.example.userauth.service;

import com.example.userauth.dto.ReportDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
public class ReportService {

    private static final Firestore db = FirestoreClient.getFirestore();

    private final Firestore firestore;

    @Autowired
    public ReportService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Optional<ReportDTO> getReportDetails(String reportId) throws InterruptedException, ExecutionException {
        // Firestore에서 신고 ID에 해당하는 문서 가져오기
        DocumentSnapshot documentSnapshot;
        try {
            documentSnapshot = firestore.collection("reports").document(reportId).get().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ExecutionException("Error retrieving report details", e);
        }

        // 문서가 존재하는 경우, 해당 데이터를 DTO로 변환
        if (documentSnapshot.exists()) {
            ReportDTO dto = new ReportDTO();
            dto.setReport_id(documentSnapshot.getId());
            dto.setReported_user_id(documentSnapshot.getString("reported_user_id"));
            dto.setReported_user_id(documentSnapshot.getString("reporter_user_id"));
            dto.setReported_user_name(documentSnapshot.getString("reported_user_name"));
            dto.setReason(documentSnapshot.getString("reason"));
            dto.setDescription(documentSnapshot.getString("description"));
            dto.setStatus(documentSnapshot.getString("status"));
            dto.setSeverity(documentSnapshot.getLong("severity"));

            // Timestamp를 LocalDateTime으로 변환
            if (documentSnapshot.contains("created_at")) {
                // Firebase Timestamp를 java.util.Date로 변환
                java.util.Date createdAtDate = documentSnapshot.getDate("created_at");
                if (createdAtDate != null) {
                    Instant instant = createdAtDate.toInstant();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    dto.setCreated_at(localDateTime);
                }
            }

            return Optional.of(dto);
        } else {
            // 해당 ID의 신고가 존재하지 않는 경우
            return Optional.empty();
        }
    }

    public String updateReportStatus(String reportId, String status, String reason) {
        // Firestore에서 신고 ID에 해당하는 문서를 업데이트
        DocumentReference reportRef = firestore.collection("reports").document(reportId);
        try {
            // 문서 가져오기
            DocumentSnapshot reportSnapshot = reportRef.get().get();
            if (reportSnapshot.exists()) {
                // 상태 업데이트
                reportRef.update("status", status, "reason", reason).get();
                return "Report status updated successfully.";
            } else {
                throw new IllegalArgumentException("Report not found with id: " + reportId);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException("Error updating report status: " + e.getMessage());
        }
    }

    public int getTotalReportCount() {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("reports").get();
            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.size();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("신고 수 조회 오류: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 신고 유형별로 필터링된 신고 리스트를 반환
     */
    public List<ReportDTO> getReportsForType(String type, int page, int limit, String search, String status, String sortBy, String sortOrder) throws InterruptedException, ExecutionException {
        Query query;

        switch (type) {
            case "user":
                query = firestore.collection("reports").whereEqualTo("report_type", "user");
                break;
            case "post":
                query = firestore.collection("reports").whereEqualTo("report_type", "post");
                break;
            case "chat":
                query = firestore.collection("reports").whereEqualTo("report_type", "chat");
                break;
            default:
                query = firestore.collection("reports"); // 기본값: 모든 신고
                break;
        }

        // 상태 필터링 (status가 "all"이 아닌 경우)
        if (!"all".equals(status)) {
            query = query.whereEqualTo("status", status);
        }

        // 검색 필터링
        if (!search.isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("post_title", search)
                    .whereLessThanOrEqualTo("post_title", search + "\uf8ff");
        }

        // 페이지네이션 처리
        query = query.offset((page - 1) * limit).limit(limit);

        // 쿼리 실행
        QuerySnapshot querySnapshot;
        try {
            querySnapshot = query.get().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ExecutionException("Error executing Firestore query", e);
        }

        // 데이터 변환
        return querySnapshot.getDocuments().stream().map(doc -> {
            ReportDTO dto = new ReportDTO();
            dto.setReport_id(doc.getId());
            dto.setSeverity(doc.getLong("severity"));
            dto.setStatus(doc.getString("status"));

            // Timestamp를 LocalDateTime으로 변환
            if (doc.contains("created_at")) {
                // Firebase Timestamp를 java.util.Date로 변환
                java.util.Date createdAtDate = doc.getDate("created_at");
                if (createdAtDate != null) {
                    Instant instant = createdAtDate.toInstant();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    dto.setCreated_at(localDateTime);
                }
            }

            // 각 type에 따라 다르게 필드 반환
            switch (type) {
                case "user":
                    dto.setReported_user_id(doc.getString("reported_user_id"));
                    dto.setReported_user_name(doc.getString("reported_user_name"));
                    dto.setReason(doc.getString("reason"));
                    dto.setDescription(doc.getString("description"));
                    dto.setReporter_user_id(doc.getString("reporter_user_id")); // user 타입에만 추가
                    break;
                case "post":
                    dto.setReporter_user_id(doc.getString("reporter_user_id"));
                    dto.setReason(doc.getString("reason"));
                    dto.setDescription(doc.getString("description"));
                    dto.setPost_id(doc.getString("post_id")); // post 타입에만 추가
                    break;
                case "chat":
                    dto.setReporter_user_id(doc.getString("reporter_user_id"));
                    dto.setReason(doc.getString("reason"));
                    dto.setDescription(doc.getString("description"));
                    dto.setChat_id(doc.getString("chat_id")); // chat 타입에만 추가
                    dto.setChat_room_id(doc.getString("chat_room_id")); // chat 타입에만 추가
                    break;
                default:
                    // 기본 필드
                    dto.setReason(doc.getString("reason"));
                    dto.setDescription(doc.getString("description"));
                    break;
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public String processReport(String reportId, String status, String actionTaken, String comment,
                                boolean notifyReporter, boolean notifyReported, Integer suspensionDuration) {
        // Firestore에서 신고 ID에 해당하는 문서 가져오기
        DocumentReference reportRef = firestore.collection("reports").document(reportId);
        String message = "";  // 결과 메시지를 저장할 변수

        try {
            DocumentSnapshot reportSnapshot = reportRef.get().get();

            if (!reportSnapshot.exists()) {
                throw new IllegalArgumentException("ID에 해당하는 신고를 찾을 수 없습니다: " + reportId);
            }

            // 상태 업데이트 및 처리할 액션
            reportRef.update("status", status, "comment", comment).get();

            // 처리된 조치에 따라 상태를 갱신
            switch (actionTaken.toLowerCase()) {
                case "approve":
                    // 신고 승인 처리
                    reportRef.update("status", "approved").get();
                    if (notifyReporter) {
                        // 신고자에게 알림 보내는 로직 추가 (예시)
                        // sendNotificationToReporter(reportId);
                    }
                    if (notifyReported) {
                        // 신고된 사용자에게 알림 보내는 로직 추가 (예시)
                        // sendNotificationToReported(reportId);
                    }
                    message = "신고가 승인되었습니다.";
                    break;

                case "reject":
                    // 신고 거부 처리
                    reportRef.update("status", "rejected").get();
                    message = "신고가 거부되었습니다.";
                    break;

                case "escalate":
                    // 신고 승격 처리 (예: 관리자에게 전달)
                    reportRef.update("status", "escalated").get();
                    message = "신고가 관리자에게 전달되었습니다.";
                    break;

                case "suspend":
                    // 사용자 정지 처리 (정지 기간을 기반으로 추가 작업)
                    if (suspensionDuration != null) {
                        // 사용자 정지 기간을 처리하는 로직 추가
                        // 예: 유저의 정지 상태 업데이트
                        // suspendUser(reportSnapshot.getString("reported_user_id"), suspensionDuration);
                        message = suspensionDuration + "일 동안 사용자가 정지되었습니다.";
                    } else {
                        message = "정지 기간이 필요합니다.";
                    }
                    break;

                default:
                    message = "잘못된 액션이 지정되었습니다. 'approve', 'reject', 'escalate', 'suspend' 중 하나를 사용하세요.";
                    break;
            }
        } catch (InterruptedException | ExecutionException e) {
            // Firestore 작업에서 발생한 예외 처리
            message = "신고 처리 중 오류가 발생했습니다: " + e.getMessage();
        } catch (Exception e) {
            // 일반적인 예외 처리
            message = "예상치 못한 오류가 발생했습니다: " + e.getMessage();
        }

        return message;  // 최종적으로 처리된 메시지 반환


    }

}
