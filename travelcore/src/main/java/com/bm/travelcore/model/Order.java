package com.bm.travelcore.model;

import com.bm.travelcore.model.abstracts.AbstractOrderModel;
import com.bm.travelcore.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_ORDER")
@EntityListeners(AuditingEntityListener.class)
public class Order extends AbstractOrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", allocationSize = 1)
    private Long id;

    @Column(name = "provider_booking_id")
    private String providerBookingId;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_service_fees")
    private String paymentServiceFees;

    @Column(name = "total_price_less_fees")
    private String totalPriceLessFees;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "currency")
    private String currency;

    @Lob
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_from", nullable = false)
    @Builder.Default
    private String createdFrom = "internal";

    @Column(name = "is_send_mail", nullable = false)
    @Builder.Default
    private Boolean isSendMail = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers;

    public void addOrderDetail(OrderDetail detail) {
        if (this.orderDetails == null) {
            this.orderDetails = new ArrayList<>();
        }
        this.orderDetails.add(detail);
        detail.setOrder(this);
    }

    public void addPassenger(Passenger passenger) {
        if (this.passengers == null) {
            this.passengers = new ArrayList<>();
        }
        this.passengers.add(passenger);
        passenger.setOrder(this);
    }
}