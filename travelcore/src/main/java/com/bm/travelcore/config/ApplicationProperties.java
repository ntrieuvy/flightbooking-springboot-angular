package com.bm.travelcore.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {
    
    @Value("${application.mailing.frontend.activation_url}")
    private String activationUrl;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.mailing.not_reply_email}")
    private String notReplyEmail;

    @Value("${application.provider.sms.default}")
    private String defaultProviderSms;

    @Value("${application.provider.sms.twilio.account_sid}")
    private String twilioAccountSid;

    @Value("${application.provider.sms.twilio.auth_token}")
    private String twilioAuthToken;

    @Value("${application.provider.sms.outgoing_number}")
    private String twilioOutgoingNumber;

    @Value("${application.frontend.oauth-redirect-url}")
    private String oauthRedirectUrl;
}
