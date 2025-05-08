package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 여행지 정보를 저장하는 엔티티 클래스
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long destinationId;

    private String name;
    private String type; // "domestic" 또는 "international"
    private String countryCode;
    private String cityId;
    private String areaCode;
    private String contentTypeId;
} 