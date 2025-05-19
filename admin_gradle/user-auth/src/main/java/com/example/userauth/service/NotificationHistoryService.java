package com.example.userauth.service;

import com.example.userauth.dto.NotificationHistoryResponseDTO;
import com.example.userauth.model.NotificationHistory;
import com.example.userauth.repository.NotificationRepository;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class NotificationHistoryService {
    private final NotificationRepository repository;

    public NotificationHistoryResponseDTO getNotificationHistory(String type, String startDate, String endDate, int page, int limit) throws ExecutionException, InterruptedException {
        Timestamp start = startDate != null ? Timestamp.ofTimeSecondsAndNanos(
                LocalDate.parse(startDate).atStartOfDay(ZoneId.of("Asia/Seoul")).toEpochSecond(), 0) : null;
        Timestamp end = endDate != null ? Timestamp.ofTimeSecondsAndNanos(
                LocalDate.parse(endDate).plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul")).toEpochSecond(), 0) : null;

        int offset = (page - 1) * limit;
        List<NotificationHistory> histories = repository.findByFilters(type, start, end, offset, limit);
        long total = repository.countByFilters(type, start, end);
        int totalPages = (int) Math.ceil((double) total / limit);

        List<NotificationHistoryResponseDTO.NotificationDto> notificationDtos = histories.stream()
                .map(n -> new NotificationHistoryResponseDTO.NotificationDto(
                        n.getId(),
                        n.getTemplate_id(),
                        n.getTemplate_name(),
                        n.getType(),
                        n.getSent_at().toDate().toString(),
                        n.getRecipient_count(),
                        n.getRead_count(),
                        n.getSent_by()
                ))
                .collect(Collectors.toList());

        return new NotificationHistoryResponseDTO(true,
                new NotificationHistoryResponseDTO.DataWrapper(notificationDtos, total, page, totalPages));
    }
}
