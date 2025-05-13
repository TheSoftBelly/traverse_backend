package com.example.userauth.controller;

import com.example.userauth.dto.ReportDTO;
import com.example.userauth.service.ReportService;
import com.example.userauth.dto.ReportProcessRequest;
import com.example.userauth.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 사용자 신고 목록 조회 API
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUserReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return getReportsForType("user", page, limit, search, status, sortBy, sortOrder);
    }

    // 게시물 신고 목록 조회 API
    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getPostReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return getReportsForType("post", page, limit, search, status, sortBy, sortOrder);
    }

    // 채팅 신고 목록 조회 API
    @GetMapping("/chats")
    public ResponseEntity<Map<String, Object>> getChatReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return getReportsForType("chat", page, limit, search, status, sortBy, sortOrder);
    }

    // 공통된 처리 로직
    private ResponseEntity<Map<String, Object>> getReportsForType(
            String type, int page, int limit, String search, String status, String sortBy, String sortOrder) {
        try {
            // 서비스에서 신고 리스트를 가져옵니다.
            List<ReportDTO> reports = reportService.getReportsForType(type, page, limit, search, status, sortBy, sortOrder);

            // 총 신고 건수를 가져옵니다.
            int total_Count = reportService.getTotalReportCount();

            // 페이지 계산
            int tota_lPages = (total_Count + limit - 1) / limit;

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("reports", reports);
            response.put("total_count", total_Count);
            response.put("current_page", page);
            response.put("total_pages", (int) Math.ceil((double) tota_lPages / limit));

            // ResponseEntity로 응답을 반환합니다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "보고서 데이터를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("success", false, "error", "서버 오류: " + e.getMessage()));
        }
    }

    // 신고 상세 조회 API
    @GetMapping("/{reportId}")
    public ResponseEntity<Object> getReportDetails(@PathVariable String reportId) {
        try {
            Optional<ReportDTO> report = reportService.getReportDetails(reportId);
            if (report.isPresent()) {
                return ResponseEntity.ok(report.get());  // Optional을 풀어서 반환
            } else {
                return ResponseEntity.status(404).body(Map.of("success", false, "error", "신고를 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", "서버 오류: " + e.getMessage()));
        }
    }

    // 신고 상태 업데이트 API
    @PatchMapping("/{report_id}/status")
    public ResponseEntity<Object> updateReportStatus(
            @PathVariable String reportId,
            @RequestBody Map<String, String> statusUpdateRequest) {
        try {
            String status = statusUpdateRequest.get("status");
            String reason = statusUpdateRequest.get("reason");

            // 상태 업데이트
            String message = reportService.updateReportStatus(reportId, status, reason);

            return ResponseEntity.ok(new ApiResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // 신고 처리 API
    @PatchMapping("/{report_id}/process")
    public ResponseEntity<Map<String, Object>> processReport(
            @PathVariable("report_id") String reportId,
            @RequestBody ReportProcessRequest request) {

        try {
            // 로그 추가
            System.out.println("Processing report ID: " + reportId);
            // 신고 처리 로직을 호출
            String message = reportService.processReport(
                    reportId,
                    request.getStatus(),
                    request.getActionTaken(),
                    request.getComment(),
                    request.isNotifyReporter(),
                    request.isNotifyReported(),
                    request.getSuspensionDuration()
            );

            // 성공적인 처리 응답 반환
            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "신고가 처리되었습니다"
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 예외 처리 시 실패 응답 반환
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "신고 처리 중 오류가 발생했습니다."
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    // 오류 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleExceptions(Exception e) {
        return ResponseEntity
                .status(500)
                .body(Map.of("success", false, "error", "서버 오류: " + e.getMessage()));
    }
}
