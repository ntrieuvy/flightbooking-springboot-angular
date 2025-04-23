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
@Table(name = "BAGGAGE")
@EntityListeners(AuditingEntityListener.class)
public class Baggage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baggage_seq_gen")
    @SequenceGenerator(name = "baggage_seq_gen", sequenceName = "baggage_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @Column(name = "airline", nullable = false)
    private String airline;

    @Column(name = "leg")
    private Integer leg;

    @Column(name = "route", nullable = false)
    private String route;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "currency")
    private String currency;

    @Column(name = "name")
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "price_root")
    @Builder.Default
    private Double priceRoot = 0.00;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}