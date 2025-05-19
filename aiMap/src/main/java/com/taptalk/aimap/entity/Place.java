package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 장소 정보를 저장하는 엔티티 클래스
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "places")
@EntityListeners(AuditingEntityListener.class)
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String location;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    private Boolean isIndoor;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Integer mapX;
    private Integer mapY;
    private String keyword;
    private String type;
    private Integer areaCode;
    private String address;
    private String image;
    private Integer mapLevel;
    private String tel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destination destination;

    public enum ContentType {
        SIGHTSEEING, CULTURAL, FESTIVAL, COURSE, LEPORTS, 
        ACCOMMODATION, SHOPPING, RESTAURANT
    }
} 