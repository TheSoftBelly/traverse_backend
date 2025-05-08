package com.taptalk.aimap.repository;

import com.taptalk.aimap.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 장소 정보를 관리하는 Repository 인터페이스
 */
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByDestination_DestinationId(Long destinationId);
    List<Place> findByContentType(Place.ContentType contentType);
    List<Place> findByLocationContaining(String location);
    List<Place> findByNameContaining(String name);
} 