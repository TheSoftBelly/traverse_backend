package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 여행 필수품 정보를 저장하는 엔티티 클래스
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TravelEssentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long essentialsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private Boolean hasPassport;
    private Boolean hasVisa;
    private Boolean hasTravelInsurance;
    private Boolean hasUsim;
    private Boolean hasPocketWifi;
} 