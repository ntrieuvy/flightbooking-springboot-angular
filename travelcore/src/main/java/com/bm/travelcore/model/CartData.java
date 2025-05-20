package com.bm.travelcore.model;

import com.bm.travelcore.model.abstracts.AbstractOrderModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CartData extends AbstractOrderModel {
    private Long Id;
    private double totalPrice;
    private String paymentIntentId;
    private String currency;
    private User user;
}
