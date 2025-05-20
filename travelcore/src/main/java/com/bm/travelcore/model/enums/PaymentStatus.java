package com.bm.travelcore.model.enums;

public enum PaymentStatus {
    NOTPAID,
    PAID,
    FAILED,
    REFUNDED,
    PARTIALLY_PAID;

    public String getCode() {
        return name();
    }
}
