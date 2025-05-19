package com.bm.travelcore.service;

import com.bm.travelcore.dto.AuthenticationReqDTO;
import com.bm.travelcore.dto.AuthenticationResDTO;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.model.enums.LoginProvider;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface AuthenticationService {
    void register(RegistrationReqDTO registrationReqDTO) throws MessagingException;

    AuthenticationResDTO authenticate(@Valid AuthenticationReqDTO authenticationReqDTO);

    AuthenticationResDTO activateAccount(String otp) throws MessagingException;

    boolean isUserExists(String identifier, LoginProvider provider);

    void resendOtp(String identifier) throws MessagingException;

    String buildAuthUrl(String loginType);

    AuthenticationResDTO authenticateWithGoogle(String code);
}
