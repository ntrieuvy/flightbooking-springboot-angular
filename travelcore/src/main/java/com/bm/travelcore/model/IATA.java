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
@Table(name = "IATA")
@EntityListeners(AuditingEntityListener.class)
public class IATA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iata_seq_gen")
    @SequenceGenerator(name = "iata_seq_gen", sequenceName = "iata_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;
}