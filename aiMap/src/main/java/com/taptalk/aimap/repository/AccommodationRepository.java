package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 숙소 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByTrip_TripId(Long tripId);
    List<Accommodation> findByDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
} 