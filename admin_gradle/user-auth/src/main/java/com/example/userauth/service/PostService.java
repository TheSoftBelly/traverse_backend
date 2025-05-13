package com.example.userauth.service;

import com.example.userauth.dto.PostListResponseDTO;
import com.example.userauth.dto.PostResponseDTO;
import com.example.userauth.dto.PostStatusUpdateRequest;
import com.example.userauth.model.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Firestore db = FirestoreClient.getFirestore();

    /**
     * Firestore에서 게시물 목록을 가져옵니다.
     */
    public PostListResponseDTO getPosts(int page, int limit, String status, String search, String user_id, String sortBy, String sortOrder, Boolean hasReports) throws ExecutionException, InterruptedException {
        List<Post> posts = new ArrayList<>();
        String lastDocId = null;

        // Firestore에서 게시물 데이터 조회
        CollectionReference postsRef = db.collection("Post");
        Query query = postsRef;

        // 페이지네이션: 페이지 > 1일 경우 lastDocId로 쿼리 시작
        if (page > 1 && lastDocId != null) {
            DocumentReference lastDocRef = db.collection("Post").document(lastDocId);
            query = query.startAfter(lastDocRef);
        }

        // 검색 필터 (post_id 또는 post_content에 검색어 포함)
        if (search != null && !search.isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("post_id", search)
                    .whereLessThanOrEqualTo("post_id", search + "\uf8ff");
        }

        // 사용자 ID 필터링
        if (user_id != null && !user_id.isEmpty()) {
            query = query.whereEqualTo("user_id", user_id);
        }

        // 정렬 및 페이지 제한
        query = query.orderBy(sortBy, sortOrder.equals("desc") ? Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                .limit(limit);

        // Firestore 쿼리 실행
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();

        System.out.println("쿼리 결과 문서 수: " + querySnapshot.size());

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            System.out.println("문서 ID: " + document.getId());  // 각 게시물의 문서 ID 출력

            // hasReports 필드 값 계산
            Long reports = (Long) document.get("report_count");
            int reportCount = document.getLong("report_count") != null ? document.getLong("report_count").intValue() : 0;


            // Post 객체로 변환
            Post post = convertToPost(document);
            post.setReport_count((long) reportCount);  // hasReports 필드를 reportCount 값으로 설정

            posts.add(post);  // Firestore 문서를 Post 객체로 변환하여 리스트에 추가
        }

        // 게시물 응답 DTO 생성
        List<PostResponseDTO> postDtos = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        int totalCount = querySnapshot.size();
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        return new PostListResponseDTO(postDtos, totalCount, page, totalPages);
    }



    /**
     * Firestore 문서를 Post 객체로 변환합니다.
     */
    private Post convertToPost(DocumentSnapshot document) {
        Post post = new Post();
        post.setpost_Id(document.getString("post_id"));  // Firestore에서 post_id 필드를 가져옵니다.
        post.setTitle(document.getString("title"));  // 제목 필드 가져오기
        post.setPost_content(document.getString("post_content"));  // 게시물 내용 필드
        post.setStatus(document.getString("status"));  // 게시물 상태
        post.setUser_id(document.getString("user_id"));  // 사용자 ID
        post.setUser_name(document.getString("user_name"));  // 사용자 이름
        post.setCreated_at(document.getTimestamp("created_at"));  // 생성일시
        post.setUpdated_at(document.getTimestamp("updated_at"));  // 업데이트 일시
        post.setHash_tags((List<String>) document.get("hash_tags"));  // 해시태그 목록
        post.setPost_images((List<String>) document.get("post_images"));  // 게시물 이미지 목록
        // 리포트 수 처리 (null 체크)
        Long reportCount = document.getLong("report_count");
        post.setReport_count((long) (reportCount != null ? reportCount.intValue() : 0));  // null 체크 후 값 설정

        // 좋아요 수 처리 (null 체크)
        Long likeCount = document.getLong("like_count");
        post.setLike_count(likeCount != null ? likeCount.intValue() : 0);  // null 체크 후 값 설정

        // 조회수 처리 (null 체크)
        Long viewCount = document.getLong("view_count");
        post.setView_count(viewCount != null ? viewCount.intValue() : 0);  // null 체크 후 값 설정

        return post;
    }



    /**
     * Firestore에서 게시물 상세 정보를 가져옵니다.
     */
    public PostResponseDTO getPostDetail(String postId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Post").document(postId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Post post = convertToPost(document);  // Firestore 문서를 Post 객체로 변환

            // Firestore에서 게시물에 대한 추가 정보 가져오기 (예: 댓글, 리포트)
            List<Post.Comment> comments = getCommentsForPost(postId);
            List<Post.Report> reports = getReportsForPost(postId);

            // Post 객체에 댓글과 리포트를 설정
            post.setComments(comments);
            post.setReports(reports);

            // PostResponseDto에 변환
            return new PostResponseDTO(post);
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    private List<Post.Comment> getCommentsForPost(String postId) throws ExecutionException, InterruptedException {
        CollectionReference commentsRef = db.collection("Post").document(postId).collection("Comments");
        ApiFuture<QuerySnapshot> future = commentsRef.get();
        QuerySnapshot querySnapshot = future.get();

        List<Post.Comment> comments = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            comments.add(convertToComment(document));  // Firestore 문서를 Comment 객체로 변환
        }
        return comments;
    }

    private List<Post.Report> getReportsForPost(String postId) throws ExecutionException, InterruptedException {
        CollectionReference reportsRef = db.collection("Post").document(postId).collection("Reports");
        ApiFuture<QuerySnapshot> future = reportsRef.get();
        QuerySnapshot querySnapshot = future.get();

        List<Post.Report> reports = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            reports.add(convertToReport(document));  // Firestore 문서를 Report 객체로 변환
        }
        return reports;
    }

    private Post.Comment convertToComment(DocumentSnapshot document) {
        Post.Comment comment = new Post.Comment();
        comment.setComment_id(document.getId());
        comment.setUser_id(document.getString("user_id"));
        comment.setUser_name(document.getString("user_name"));
        comment.setContent(document.getString("content"));
        comment.setCreated_at(document.getTimestamp("created_at"));
        comment.setReport_count(document.getLong("report_count").intValue());
        return comment;
    }

    private Post.Report convertToReport(DocumentSnapshot document) {
        Post.Report report = new Post.Report();
        report.setReport_id(document.getId());
        report.setReason(document.getString("reason"));
        report.setStatus(document.getString("status"));
        return report;
    }

    // 게시물 상태 변경
    public void updatePostStatus(String postId, PostStatusUpdateRequest request) throws ExecutionException, InterruptedException {
        DocumentReference postRef = db.collection("Post").document(postId);

        // 게시물 상태를 변경
        ApiFuture<WriteResult> future = postRef.update(
                "status", request.getStatus(),
                "reason", request.getReason()
        );

        // 변경된 후 알림을 보낼지 여부 확인
        if (request.getnotify_user()) {
            // 알림 보내는 로직 (예: 이메일 또는 푸시 알림)
            sendNotificationToUser(postId, request.getStatus());
        }

        // WriteResult를 기다리고 작업 완료 후 return
        future.get();
    }

    // 알림을 사용자에게 보내는 메서드 (기본 예시)
    private void sendNotificationToUser(String postId, String status) {
        // 여기서 실제 알림 보내는 로직을 구현
        // 예: 사용자에게 상태 변경을 알리는 이메일 또는 푸시 알림 전송
    }
    /**
     * 신고 검토 대기 목록을 가져옵니다.
     */
    public Map<String, Object> getPendingReviews(int page, int limit) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> posts = new ArrayList<>();
        String lastDocId = null;

        // page를 사용해 lastDocId를 계산 (예: 페이지별로 마지막 문서 ID를 추적)
        if (page > 1) {
            lastDocId = "someLastDocIdForPage" + page;  // 실제 로직에 맞게 수정 필요
        }

        // Firestore에서 게시물 데이터 조회
        CollectionReference postsRef = db.collection("Post");
        Query query = postsRef.whereEqualTo("hasReports", true)  // 신고가 있는 게시물만 조회
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(limit);

        // Firestore 쿼리 실행
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("post_id", document.getId());
            postData.put("title", document.getString("title"));
            postData.put("user_id", document.getString("user_id"));
            postData.put("user_name", document.getString("user_name"));
            postData.put("created_at", document.getTimestamp("created_at"));
            //postData.put("flag_reason", document.getString("flag_reason"));
            postData.put("severity", document.getLong("severity"));

            posts.add(postData);
        }

        int totalCount = querySnapshot.size();
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        // Response 형식에 맞춰 반환
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("posts", posts);
        responseData.put("total_count", totalCount);
        responseData.put("current_page", page);
        responseData.put("total_pages", totalPages);

        return responseData;
    }

    /**
     * 게시물 통계 정보를 가져옵니다.
     */
    public Map<String, Object> getPostsStatistics(String startDate, String endDate, String interval) throws ExecutionException, InterruptedException, ParseException {

        // 게시물 통계 초기화
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("success", true);

        // 날짜 형식 처리
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 시작 날짜와 종료 날짜가 null인 경우 기본값 설정
        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.minusDays(7).toString();  // 예: 지난 7일
            endDate = now.toString();  // 오늘
        }

        // 날짜 문자열을 Date 객체로 변환
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);


        // 게시물 쿼리 작성 (날짜 범위에 맞는 게시물 찾기)
        CollectionReference postsRef = db.collection("Post");
        Query query = postsRef.whereGreaterThanOrEqualTo("created_at", start)
                .whereLessThanOrEqualTo("created_at", end);

        // 게시물 통계 데이터 처리
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();

        int totalPosts = querySnapshot.size(); // 전체 게시물 수
        int newPosts = 0; // 새 게시물 카운트
        int activePosts = 0; // 활성 게시물 카운트

        List<Map<String, Object>> postTrend = new ArrayList<>(); // 게시물 트렌드
        Map<String, Integer> reportCategoryCount = new HashMap<>(); // 보고 카테고리별 카운트

        // 게시물별 통계 추출
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Date createdAt = document.getTimestamp("created_at").toDate();
            String createdAtDate = new SimpleDateFormat("yyyy-MM-dd").format(createdAt);

            // 각 게시물별 통계 처리
            Map<String, Object> postStat = new HashMap<>();
            postStat.put("date", createdAtDate);
            postStat.put("like_count", document.getLong("like_count"));
            postStat.put("view_count", document.getLong("view_count"));

            // 트렌드에 추가
            postTrend.add(postStat);

            // Report stats 처리
            List<?> reports = (List<?>) document.get("report_count");
            int reportCount = (reports != null) ? reports.size() : 0;

            // 보고 카테고리별 카운트 계산
            if (reportCount > 0) {
                String reportCategory = document.getString("report_category");  // 카테고리별 보고 수
                reportCategoryCount.put(reportCategory, reportCategoryCount.getOrDefault(reportCategory, 0) + reportCount);
            }

            // 활성 게시물과 새 게시물 처리
            if ("active".equals(document.getString("status"))) {
                activePosts++;
            }
            if (createdAt.after(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                newPosts++;
            }
        }

        // 통계 데이터 반환
        statistics.put("data", Map.of(
                "total_posts", totalPosts,
                "new_posts", newPosts,
                "active_posts", activePosts,
                "post_trend", postTrend,
                "report_stats", Map.of(
                        "total", reportCategoryCount.values().stream().mapToInt(Integer::intValue).sum(),
                        "pending", reportCategoryCount.getOrDefault("pending", 0),
                        "resolved", reportCategoryCount.getOrDefault("resolved", 0),
                        "by_category", reportCategoryCount.entrySet().stream()
                                .map(entry -> Map.of("category", entry.getKey(), "count", entry.getValue()))
                                .collect(Collectors.toList())
                )
        ));

        return statistics;
    }



}
