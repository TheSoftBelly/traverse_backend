package com.example.userauth.service;

import com.example.userauth.dto.response.ReportDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Firestore db = FirestoreClient.getFirestore();

    private final Firestore firestore;

    @Autowired
    public ReportService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Map<String, Object> getReportDetailsAsMap(String reportId) throws InterruptedException, ExecutionException {
        Optional<ReportDTO> optionalReport = getReportDetails(reportId);
        if (optionalReport.isEmpty()) {
            return Collections.emptyMap();
        }
        ReportDTO report = optionalReport.get();

        Map<String, Object> response = new HashMap<>();
        response.put("report_id", report.getReport_id());

        // type 명확히 설정
        String type = report.getType();
        if (type == null || type.isEmpty()) {
            type = report.getReport_type(); // DTO에서 report_type으로 fallback
        }
        response.put("type", type);


        // 신고 유형별 target_id 및 target_details 설정
        switch (type) {
            case "user":
                response.put("target_id", report.getReported_user_id());
                response.put("target_details", report.getReported_user_name());
                break;
            case "post":
                response.put("target_id", report.getPost_id());
                response.put("post_id", report.getPost_id());
                response.put("post_content", report.getPost_content());

                // [수정됨] Post 컬렉션에서 post_id로 게시물 정보 조회
                DocumentSnapshot postDoc = firestore.collection("Post").document(report.getPost_id()).get().get();
                if (postDoc.exists()) {
                    response.put("target_details", postDoc.getString("user_id")); // [수정됨] 작성자 ID를 target_details로

                    List<Object> postImagesRaw = (List<Object>) postDoc.get("post_images");
                    List<String> postImages = postImagesRaw != null
                            ? postImagesRaw.stream().map(Object::toString).collect(Collectors.toList())
                            : Collections.emptyList();
                    response.put("post_images", postImages);


                    List<Object> hashTagsRaw = (List<Object>) postDoc.get("hash_tags");
                    List<String> hashTags = hashTagsRaw != null
                            ? hashTagsRaw.stream().map(Object::toString).collect(Collectors.toList())
                            : Collections.emptyList();
                    response.put("hash_tags", hashTags);

                } else {
                    response.put("target_details", null); // [수정됨]
                    response.put("post_images", Collections.emptyList()); // [수정됨]
                    response.put("hash_tags", Collections.emptyList()); // [수정됨]
                }
                break;
            case "chat":
                response.put("target_id", report.getChat_id());
                response.put("chat_id", report.getChat_id());
                response.put("chat_room_id", report.getChat_room_id());
                // reported_user_id 가져오기
                String reportedUserId = report.getReported_user_id();

                // Firestore에서 해당 사용자 정보 조회
                if (reportedUserId != null && !reportedUserId.isEmpty()) {
                    DocumentSnapshot userDoc = firestore.collection("users").document(reportedUserId).get().get();
                    if (userDoc.exists()) {
                        response.put("target_details", userDoc.getString("user_id"));  // 사용자 user_id를 target_details에 설정
                    } else {
                        response.put("target_details", null);  // 사용자 문서 없음
                    }
                } else {
                    response.put("target_details", null);  // reported_user_id가 null인 경우
                }
                break;
            case "snap":
                // snap 신고일 경우 snap_id를 target_id로 세팅
                response.put("target_id", report.getSnap_id());
                // 프론트에서 snap_id 필요하면 별도 필드도 추가
                response.put("snap_id", report.getSnap_id());
                response.put("snap_content", report.getSnap_content());

                DocumentSnapshot snapDoc = firestore.collection("SnapPost").document(report.getSnap_id()).get().get();
                if (snapDoc.exists()) {
                    response.put("target_details", snapDoc.getString("user_id")); // [수정됨] 작성자 ID를 target_details로

                    List<Object> snapImagesRaw = (List<Object>) snapDoc.get("snap_images");
                    List<String> snapImages = snapImagesRaw != null
                            ? snapImagesRaw.stream().map(Object::toString).collect(Collectors.toList())
                            : Collections.emptyList();
                    response.put("snap_images", snapImages);


                    List<Object> hashTagsRaw = (List<Object>) snapDoc.get("hash_tags");
                    List<String> hashTags = hashTagsRaw != null
                            ? hashTagsRaw.stream().map(Object::toString).collect(Collectors.toList())
                            : Collections.emptyList();
                    response.put("hash_tags", hashTags);
                }
                break;
            case "comment":
                response.put("target_id", report.getComment_id());
                response.put("target_details", report.getComment_content());
                response.put("comment_id", report.getComment_id());
                response.put("comment_content", report.getComment_content());
                response.put("parent_comment_id", report.getParent_comment_id());
                break;
            default:
                response.put("target_id", null);
                response.put("target_details", null);
                break;
        }

        response.put("reporter_user_id", report.getReporter_user_id());
        response.put("reporter_details", report.getReporter_user_id());  // 필요시 Map 형태로 확장 가능

        response.put("reason", report.getReason());
        response.put("description", report.getDescription());
        response.put("evidence", report.getEvidence());
        response.put("status", report.getStatus());
        response.put("created_at", report.getCreated_at());
        response.put("updated_at", report.getUpdated_at());
        response.put("processed_by", report.getProcessed_by());
        response.put("processed_at", report.getProcessed_at());
        response.put("comment", report.getComment());
        response.put("action_taken", report.getAction_taken());
        response.put("report_id", report.getReport_id());
        response.put("report_type", report.getReport_type());
        response.put("reported_user_id", report.getReported_user_id());
        response.put("reported_user_name", report.getReported_user_name());

        return response;
    }

    public Optional<ReportDTO> getReportDetails(String reportId) throws InterruptedException, ExecutionException {
        DocumentSnapshot doc = firestore.collection("reports").document(reportId).get().get();

        if (!doc.exists()) {
            return Optional.empty();
        }

        ReportDTO report = new ReportDTO();
        report.setReport_id(doc.getString("report_id"));
        report.setReport_type(doc.getString("report_type"));  // 중요: report_type 필드 꼭 세팅
        report.setType(doc.getString("report_type")); // 필요시 type 필드도 세팅
        report.setReporter_user_id(doc.getString("reporter_user_id"));
        report.setReason(doc.getString("reason"));
        report.setDescription(doc.getString("description"));
        report.setStatus(doc.getString("status"));

        // created_at, updated_at, processed_at 등의 Timestamp -> LocalDateTime 변환 처리 필요
        Timestamp createdAtTimestamp = doc.getTimestamp("created_at");
        if (createdAtTimestamp != null) {
            report.setCreated_at(createdAtTimestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        Timestamp updatedAtTimestamp = doc.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            report.setUpdated_at(updatedAtTimestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        Timestamp processedAtTimestamp = doc.getTimestamp("processed_at");
        if (processedAtTimestamp != null) {
            report.setProcessed_at(processedAtTimestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        report.setProcessed_by(doc.getString("processed_by"));
        report.setComment(doc.getString("comment"));
        report.setAction_taken(doc.getString("action_taken"));
        report.setSeverity(doc.getLong("severity"));
        report.setEvidence((List<String>) doc.get("evidence"));  // 형변환 주의

        // 신고 유형별 추가 필드 세팅
        String reportType = report.getReport_type();
        if ("snap".equals(reportType)) {
            report.setSnap_id(doc.getString("snap_id"));
            report.setSnap_title(doc.getString("snap_title"));
            report.setSnap_content(doc.getString("snap_content"));
        } else if ("user".equals(reportType)) {
            report.setReported_user_id(doc.getString("reported_user_id"));
            report.setReported_user_name(doc.getString("reported_user_name"));
        } else if ("post".equals(reportType)) {
            report.setPost_id(doc.getString("post_id"));
            report.setTitle(doc.getString("title"));
            report.setPost_content(doc.getString("post_content"));
        } else if ("chat".equals(reportType)) {
            report.setChat_id(doc.getString("chat_id"));
            report.setChat_room_id(doc.getString("chat_room_id"));
            report.setReported_user_id(doc.getString("reported_user_id"));
        } else if ("comment".equals(reportType)) {
            report.setComment_id(doc.getString("comment_id"));
            report.setComment_content(doc.getString("comment_content"));
            report.setParent_comment_id(doc.getString("parent_comment_id"));
            // 댓글이 post에 속한 경우와 snap에 속한 경우 분리해서 설정
            if (doc.contains("post_id")) {
                report.setPost_id(doc.getString("post_id"));
            } else if (doc.contains("snap_id")) {
                report.setSnap_id(doc.getString("snap_id"));
            }
        }

        return Optional.of(report);
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


    public String processReport(String reportId, String status, String actionTaken, String comment,
                                boolean notifyReporter, boolean notifyReported, Integer suspensionDuration) {
        DocumentReference reportRef = firestore.collection("reports").document(reportId);
        String message = "";

        try {
            DocumentSnapshot reportSnapshot = reportRef.get().get();

            if (!reportSnapshot.exists()) {
                throw new IllegalArgumentException("ID에 해당하는 신고를 찾을 수 없습니다: " + reportId);
            }

            // 신고 문서 상태 업데이트 및 코멘트
            reportRef.update("status", status, "action_taken", actionTaken, "comment", comment).get();

            // 신고 유형
            String reportType = reportSnapshot.getString("report_type");

            // ==== 사용자 신고 처리 ====
            if ("user".equals(reportType)) {
                String reportedUserId = reportSnapshot.getString("reported_user_id");
                if (reportedUserId != null) {
                    updateUserActionByUid(reportedUserId, status, actionTaken, suspensionDuration);
                }
            }

            // ==== 게시물 신고 처리 ====
            if ("post".equals(reportType)) {
                String postId = reportSnapshot.getString("post_id");
                if (postId != null) {
                    DocumentSnapshot postSnapshot = firestore.collection("Post").document(postId).get().get();
                    if (postSnapshot.exists()) {
                        String userId = postSnapshot.getString("user_id");
                        System.out.println("user_id : " + userId);
                        if (userId != null) {
                            updateUserAction(userId, null, actionTaken, suspensionDuration);
                        } else {
                            System.out.println("⚠️ user_id가 null입니다. Post ID: " + postId);
                        }
                    } else {
                        System.out.println("⚠️ Post 문서를 찾을 수 없습니다. Post ID: " + postId);
                    }
                } else {
                    System.out.println("⚠️ post_id가 report 문서에 없습니다. Report ID: " + reportId);
                }
            }


            // ==== 스냅 신고 처리 ====
            if ("snap".equals(reportType)) {
                String postId = reportSnapshot.getString("snap_id");
                if (postId != null) {
                    DocumentSnapshot snapSnapshot = firestore.collection("SnapPost").document(postId).get().get();
                    if (snapSnapshot.exists()) {
                        String userId = snapSnapshot.getString("user_id");
                        if (userId != null) {
                            updateUserAction(userId, null, actionTaken, suspensionDuration);
                        }
                    }
                }
            }

            // ==== 채팅 신고 처리 ====
            if ("chat".equals(reportType)) {
                String reportedUserId = reportSnapshot.getString("reported_user_id");
                if (reportedUserId != null) {
                    updateUserActionByUid(reportedUserId, null, actionTaken, suspensionDuration);
                }
            }

            // ==== 댓글 신고 처리 ====
            if ("comment".equals(reportType)) {
                String commentId = reportSnapshot.getString("comment_id");
                String postId = reportSnapshot.getString("post_id");
                String snapId = reportSnapshot.getString("snap_id");
                String parentCommentId = reportSnapshot.getString("parent_comment_id");

                String commentCollection = null;

                if (commentId != null) {
                    if (snapId != null) {
                        commentCollection = "Snap_Comments";
                    } else if (postId != null) {
                        commentCollection = "Post_Comments";
                    }
                }

                // 해당 컬렉션에서 댓글 문서 조회 후 user_id로 제재 처리
                if (commentCollection != null && commentId != null) {
                    DocumentSnapshot commentSnapshot = firestore
                            .collection(commentCollection)
                            .document(commentId)
                            .get()
                            .get();

                    if (commentSnapshot.exists()) {
                        String userId = commentSnapshot.getString("user_id");

                        if (userId != null && !userId.isEmpty()) {
                            updateUserAction(userId, null, actionTaken, suspensionDuration);
                        }
                    }
                }
            }

            // ==== 메시지 설정 ====
            switch (actionTaken.toLowerCase()) {
                case "approve":
                    message = "신고가 승인되었습니다.";
                    break;
                case "reject":
                    message = "신고가 거부되었습니다.";
                    break;
                case "escalate":
                    message = "신고가 관리자에게 전달되었습니다.";
                    break;
                case "warn":
                    message = "경고 조치가 완료되었습니다.";
                    break;
                case "suspend":
                    message = (suspensionDuration != null)
                            ? suspensionDuration + "일 동안 사용자가 정지되었습니다."
                            : "정지 기간이 필요합니다.";
                    break;
                case "delete_content":
                    message = "콘텐츠가 삭제되었습니다.";
                    break;
                case "ban":
                    message = "사용자가 영구 정지되었습니다.";
                    break;
                default:
                    message = "처리된 액션: " + actionTaken;
            }

        } catch (InterruptedException | ExecutionException e) {
            message = "신고 처리 중 오류가 발생했습니다: " + e.getMessage();
        } catch (Exception e) {
            message = "예상치 못한 오류가 발생했습니다: " + e.getMessage();
        }

        return message;
    }



    private void updateUserAction(String userId, String status, String actionTaken, Integer suspensionDuration) throws Exception {
        // 1. user_id 필드로 users 컬렉션에서 문서 검색
        ApiFuture<QuerySnapshot> query = firestore.collection("users")
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 user_id(" + userId + ")를 가진 사용자를 찾을 수 없습니다.");
        }

        // 2. 문서 ID (uid) 가져오기
        DocumentSnapshot userDoc = documents.get(0);
        String uid = userDoc.getId();

        // 3. 업데이트할 데이터 준비
        Map<String, Object> updateMap = new HashMap<>();
        if (status != null) {
            updateMap.put("status", status);
        }
        updateMap.put("action_taken", actionTaken);
        if ("suspend".equalsIgnoreCase(actionTaken)) {
            if (suspensionDuration != null) {
                updateMap.put("suspension_duration", suspensionDuration);
                updateMap.put("suspension_start", Timestamp.now()); // 🔥 현재 시간 저장
            } else {
                throw new IllegalArgumentException("정지(suspend) 조치에는 suspension_duration이 필요합니다.");
            }
        }

        // 4. 실제 업데이트
        firestore.collection("users").document(uid).update(updateMap).get();
    }

    private void updateUserActionByUid(String uid, String status, String actionTaken, Integer suspensionDuration) throws Exception {
        // 1. 업데이트할 데이터 준비
        Map<String, Object> updateMap = new HashMap<>();
        if (status != null) {
            updateMap.put("status", status);
        }
        updateMap.put("action_taken", actionTaken);
        if ("suspend".equalsIgnoreCase(actionTaken)) {
            if (suspensionDuration != null) {
                updateMap.put("suspension_duration", suspensionDuration);
                updateMap.put("suspension_start", Timestamp.now());
            } else {
                throw new IllegalArgumentException("정지(suspend) 조치에는 suspension_duration이 필요합니다.");
            }
        }

        // 2. 실제 업데이트
        firestore.collection("users").document(uid).update(updateMap).get();
    }


    public Map<String, Object> getReportsSummary(String type, int page, int limit, String search, String status,
                                                 String sortBy, String sortOrder) throws InterruptedException, ExecutionException {
        CollectionReference reportsRef = firestore.collection("reports");
        Query query = reportsRef.whereEqualTo("report_type", type);

        if (!"all".equals(status)) {
            query = query.whereEqualTo("status", status);
        }

        // 정렬 조건 (snap일 때만 사용하도록 하려면 if ("snap".equals(type)) 조건 추가)
        if (sortBy != null && sortOrder != null && !sortBy.isBlank() && !sortOrder.isBlank()) {
            query = "desc".equalsIgnoreCase(sortOrder) ? query.orderBy(sortBy, Query.Direction.DESCENDING)
                    : query.orderBy(sortBy, Query.Direction.ASCENDING);
        }

        List<QueryDocumentSnapshot> allDocs = query.get().get().getDocuments();

        // ✅ 간결하고 명확한 search 필터 (특정 필드에 대해서만 적용)
        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();

            allDocs = allDocs.stream().filter(doc -> {
                String reportedUserName = Optional.ofNullable(doc.getString("reported_user_name")).orElse("").toLowerCase();
                String reporterUserId = Optional.ofNullable(doc.getString("reporter_user_id")).orElse("").toLowerCase();
                String reason = Optional.ofNullable(doc.getString("reason")).orElse("").toLowerCase();
                String title = Optional.ofNullable(doc.getString("title")).orElse("").toLowerCase();
                String snapTitle = Optional.ofNullable(doc.getString("snap_title")).orElse("").toLowerCase();

                return reportedUserName.contains(searchLower) ||
                        reporterUserId.contains(searchLower) ||
                        reason.contains(searchLower) ||
                        title.contains(searchLower) ||
                        snapTitle.contains(searchLower);
            }).collect(Collectors.toList());
        }



        int totalCount = allDocs.size();
        int totalPages = (int) Math.ceil((double) totalCount / limit);
        int start = Math.min((page - 1) * limit, totalCount);
        int end = Math.min(start + limit, totalCount);
        List<QueryDocumentSnapshot> pagedDocs = allDocs.subList(start, end);

        List<ReportDTO> reports = pagedDocs.stream().map(doc -> {
            ReportDTO dto = new ReportDTO();
            dto.setReport_id(doc.getId());
            dto.setSeverity(doc.getLong("severity"));
            dto.setStatus(doc.getString("status"));
            dto.setReason(doc.getString("reason"));
            dto.setDescription(doc.getString("description"));
            dto.setReporter_user_id(doc.getString("reporter_user_id"));
            dto.setReported_user_id(doc.getString("reported_user_id"));
            dto.setReported_user_name(doc.getString("reported_user_name"));
            Timestamp createdTimestamp = doc.getTimestamp("created_at");
            if (createdTimestamp != null) {
                dto.setCreated_at(LocalDateTime.ofInstant(createdTimestamp.toDate().toInstant(), ZoneId.systemDefault()));
            }

            // 🔍 title 조회 처리
            String title = null;
            if ("post".equals(type)) {
                String postId = doc.getString("post_id");
                if (postId != null) {
                    DocumentSnapshot postDoc = null;
                    try {
                        postDoc = firestore.collection("Post").document(postId).get().get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    title = postDoc.contains("title") ? postDoc.getString("title") : null;
                }
            } else if ("snap".equals(type)) {
                String snapId = doc.getString("snap_id");
                if (snapId != null) {
                    DocumentSnapshot snapDoc = null;
                    try {
                        snapDoc = firestore.collection("SnapPost").document(snapId).get().get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    title = snapDoc.contains("title") ? snapDoc.getString("title") : null;
                }
            } else if ("chat".equals(type)) {
                dto.setChat_id(doc.getString("chat_id"));
                dto.setChat_room_id(doc.getString("chat_room_id"));
                dto.setChat_content(doc.getString("chat_content"));
                title = doc.getString("chat_content"); // 프론트에서 title처럼 리스트에 표시할 수 있도록
            }

            dto.setTitle(title);


            return dto;
        }).collect(Collectors.toList());

        Map<String, Long> statusCounts = allDocs.stream()
                .map(d -> d.getString("status"))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String topReason = allDocs.stream()
                .map(d -> d.getString("reason"))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Map<String, Object> response = new HashMap<>();
        response.put("total_count", totalCount);
        response.put("current_page", page);
        response.put("total_pages", totalPages);
        response.put("status_counts", statusCounts);
        response.put("top_reason", topReason);
        response.put("reports", reports);

        response.put("pending_count", statusCounts.getOrDefault("pending", 0L));
        response.put("resolved_count", statusCounts.getOrDefault("resolved", 0L));
        response.put("other_count", statusCounts.getOrDefault("rejected", 0L));
        return response;
    }

    public Map<String, Object> getCommentReports(
            int page, int limit,
            String status, String search,
            String sortBy, String sortOrder) throws ExecutionException, InterruptedException {

        // 기본값 설정
        if (page < 1) page = 1;
        if (limit < 1) limit = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "created_at";
        if (sortOrder == null || sortOrder.isEmpty()) sortOrder = "desc";

        // Firestore 쿼리 생성
        CollectionReference reportsRef = firestore.collection("reports");
        Query query = reportsRef.whereEqualTo("report_type", "comment");

        if (!"all".equalsIgnoreCase(status)) {
            query = query.whereEqualTo("status", status);
        }

        // 정렬
        Query.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        query = query.orderBy(sortBy, direction);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> allDocs = future.get().getDocuments();

// 1. 검색 필터링
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.trim().toLowerCase();

            allDocs = allDocs.stream().filter(doc -> {
                String reportId = Optional.ofNullable(doc.getId()).orElse("");
                String commentId = Optional.ofNullable(doc.getString("comment_id")).orElse("");
                String commentContent = Optional.ofNullable(doc.getString("comment_content")).orElse("");
                String comment = Optional.ofNullable(doc.getString("comment")).orElse("");
                String reporterUserId = Optional.ofNullable(doc.getString("reporter_user_id")).orElse("");
                String reportedUserId = Optional.ofNullable(doc.getString("reported_user_id")).orElse("");
                String parentCommentId = Optional.ofNullable(doc.getString("parent_comment_id")).orElse("");
                String reason = Optional.ofNullable(doc.getString("reason")).orElse("");
                String description = Optional.ofNullable(doc.getString("description")).orElse("");
                String STATUS = Optional.ofNullable(doc.getString("status")).orElse("");
                String postId = Optional.ofNullable(doc.getString("post_id")).orElse("");
                String createdAt = "";

                Timestamp createdTimestamp = doc.getTimestamp("created_at");
                if (createdTimestamp != null) {
                    createdAt = createdTimestamp.toDate().toString();
                }

                return reportId.toLowerCase().contains(searchLower)
                        || commentId.toLowerCase().contains(searchLower)
                        || commentContent.toLowerCase().contains(searchLower)
                        || comment.toLowerCase().contains(searchLower)
                        || reporterUserId.toLowerCase().contains(searchLower)
                        || reportedUserId.toLowerCase().contains(searchLower)
                        || parentCommentId.toLowerCase().contains(searchLower)
                        || reason.toLowerCase().contains(searchLower)
                        || description.toLowerCase().contains(searchLower)
                        || STATUS.toLowerCase().contains(searchLower)
                        || postId.toLowerCase().contains(searchLower)
                        || createdAt.toLowerCase().contains(searchLower);
            }).collect(Collectors.toList());
        }

// 2. 검색된 결과에서 직접 페이징 적용
        int offset = (page - 1) * limit;
        int toIndex = Math.min(offset + limit, allDocs.size());
        List<QueryDocumentSnapshot> pagedDocs = allDocs.subList(
                Math.min(offset, allDocs.size()), toIndex
        );

// 3. DTO 변환
        List<ReportDTO> reports = new ArrayList<>();
        for (DocumentSnapshot doc : pagedDocs) {
            ReportDTO dto = new ReportDTO();
            dto.setReport_id(doc.getString("report_id"));
            dto.setComment(doc.getString("comment_content"));
            dto.setReporter_user_id(doc.getString("reporter_user_id"));
            dto.setStatus(doc.getString("status"));
            dto.setReason(doc.getString("reason"));
            dto.setDescription(doc.getString("description"));
            dto.setSeverity(doc.contains("severity") ? Long.valueOf(doc.getLong("severity")) : null);
            dto.setReport_type(doc.getString("report_type"));
            dto.setPost_id(doc.getString("post_id"));
            dto.setSnap_id(doc.getString("snap_id"));
            dto.setType("comment");
            dto.setComment_id(doc.getString("comment_id"));
            dto.setParent_comment_id(doc.getString("parent_comment_id"));

            Timestamp createdTimestamp = doc.getTimestamp("created_at");
            if (createdTimestamp != null) {
                dto.setCreated_at(LocalDateTime.ofInstant(createdTimestamp.toDate().toInstant(), ZoneId.systemDefault()));
            }
            dto.setReported_user_id(null); // 필요 시 추가 처리

            reports.add(dto);
        }

// 4. 전체 카운트는 필터된 기준으로 계산
        int totalCount = allDocs.size();
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        Map<String, Object> result = new HashMap<>();
        result.put("reports", reports);
        result.put("total_count", totalCount);
        result.put("current_page", page);
        result.put("total_pages", totalPages);
        return result;
    }
}
