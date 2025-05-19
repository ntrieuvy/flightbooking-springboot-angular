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

    @Column(name = "system")
    private String system;

    @Column(name = "type")
    private String type;

    @Column(name = "pax_type")
    private String paxType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "confirmed")
    private Boolean confirmed;

    @Column(name = "b_session")
    private String session;

    @Column(name = "airline", nullable = false)
    private String airline;

    @Column(name = "leg")
    private Integer leg;

    @Column(name = "currency")
    private String currency;

    @Column(name = "name")
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "value", nullable = false)
    private String value;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}