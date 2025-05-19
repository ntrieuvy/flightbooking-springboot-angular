package com.bm.travelcore.model;

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
@Table(name = "ORDER_DETAIL")
@EntityListeners(AuditingEntityListener.class)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_seq_gen")
    @SequenceGenerator(name = "order_detail_seq_gen", sequenceName = "order_detail_seq", allocationSize = 1)
    private Long id;

    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "flight_value", nullable = false)
    private String flightValue;

    @Column(nullable = false)
    @Builder.Default
    private Integer numberFlight = 1;

    @Column(nullable = false)
    @Builder.Default
    private Integer status = 1;

    @Column(name = "is_cancel", nullable = false)
    @Builder.Default
    private Boolean isCancel = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "number_adt", nullable = false)
    @Builder.Default
    private Integer numberAdt = 0;

    @Column(name = "number_chd", nullable = false)
    @Builder.Default
    private Integer numberChd = 0;

    @Column(name = "number_inf", nullable = false)
    @Builder.Default
    private Integer numberInf = 0;

    @Column(name = "fare_adt", nullable = false)
    @Builder.Default
    private Double fareAdt = 0.0;

    @Column(name = "fare_chd", nullable = false)
    @Builder.Default
    private Double fareChd = 0.0;

    @Column(name = "fare_inf", nullable = false)
    @Builder.Default
    private Double fareInf = 0.0;

    @Column(name = "tax_adt", nullable = false)
    @Builder.Default
    private Double taxAdt = 0.0;

    @Column(name = "tax_chd", nullable = false)
    @Builder.Default
    private Double taxChd = 0.0;

    @Column(name = "tax_inf", nullable = false)
    @Builder.Default
    private Double taxInf = 0.0;

    @Column(name = "fee_adt", nullable = false)
    @Builder.Default
    private Double feeAdt = 0.0;

    @Column(name = "fee_chd", nullable = false)
    @Builder.Default
    private Double feeChd = 0.0;

    @Column(name = "fee_inf", nullable = false)
    @Builder.Default
    private Double feeInf = 0.0;

    @Column(name = "service_fee_adt", nullable = false)
    @Builder.Default
    private Double serviceFeeAdt = 0.0;

    @Column(name = "service_fee_chd", nullable = false)
    @Builder.Default
    private Double serviceFeeChd = 0.0;

    @Column(name = "service_fee_inf", nullable = false)
    @Builder.Default
    private Double serviceFeeInf = 0.0;

    @Column(name = "total_net_price", nullable = false)
    @Builder.Default
    private Double totalNetPrice = 0.0;

    @Column(name = "total_service_fee", nullable = false)
    @Builder.Default
    private Double totalServiceFee = 0.0;

    @Column(name = "total_discount", nullable = false)
    @Builder.Default
    private Double totalDiscount = 0.0;

    @Column(name = "total_commission", nullable = false)
    @Builder.Default
    private Double totalCommission = 0.0;

    @Column(name = "total_price", nullable = false)
    @Builder.Default
    private Double totalPrice = 0.0;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(nullable = false)
    @Builder.Default
    private Boolean promo = false;

    @Column(nullable = false)
    private String airline;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "f_session")
    private String session;

    @Lob
    @Column(name = "booking_image")
    private String bookingImage;

    @Column(name = "commission_adt", nullable = false)
    @Builder.Default
    private Double commissionAdt = 0.0;

    @Column(name = "commission_chd", nullable = false)
    @Builder.Default
    private Double commissionChd = 0.0;

    @Column(name = "commission_inf", nullable = false)
    @Builder.Default
    private Double commissionInf = 0.0;

    @Column(name = "commission_total", nullable = false)
    @Builder.Default
    private Double commissionTotal = 0.0;

    @Column(name = "system_commission_adt", nullable = false)
    @Builder.Default
    private Double systemCommissionAdt = 0.0;

    @Column(name = "system_commission_chd", nullable = false)
    @Builder.Default
    private Double systemCommissionChd = 0.0;

    @Column(name = "system_commission_inf", nullable = false)
    @Builder.Default
    private Double systemCommissionInf = 0.0;

    @Column(name = "system_commission_total", nullable = false)
    @Builder.Default
    private Double systemCommissionTotal = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer leg = 0;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderFlightMap> orderFlightMaps;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    public void addFlight(OrderFlightMap orderFlightMap) {
        if (this.orderFlightMaps == null) {
            this.orderFlightMaps = new ArrayList<>();
        }
        this.orderFlightMaps.add(orderFlightMap);
        orderFlightMap.setOrderDetail(this);
    }
}