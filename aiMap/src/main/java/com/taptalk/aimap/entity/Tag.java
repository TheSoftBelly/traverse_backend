package com.taptalk.aimap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tag")
public class Tag {
    @Id
    @Column(length = 255)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
} 