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
@Table(name = "PASSPORT")
@EntityListeners(AuditingEntityListener.class)
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passport_seq_gen")
    @SequenceGenerator(name = "passport_seq_gen", sequenceName = "passport_seq", allocationSize = 1)
    private Long id;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_code")
    private String documentCode;

    @Column(name = "document_expiry")
    private String documentExpiry;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "issueCountry")
    private String issueCountry;
}
