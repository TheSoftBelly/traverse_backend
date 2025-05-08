package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 여행 정보를 저장하는 엔티티 클래스
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trips")
@EntityListeners(AuditingEntityListener.class)
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanionType companionType;

    private Double budget;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Concept concept;

    @Enumerated(EnumType.STRING)
    private Pace pace;

    @Enumerated(EnumType.STRING)
    private ArrivalTransport arrivalTransport;

    @Enumerated(EnumType.STRING)
    private LocalTransport localTransport;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<DailySchedule> dailySchedules;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL)
    private TravelEssentials travelEssentials;

    private Boolean trafficPass;
    private Boolean rentCar;
    private Boolean usim;
    private Boolean pocketWifi;
    private Boolean passport;
    private Boolean visa;
    private Boolean insurance;

    public enum CompanionType {
        SOLO, COUPLE, FAMILY, FRIENDS, GROUP
    }

    public enum Concept {
        RELAXATION, ADVENTURE, CULTURE, FOOD, SHOPPING, NATURE
    }

    public enum Pace {
        SLOW, MODERATE, FAST
    }

    public enum ArrivalTransport {
        PLANE, TRAIN, BUS, CAR, SHIP
    }

    public enum LocalTransport {
        SUBWAY, BUS, TAXI, WALK, BICYCLE, RENT_CAR
    }
} 