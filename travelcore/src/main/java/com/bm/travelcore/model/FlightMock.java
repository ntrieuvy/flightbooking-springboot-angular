package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FLIGHT_MOCK")
@EntityListeners(AuditingEntityListener.class)
public class FlightMock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "START_POINT")
    private String startPoint;

    @Column(name = "END_POINT")
    private String endPoint;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "START_DT")
    private String startDt;

    @Column(name = "END_DT")
    private String endDt;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "SEAT_REMAIN")
    private Integer seatRemain;

    @Column(name = "GROUP_CLASS")
    private String groupClass;

    @Column(name = "FARE_CLASS")
    private String fareClass;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BAGGAGE")
    private String baggage;

    @Column(name = "HAND_BAGGAGE")
    private String handBaggage;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "flightMock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightSegmentMock> segments;
}
