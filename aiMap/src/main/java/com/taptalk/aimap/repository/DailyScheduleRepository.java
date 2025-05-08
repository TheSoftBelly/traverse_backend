package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 일정 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface DailyScheduleRepository extends JpaRepository<DailySchedule, Long> {
    List<DailySchedule> findByTrip_TripId(Long tripId);
    List<DailySchedule> findByVisitDate(java.time.LocalDate visitDate);
    List<DailySchedule> findByTrip_TripIdAndVisitDate(Long tripId, java.time.LocalDate visitDate);
} 