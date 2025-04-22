package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.factory.SmsProviderFactory;
import com.bm.travelcore.service.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final SmsProviderFactory smsProviderFactory;
    private final ApplicationProperties properties;

    public SmsServiceImpl(SmsProviderFactory smsProviderFactory, ApplicationProperties properties) {
        this.smsProviderFactory = smsProviderFactory;
        this.properties = properties;
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        smsProviderFactory.getStrategy(properties.getDefaultProviderSms()).sendSms(phoneNumber, message);
    }

    @Override
    public void sendSms(String provider, String phoneNumber, String message) {
        smsProviderFactory.getStrategy(provider).sendSms(phoneNumber, message);
    }
}
