package com.bm.travelcore.model.abstracts;

import com.bm.travelcore.model.User;

public abstract class AbstractOrderModel {
    public abstract String getId();

    public abstract String getPaymentIntentId();

    public abstract String getCurrency();

    public abstract Double getTotalPrice();

    public abstract User getUser();

    public abstract void setPaymentIntentId(String paymentIntentId);
}
