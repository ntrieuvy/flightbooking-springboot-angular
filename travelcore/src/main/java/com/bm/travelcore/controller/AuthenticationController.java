package com.bm.travelcore.controller;

import com.bm.travelcore.dto.AuthenticationReqDTO;
import com.bm.travelcore.dto.AuthenticationResDTO;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.dto.SocialAuthReqDTO;
import com.bm.travelcore.model.enums.LoginProvider;
import com.bm.travelcore.service.AuthenticationService;
import com.bm.travelcore.service.SmsService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid RegistrationReqDTO registrationReqDTO
    ) throws MessagingException {
        authenticationService.register(registrationReqDTO);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResDTO> authenticate(
            @RequestBody @Valid AuthenticationReqDTO authenticationReqDTO
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationReqDTO));
    }

    @PostMapping("/social/authenticate")
    public ResponseEntity<Map<String, String>> socialAuthenticate(
            @RequestParam("type") String type
    ) {
        String loginType = type.toLowerCase();
        String url = authenticationService.buildAuthUrl(loginType);
        return ResponseEntity.ok(Collections.singletonMap("redirectUrl", url));
    }

    @PostMapping("/google/callback")
    public ResponseEntity<AuthenticationResDTO> handleGoogleCallback(
            @RequestBody SocialAuthReqDTO request
    ) {
        AuthenticationResDTO response = authenticationService.authenticateWithGoogle(request.getCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<AuthenticationResDTO> comfirm(
            @RequestParam String otp
    ) throws MessagingException {
        return ResponseEntity.ok(authenticationService.activateAccount(otp));
    }

    @GetMapping("/check-user-exists")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam String identifier) {
        boolean exists = authenticationService.isUserExists(identifier, LoginProvider.LOCAL);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/resend-otp")
    public void resendOtp(@RequestParam String identifier) throws MessagingException {
        authenticationService.resendOtp(identifier);
    }

}
