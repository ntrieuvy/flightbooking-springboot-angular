package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Table(name = "COMMISSION", indexes = {
        @Index(name = "idx_commission_agency", columnList = "agency_id"),
        @Index(name = "idx_commission_airline", columnList = "airline_id"),
        @Index(name = "idx_commission_airport_group", columnList = "airport_group_id"),
        @Index(name = "idx_commission_composite", columnList = "agency_id,airline_id,airport_group_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commission_seq_gen")
    @SequenceGenerator(name = "commission_seq_gen", sequenceName = "commission_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_group_id", nullable = false)
    private AirportGroup airportGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = true)
    private Agency agency;

    @Column(name = "service_fee_adt", nullable = false)
    @Builder.Default
    private Double serviceFeeAdt = 0.0;

    @Column(name = "service_fee_chd", nullable = false)
    @Builder.Default
    private Double serviceFeeChd = 0.0;

    @Column(name = "service_fee_inf", nullable = false)
    @Builder.Default
    private Double serviceFeeInf = 0.0;

    @Column(name = "self_service_fee_adt", nullable = false)
    @Builder.Default
    private Double selfServiceFeeAdt = 0.0;

    @Column(name = "self_service_fee_chd", nullable = false)
    @Builder.Default
    private Double selfServiceFeeChd = 0.0;

    @Column(name = "self_service_fee_inf", nullable = false)
    @Builder.Default
    private Double selfServiceFeeInf = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_updated_role", nullable = false)
    private Long lastUpdatedRole;
}