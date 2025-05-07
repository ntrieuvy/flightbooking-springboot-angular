package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AIRLINE")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airline_seq_gen")
    @SequenceGenerator(name = "airline_seq_gen", sequenceName = "airline_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "logo")
    private String logo;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AirlineAirportGroupConfig> airlineAirportGroupConfigs;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commission> commissions;
}