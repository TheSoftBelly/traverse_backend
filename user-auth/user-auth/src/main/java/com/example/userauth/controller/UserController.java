package com.example.userauth.controller;

import com.example.userauth.dto.ApiResponse;
import com.example.userauth.dto.StatusUpdateRequest;
import com.example.userauth.model.User;
import com.example.userauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 모든 사용자 목록을 조회합니다.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        List<User> users = userService.getAllUsers(page, limit, search, status, sortBy, sortOrder);
        int totalCount = userService.getTotalUserCount();
        int totalPages = (totalCount + limit - 1) / limit;

        // 응답 데이터 가공
        List<Map<String, Object>> userData = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("user_id", user.getUserId());
            userMap.put("user_name", user.getUserName());
            userMap.put("email", user.getEmail());
            userMap.put("verify", user.getVerify());
            userMap.put("created_at", user.getCreatedAt());
            userMap.put("last_login_at", user.getLastLoginAt());
            userMap.put("report_count", user.getReportCount());
            userMap.put("country_code", user.getCountryCode());

            return userMap;
        }).collect(Collectors.toList());


        // 응답 구조
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", Map.of(
                "users", userData,
                "total_count", totalCount,
                "current_page", page,
                "total_pages", totalPages
        ));


        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 상세 정보를 조회합니다.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserDetails(@PathVariable String userId) {
        User user = userService.getUserDetailById(userId);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * 오류 처리를 위한 예외 핸들러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleExceptions(Exception e) {
        return ResponseEntity
                .status(500)
                .body(Map.of("success", false, "error", "서버 오류: " + e.getMessage()));
    }

    // 사용자 상태 변경
    @PatchMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable String userId,
            @RequestBody @Valid StatusUpdateRequest statusUpdateRequest) {
        // Request Body 출력
        System.out.println("Request Body:");
        System.out.println("Status: " + statusUpdateRequest.getStatus());
        System.out.println("Reason: " + statusUpdateRequest.getReason());
        System.out.println("Duration Days: " + statusUpdateRequest.getDurationDays());

        try {
            String message = userService.updateUserStatus(
                    userId,
                    statusUpdateRequest.getStatus(),
                    statusUpdateRequest.getReason(),
                    statusUpdateRequest.getDurationDays()
            );

            // Response 출력
            ApiResponse response = new ApiResponse(true, message);
            System.out.println("Response:");
            System.out.println("Success: " + response.isSuccess());
            System.out.println("Message: " + response.getMessage());


            return ResponseEntity.ok(new ApiResponse(true, message));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
