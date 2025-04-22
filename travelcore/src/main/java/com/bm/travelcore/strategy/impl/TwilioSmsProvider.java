package com.bm.travelcore.strategy.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.strategy.SmsProviderStrategy;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service("twilio")
@AllArgsConstructor
public class TwilioSmsProvider implements SmsProviderStrategy {

    private final ApplicationProperties properties;

    @PostConstruct
    public void init() {
        Twilio.init(properties.getTwilioAccountSid(), properties.getTwilioAuthToken());
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        try {
            Message response = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(properties.getTwilioOutgoingNumber()),
                    message
            ).create();

            System.out.println("Twilio SMS sent successfully. SID: " + response.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS via Twilio: " + e.getMessage());
        }
    }
}
