package com.bm.travelcore.controller;

import com.bm.travelcore.dto.AuthenticationReqDTO;
import com.bm.travelcore.dto.AuthenticationResDTO;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/activate-account")
    public void comfirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticationService.activateAccount(token);
    }
}
