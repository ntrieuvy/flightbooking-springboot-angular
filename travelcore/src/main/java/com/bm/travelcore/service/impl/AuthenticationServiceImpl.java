package com.bm.travelcore.service.impl;

import com.bm.travelcore.constant.AppConstant;
import com.bm.travelcore.dto.AuthenticationReqDTO;
import com.bm.travelcore.dto.AuthenticationResDTO;
import com.bm.travelcore.model.enums.IdentifyType;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.model.Role;
import com.bm.travelcore.model.Token;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.EmailTemplateName;
import com.bm.travelcore.repository.RoleRepository;
import com.bm.travelcore.repository.TokenRepository;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.AuthenticationService;
import com.bm.travelcore.service.JwtService;
import com.bm.travelcore.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    private final MailService mailService;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation_url}")
    private String ACTIVATION_URL;

    @Override
    public void register(RegistrationReqDTO reqDTO) throws MessagingException {

        Role userRole = roleRepository.findByName(String.valueOf(Roles.ROLE_USER))
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER WAS NOT INITIALIZED"));

        IdentifyType userType = reqDTO.getIdentifyType();

        Optional<User> existingUser = userType == IdentifyType.EMAIL
                ? userRepository.findByEmail(reqDTO.getIdentifier())
                : userRepository.findByPhoneNumber(reqDTO.getIdentifier());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this " + userType.name().toLowerCase() + " already exists.");
        }

        User user = User
                .builder()
                .firstname(reqDTO.getFirstName())
                .lastname(reqDTO.getLastName())
                .email((userType == IdentifyType.EMAIL ? reqDTO.getIdentifier() : null))
                .phoneNumber((userType == IdentifyType.PHONE ? reqDTO.getIdentifier() : null))
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .accountLocked(Boolean.FALSE)
                .enabled(Boolean.FALSE)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);

        if (userType == IdentifyType.EMAIL) {
            sendValidationEmail(user);
        } else {
            sendValidationPhone(user);
        }
    }

    @Override
    public AuthenticationResDTO authenticate(AuthenticationReqDTO authenticationReqDTO) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationReqDTO.getIdentifier(),
                        authenticationReqDTO.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResDTO.builder().token(jwtToken).build();
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to same email address.");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        mailService.sendValidateMail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                ACTIVATION_URL,
                newToken,
                AppConstant.SUBJECT_MAIL
        );
    }

    private void sendValidationPhone(User user) {

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(AppConstant.ACTIVATION_CODE_LENGTH);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(AppConstant.TOKEN_EXPIRED_TIME))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        SecureRandom random = new SecureRandom();
        return random.ints(length, 0, 10)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
