package com.bm.travelcore.strategy.stripe.utils;

import com.bm.travelcore.model.PaymentAccount;
import com.bm.travelcore.model.User;
import com.bm.travelcore.strategy.stripe.constants.StripePaymentConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StripePaymentUtils {

    public static String updateSurchargeFeeForAccountPayment(String amount, PaymentAccount paymentAccount) {
        double finalAmount = Double.parseDouble(amount);
        double surchargePercent = 0.0;
        surchargePercent = Double.parseDouble(StripePaymentConstants.TRAVEL_ACCOUNT_PAYMENT_SURCHARGE_PERCENT_VN);
        paymentAccount.setSurchargeFee(String.valueOf(roundDecimal(finalAmount * (surchargePercent / 100))));
        finalAmount = finalAmount + finalAmount * (surchargePercent / 100);
        return String.valueOf(roundDecimal(finalAmount));
    }

    public static double roundDecimal(double value){
        return (Math.round(value*100.0)/100.0);
    }

    public static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
