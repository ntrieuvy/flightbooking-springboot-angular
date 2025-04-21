package com.bm.travelcore.service;

import com.bm.travelcore.dto.RegistrationReqDTO;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationReqDTO registrationReqDTO) throws MessagingException;
}
