package com.bm.travelcore.service;

public interface SmsService {
    public void sendSms(String phoneNumber, String message);
    public void sendSms(String provider, String phoneNumber, String message);
}
