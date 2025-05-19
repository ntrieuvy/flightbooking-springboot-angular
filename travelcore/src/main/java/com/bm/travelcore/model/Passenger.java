package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PASSENGER")
@EntityListeners(AuditingEntityListener.class)
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passenger_seq_gen")
    @SequenceGenerator(name = "passenger_seq_gen", sequenceName = "passenger_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "passenger_index", nullable = false)
    @Builder.Default
    private Integer passenger_index = 0;

    @Column(nullable = false)
    private String type;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    private LocalDate birthday;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Passport passport;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true) // Fixed typo in "passenger"
    private List<Baggage> passengerBaggageList;
}