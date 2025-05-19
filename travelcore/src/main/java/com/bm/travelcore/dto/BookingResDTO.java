package com.bm.travelcore.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class BookingResDTO {
    public String airline;
    public String bookingCode;
    public String bookingImage;
    public Integer leg;
    public Integer adt;
    public Integer chd;
    public Integer inf;
    public Integer numberFlight;
    public String flightValue;
    public Integer status;
    public Boolean isCancel;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Double fareAdt;
    public Double fareChd;
    public Double fareInf;
    public Double taxAdt;
    public Double taxChd;
    public Double taxInf;
    public Double feeAdt;
    public Double feeChd;
    public Double feeInf;
    public Double serviceFeeAdt;
    public Double serviceFeeChd;
    public Double serviceFeeInf;
    public Double totalNetPrice;
    public Double totalServiceFee;
    public Double totalDiscount;
    public Double totalCommission;
    public Double totalPrice;
    public String currency;
    public Boolean promo;
    public LocalDateTime expiryDate;
    public String session;
    public Double commissionAdt;
    public Double commissionChd;
    public Double commissionInf;
    public Double commissionTotal;
    public Double systemCommissionAdt;
    public Double systemCommissionChd;
    public Double systemCommissionInf;
    public Double systemCommissionTotal;
}
