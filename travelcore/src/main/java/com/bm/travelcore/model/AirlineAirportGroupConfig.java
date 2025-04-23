package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"AIRLINE_AIRPORT_GROUP_CONFIG\"")
@EntityListeners(AuditingEntityListener.class)
public class AirlineAirportGroupConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airline_airport_group_config_seq_gen")
    @SequenceGenerator(name = "airline_airport_group_config_seq_gen", sequenceName = "airline_airport_group_config_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_group_id", nullable = false)
    private AirportGroup airportGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "limit_fee_adt", nullable = false)
    @Builder.Default
    private Double limitFeeAdt = 0.0;

    @Column(name = "limit_fee_chd", nullable = false)
    @Builder.Default
    private Double limitFeeChd = 0.0;

    @Column(name = "limit_fee_inf", nullable = false)
    @Builder.Default
    private Double limitFeeInf = 0.0;
}