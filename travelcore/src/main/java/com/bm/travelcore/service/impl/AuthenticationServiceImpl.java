package com.bm.travelcore.service.impl;

import com.bm.travelcore.constant.AppConstant;
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
import com.bm.travelcore.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final MailService mailService;

    @Value("${application.mailing.frontend.activation_url}")
    private String activationUrl;

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

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);

        mailService.sendValidateMail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
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
        String characters = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            activationCode.append(characters.charAt(randomIndex));
        }

        return activationCode.toString();
    }
}
