package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.TravelEssentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 여행 필수품 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface TravelEssentialsRepository extends JpaRepository<TravelEssentials, Long> {
    TravelEssentials findByTrip_TripId(Long tripId);
} 