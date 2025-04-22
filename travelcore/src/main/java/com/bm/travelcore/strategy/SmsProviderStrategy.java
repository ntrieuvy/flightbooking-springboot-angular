package com.bm.travelcore.strategy;

public interface SmsProviderStrategy {
    public void sendSms(String phoneNumber, String message);
}
