package com.bm.travelcore.service.impl;

import com.bm.travelcore.constant.AppConstant;
import com.bm.travelcore.constant.ExceptionMessages;
import com.bm.travelcore.config.RedisKeyConfig;
import com.bm.travelcore.dto.AuthenticationReqDTO;
import com.bm.travelcore.dto.AuthenticationResDTO;
import com.bm.travelcore.model.enums.IdentifyType;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.model.Role;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.EmailTemplateName;
import com.bm.travelcore.repository.RoleRepository;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.AuthenticationService;
import com.bm.travelcore.service.JwtService;
import com.bm.travelcore.service.MailService;
import com.bm.travelcore.service.RedisService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;


    private final MailService mailService;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation_url}")
    private String ACTIVATION_URL;

    @Override
    public void register(RegistrationReqDTO reqDTO) throws MessagingException {

        Role userRole = roleRepository.findByName(String.valueOf(Roles.ROLE_USER))
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.ROLE_USER_WAS_NOT_INITIALIZED));

        IdentifyType userType = reqDTO.getIdentifyType();

        Optional<User> existingUser = userType == IdentifyType.EMAIL
                ? userRepository.findByEmail(reqDTO.getIdentifier())
                : userRepository.findByPhoneNumber(reqDTO.getIdentifier());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (Boolean.FALSE.equals(user.isEnabled())) {
                if (userType == IdentifyType.EMAIL) {
                    sendValidationEmail(user);
                } else {
                    sendValidationPhone(user);
                }
            }

            throw new IllegalArgumentException(String.format(ExceptionMessages.USER_ALREADY_EXISTS, userType.name().toLowerCase()));
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
        String key = RedisKeyConfig.getActivationTokenKey(token);
        String userId = redisService.get(key);

        if (userId == null) {
            throw new RuntimeException(ExceptionMessages.USER_ALREADY_ACTIVE_OR_TOKEN_INVALID);
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, "id", userId)));

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);

        redisService.delete(key);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        if (newToken == null) {
            throw new IllegalArgumentException(ExceptionMessages.ACTIVATION_EMAIL_SENT_WAIT_5_MINUTES);
        } else {
            mailService.sendValidateMail(
                    user.getEmail(),
                    user.getFullName(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    ACTIVATION_URL,
                    newToken,
                    AppConstant.SUBJECT_MAIL
            );
        }
    }

    private void sendValidationPhone(User user) {

    }

    private String generateAndSaveActivationToken(User user) {
        String userId = user.getId().toString();
        String userActivationKey = RedisKeyConfig.getUserActivationKey(userId);

        String existingToken = redisService.get(userActivationKey);

        if (existingToken != null) {
            String tokenKey = RedisKeyConfig.getActivationTokenKey(existingToken);
            long ttl = redisService.getTtl(tokenKey);
            int minResendInterval = RedisKeyConfig.TOKEN_EXPIRED_TIME - RedisKeyConfig.TIME_RESENT_TOKEN;

            if (ttl > minResendInterval) {
                return null;
            }

            redisService.save(tokenKey, userId, RedisKeyConfig.TOKEN_EXPIRED_TIME);
            redisService.save(userActivationKey, existingToken, RedisKeyConfig.TOKEN_EXPIRED_TIME);
            return existingToken;
        } else {
            String generatedToken = generateActivationCode(AppConstant.ACTIVATION_CODE_LENGTH);
            String tokenKey = RedisKeyConfig.getActivationTokenKey(generatedToken);
            redisService.save(tokenKey, userId, RedisKeyConfig.TOKEN_EXPIRED_TIME);
            redisService.save(userActivationKey, generatedToken, RedisKeyConfig.TOKEN_EXPIRED_TIME);
            return generatedToken;
        }
    }

    private String generateActivationCode(int length) {
        SecureRandom random = new SecureRandom();
        return random.ints(length, 0, 10)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
