package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FLIGHT")
@EntityListeners(AuditingEntityListener.class)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_seq_gen")
    @SequenceGenerator(name = "flight_seq_gen", sequenceName = "flight_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String airline;

    private String departure;
    private String arrival;

    @Column(name = "flight_date")
    private LocalDate date;

    @Column(name = "std")
    private LocalDateTime std;

    @Column(name = "sta")
    private LocalDateTime sta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "flight_number")
    private String number;

    private Integer duration;

    @Column(name = "group_class")
    private String groupClass;

    @Column(name = "fare_class")
    private String fareClass;

    private Boolean promo;

    @Column(columnDefinition = "CLOB")
    private String segments;

    @Column(name = "hand_baggage")
    private String handBaggage;

    @Column(name = "allowance_baggage")
    private String allowanceBaggage;

    private String operating;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderFlightMap> orderFlightMaps;
}