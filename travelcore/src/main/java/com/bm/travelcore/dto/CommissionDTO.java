package com.bm.travelcore.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommissionDTO {
    Long id;
    String airportGroup;
    String airline;
    Long agencyId;
    Double serviceFeeAdt = 0.0;
    Double serviceFeeChd = 0.0;
    Double serviceFeeInf = 0.0;
    Double selfServiceFeeAdt = 0.0;
    Double selfServiceFeeChd = 0.0;
    Double selfServiceFeeInf = 0.0;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
