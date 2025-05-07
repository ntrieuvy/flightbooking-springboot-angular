package com.bm.travelcore.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

    // JWT
    @Value("${application.mailing.frontend.activation_url}")
    private String activationUrl;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Mail
    @Value("${application.mailing.not_reply_email}")
    private String notReplyEmail;
    @Value("${application.mailing.contact}")
    private String contactEmail;

    // SMS
    @Value("${application.provider.sms.default}")
    private String defaultProviderSms;

    // twilio
    @Value("${application.provider.sms.twilio.account_sid}")
    private String twilioAccountSid;
    @Value("${application.provider.sms.twilio.auth_token}")
    private String twilioAuthToken;
    @Value("${application.provider.sms.outgoing_number}")
    private String twilioOutgoingNumber;

    @Value("${application.frontend.oauth-redirect-url}")
    private String oauthRedirectUrl;

    @Value("${application.frontend.url}")
    private String frontendUrl;

    @Value("${application.provider.flight-api.default}")
    private String providerFlightApiDefault;

    // aviationstack
    @Value("${application.provider.flight-api.aviationstack.endpoint}")
    private String aviationstackEndpoint;
    @Value("${application.provider.flight-api.aviationstack.access-key}")
    private String ativitionstackAccessKey;

    // Datacom
    @Value( "${application.provider.flight-api.datacom.endpoint}")
    private String dcEndpoint;
    @Value( "${application.provider.flight-api.datacom.private-key}")
    private String dcPrivateKey;
    @Value( "${application.provider.flight-api.datacom.api-account}")
    private String dcApiAccount;
    @Value( "${application.provider.flight-api.datacom.api-password}")
    private String dcApiPassword;
    @Value( "${application.provider.flight-api.datacom.currency}")
    private String dcCurrency;
    @Value( "${application.provider.flight-api.datacom.language}")
    private String dcLanguage;
    @Value( "${application.provider.flight-api.datacom.ip-address}")
    private String dcIpAddress;
}
