package com.bm.travelcore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResDTO {
    private boolean success;
    private String transactionId;
    private String paymentUrl;
    private String errorMessage;
    private Double amount;
    private String currency;
}
