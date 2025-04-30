package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FLIGHT_SEGMENT_MOCK")
@EntityListeners(AuditingEntityListener.class)
public class FlightSegmentMock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLIGHT_MOCK_ID", nullable = false)
    private FlightMock flightMock;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;

    @Column(name = "START_POINT")
    private String startPoint;

    @Column(name = "END_POINT")
    private String endPoint;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "PLANE")
    private String plane;
}
