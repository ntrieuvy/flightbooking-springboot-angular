package com.bm.travelcore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"ORDER_FLIGHT_MAP\"")
@EntityListeners(AuditingEntityListener.class)
public class OrderFlightMap {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_flight_map_seq_gen")
    @SequenceGenerator(name = "order_flight_map_seq_gen", sequenceName = "order_flight_map_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}