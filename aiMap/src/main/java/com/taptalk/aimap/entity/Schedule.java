package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "daily_schedule_id")
    private DailySchedule dailySchedule;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Column(nullable = false)
    private LocalTime startDay;

    @Column(nullable = false)
    private LocalTime endDay;

    @Column(nullable = false)
    private Integer sequenceNumber;

    private LocalDateTime createdAt;
} 