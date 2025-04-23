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
@Table(name = "AIRPORT_GROUP")
@EntityListeners(AuditingEntityListener.class)
public class AirportGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_group_seq_gen")
    @SequenceGenerator(name = "airport_group_seq_gen", sequenceName = "airport_group_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "airportGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Airport> airports;

    @OneToMany(mappedBy = "airportGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AirlineAirportGroupConfig> airlineAirportGroupConfigs;

    @OneToMany(mappedBy = "airportGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commission> commissions;


}