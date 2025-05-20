package com.bm.travelcore.dto;

import com.bm.travelcore.model.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentReqDTO {
    private PaymentType paymentType;
    private String orderId;
    private double amount;
    private String currency;
    private String description;
    private String customerId;
}
