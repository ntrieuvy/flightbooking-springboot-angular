package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResDTO {
    private Long id;
    private String bookingCode;
    private String flightValue;
    private Integer numberFlight;
    private Integer status;
    private Boolean isCancel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer numberAdt;
    private Integer numberChd;
    private Integer numberInf;
    private Double fareAdt;
    private Double fareChd;
    private Double fareInf;
    private Double taxAdt;
    private Double taxChd;
    private Double taxInf;
    private Double feeAdt;
    private Double feeChd;
    private Double feeInf;
    private Double serviceFeeAdt;
    private Double serviceFeeChd;
    private Double serviceFeeInf;
    private Double totalNetPrice;
    private Double totalServiceFee;
    private Double totalDiscount;
    private Double totalCommission;
    private Double totalPrice;
    private String currency;
    private Boolean promo;
    private String airline;
    private LocalDateTime expiryDate;
    private String session;
    private String bookingImage;
    private Double commissionAdt;
    private Double commissionChd;
    private Double commissionInf;
    private Double commissionTotal;
    private Double systemCommissionAdt;
    private Double systemCommissionChd;
    private Double systemCommissionInf;
    private Double systemCommissionTotal;
    private Integer leg;
    private List<FlightResDTO> flights;
    private List<TicketResDTO> tickets;
}