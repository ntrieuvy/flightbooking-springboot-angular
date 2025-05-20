package com.bm.travelcore.strategy;

import com.bm.travelcore.dto.PaymentReqDTO;
import com.bm.travelcore.dto.PaymentResDTO;
import com.bm.travelcore.model.CartData;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.model.PaymentAccount;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.abstracts.AbstractOrderModel;
import com.bm.travelcore.model.enums.PaymentType;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;

import java.util.List;

public interface PaymentGatewayStrategy {
    void chargeForOrder(Order orderModel) throws StripeException;
    void updatePaymentIntentForOrder(Order orderModel) throws StripeException;
    String updatePaymentIntentForCart(CartData cartData) throws StripeException;
    boolean isIntentUnCapture(AbstractOrderModel abstractOrderModel) throws StripeException;
    String createPaymentIntent(CartData cartData) throws StripeException;
    void cancelPaymentIntent(AbstractOrderModel abstractOrderModel) throws StripeException;
    void renewPaymentIntent(CartData cartData);
    PaymentAccount createPaymentIntentForAccount(User user, String amount) throws StripeException;
    String updatePaymentIntentForAccount(User user, String amount, String paymentIntentId, PaymentAccount paymentAccount) throws StripeException;
    PaymentAccount chargeForPaymentAccount(PaymentAccount paymentAccount) throws StripeException;
    boolean isExistedStripeCustomer(String customerId) throws StripeException;
    SetupIntent createSetupIntent(User user) throws StripeException;
    List<PaymentMethod> collectPaymentMethod(String customerId) throws StripeException;
    void removePaymentMethod(String paymentMethodId) throws StripeException;
    String getDefaultPaymentMethod(String customerId) throws StripeException;
    void setDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException;
    boolean supports(PaymentType type);
}
