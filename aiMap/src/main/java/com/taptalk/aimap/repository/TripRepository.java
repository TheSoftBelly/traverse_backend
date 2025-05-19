package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 여행 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUser_UserId(Long userId);
    List<Trip> findByDestination_DestinationId(Long destinationId);
} 