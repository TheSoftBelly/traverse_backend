package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 여행지 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByType(String type);
    List<Destination> findByCountryCode(String countryCode);
    List<Destination> findByAreaCode(String areaCode);
} 