package com.bm.travelcore.model;

import com.bm.travelcore.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAYMENT_ACCOUNT")
@EntityListeners(AuditingEntityListener.class)
public class PaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq_gen")
    @SequenceGenerator(name = "payment_seq_gen", sequenceName = "payment_seq", allocationSize = 1)
    private Long id;

    private String paymentAccountId;
    private String paymentIntentId;
    private String intentSecret;
    private String amount;
    private String paymentServiceFees;
    private String totalPriceLessFees;
    private String surchargeFee;
    private String transactionDate;
    private PaymentStatus paymentStatus;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
