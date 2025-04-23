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
@Table(name = "COLLABORATOR")
@EntityListeners(AuditingEntityListener.class)
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "collaborator_seq_gen")
    @SequenceGenerator(name = "collaborator_seq_gen", sequenceName = "collaborator_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "account_balance", nullable = false)
    @Builder.Default
    private Double accountBalance = 0.0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}