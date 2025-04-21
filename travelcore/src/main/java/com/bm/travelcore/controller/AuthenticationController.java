package com.bm.travelcore.controller;

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
}
