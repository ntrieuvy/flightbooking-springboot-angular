package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.dto.*;
import com.bm.travelcore.model.enums.LoginProvider;
import com.bm.travelcore.utils.constant.AppConstant;
import com.bm.travelcore.utils.constant.ExceptionMessages;
import com.bm.travelcore.config.RedisKeyConfig;
import com.bm.travelcore.model.enums.IdentifyType;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.model.Role;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.EmailTemplateName;
import com.bm.travelcore.repository.RoleRepository;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final ApplicationProperties properties;

    private final EmailService EMailService;
    private final JwtService jwtService;
    private final SmsService smsService;

    private final UserDetailsService userDetailsService;
    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;
    private final UserService userService;

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
                return;
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
                .loginProvider(LoginProvider.LOCAL)
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
        String username = authenticationReqDTO.getIdentifier();
        if (username.contains("@")) {
            username = username + ":" + LoginProvider.LOCAL.name();
        }

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        authenticationReqDTO.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFullName());
        claims.put("firstName", user.getFirstname());
        claims.put("lastName", user.getLastname());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("authProvider", user.getLoginProvider().name());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResDTO.builder().token(jwtToken).build();
    }

    @Override
    @Transactional
    public AuthenticationResDTO activateAccount(String otp) throws MessagingException {
        String key = RedisKeyConfig.getActivationOtpKey(otp);
        String userId = redisService.get(key);

        if (userId == null) {
            throw new RuntimeException(ExceptionMessages.USER_ALREADY_ACTIVE_OR_OTP_INVALID);
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, "id", userId)));

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);

        var claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("fullName", user.getFullName());
        claims.put("firstName", user.getFirstname());
        claims.put("lastName", user.getLastname());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("authProvider", user.getLoginProvider().name());
        var jwtToken = jwtService.generateToken(claims, user);

        redisService.delete(key);

        return AuthenticationResDTO.builder().token(jwtToken).build();
    }

    @Override
    public boolean isUserExists(String identifier, LoginProvider provider) {
        try {
            userService.loadUserByUsername(identifier, provider);
            return true;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    @Override
    public void resendOtp(String identifier) throws MessagingException {
        if (identifier.contains("@")) {
            User user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL, identifier)));

            sendValidationEmail(user);
        } else {
            User user = userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_PHONE, identifier)));

            sendValidationPhone(user);
        }
    }

    @Override
    public String buildAuthUrl(String provider) {
        if ("google".equals(provider)) {
            return buildGoogleAuthUrl();
        }
        throw new IllegalArgumentException("Unsupported provider: " + provider);
    }

    @Override
    public AuthenticationResDTO authenticateWithGoogle(String code) {
        try {
            GoogleTokenResDTO tokenResponse = exchangeCodeForToken(code);

            GoogleUserInfo userInfo = getUserInfo(tokenResponse.getAccessToken());

            User user = processOAuth2User(userInfo);
            var claims = new HashMap<String, Object>();
            claims.put("id", user.getId());
            claims.put("fullName", user.getFullName());
            claims.put("firstName", user.getFirstname());
            claims.put("lastName", user.getLastname());
            claims.put("phoneNumber", user.getPhoneNumber());
            claims.put("authProvider", user.getLoginProvider().name());
            String jwt = jwtService.generateToken(claims, user);

            return AuthenticationResDTO.builder().token(jwt).build();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private User processOAuth2User(GoogleUserInfo oAuth2UserInfo) {
        Optional<User> userOptional = userRepository.findByEmailAndLoginProvider(
                oAuth2UserInfo.getEmail(),
                LoginProvider.GOOGLE
        );

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            Role userRole = roleRepository.findByName(String.valueOf(Roles.ROLE_USER))
                    .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.ROLE_USER_WAS_NOT_INITIALIZED));

            user = userRepository.save(User
                            .builder()
                            .firstname(oAuth2UserInfo.getGiven_name())
                            .lastname(oAuth2UserInfo.getFamily_name())
                            .email(oAuth2UserInfo.getEmail())
                            .phoneNumber(null)
                            .password(passwordEncoder.encode(oAuth2UserInfo.getEmail()))
                            .loginProvider(LoginProvider.GOOGLE)
                            .accountLocked(Boolean.FALSE)
                            .enabled(Boolean.TRUE)
                            .roles(List.of(userRole))
                            .build()
            );
        }

        return user;
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        String userInfoUrl = applicationProperties.getGoogleUserinfo();

        return restTemplate.getForObject(
                userInfoUrl + "?access_token=" + accessToken,
                GoogleUserInfo.class
        );
    }

    private GoogleTokenResDTO exchangeCodeForToken(String code) {
        String tokenUrl = applicationProperties.getGoogleTokenUri();

        Map<String, String> params = Map.of(
                "code", code,
                "client_id", applicationProperties.getGoogleClientId(),
                "client_secret", applicationProperties.getGoogleClientSecret(),
                "redirect_uri", applicationProperties.getRedirectUri(),
                "grant_type", "authorization_code"
        );

        return restTemplate.postForObject(tokenUrl, params, GoogleTokenResDTO.class);
    }

    private String buildGoogleAuthUrl() {
        String baseUrl = applicationProperties.getGoogleAuthorization();
        String redirectUriEncoded = URLEncoder.encode(applicationProperties.getRedirectUri(), StandardCharsets.UTF_8);

        String scope = URLEncoder.encode("openid profile email", StandardCharsets.UTF_8);

        return baseUrl +
                "?client_id=" + applicationProperties.getGoogleClientId() +
                "&redirect_uri=" + redirectUriEncoded +
                "&response_type=code" +
                "&scope=" + scope +
                "&access_type=offline" +
                "&prompt=consent";
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationOtp(user);
        if (newToken == null) {
            throw new IllegalArgumentException(ExceptionMessages.ACTIVATION_EMAIL_SENT_WAIT_5_MINUTES);
        } else {
            EMailService.sendValidateEmail(
                    user.getEmail(),
                    user.getFullName(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    properties.getRedirectUri() + "?otp=" + newToken,
                    newToken,
                    AppConstant.SUBJECT_MAIL
            );
        }
    }

    private void sendValidationPhone(User user) {
        String otp = generateAndSaveActivationOtp(user);
        if (otp == null) {
            throw new IllegalArgumentException(ExceptionMessages.ACTIVATION_EMAIL_SENT_WAIT_5_MINUTES);
        } else {
            smsService.sendSms(user.getPhoneNumber(), otp);
        }
    }

    private String generateAndSaveActivationOtp(User user) {
        String userId = user.getId().toString();
        String userActivationKey = RedisKeyConfig.getUserActivationKey(userId);

        String existingOtp = redisService.get(userActivationKey);

        if (existingOtp != null) {
            String otp = RedisKeyConfig.getActivationOtpKey(existingOtp);
//            long ttl = redisService.getTtl(otp);
//            int minResendInterval = RedisKeyConfig.OTP_EXPIRED_TIME - RedisKeyConfig.OTP_TIME_RESENT;
//
//            if (ttl > minResendInterval) {
//                return null;
//            }

            redisService.save(otp, userId, RedisKeyConfig.OTP_EXPIRED_TIME);
            redisService.save(userActivationKey, existingOtp, RedisKeyConfig.OTP_EXPIRED_TIME);
            return existingOtp;
        } else {
            String generatedOtp = generateActivationCode(AppConstant.ACTIVATION_CODE_LENGTH);
            String otp = RedisKeyConfig.getActivationOtpKey(generatedOtp); // bugs
            redisService.save(otp, userId, RedisKeyConfig.OTP_EXPIRED_TIME);
            redisService.save(userActivationKey, otp, RedisKeyConfig.OTP_EXPIRED_TIME);
            return generatedOtp;
        }
    }

    private String generateActivationCode(int length) {
        SecureRandom random = new SecureRandom();
        return random.ints(length, 0, 10)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
