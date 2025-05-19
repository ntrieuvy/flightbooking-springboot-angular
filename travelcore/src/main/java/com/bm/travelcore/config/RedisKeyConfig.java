package com.bm.travelcore.config;

public class RedisKeyConfig {
    public static final String ACTIVATION_OTP_PREFIX = "activation:";
    public static final String USER_ACTIVATION_PREFIX = "user_activation:";

    //
    public static final Integer OTP_TIME_RESENT = 5;
    public static final Integer OTP_EXPIRED_TIME = 15;

    //
    public static final String HOLD_FLIGHT_PREFIX = "hold_flight:";
    public static final String HOLD_FLIGHT_DOC_CODE_PREFIX = "doc_code:";

    public static String getActivationOtpKey(String otp) {
        return ACTIVATION_OTP_PREFIX + otp;
    }

    public static String getUserActivationKey(String userId) {
        return USER_ACTIVATION_PREFIX + userId;
    }

    public static String getHoldFlightKey(String orderId) {
        return HOLD_FLIGHT_PREFIX + orderId;
    }

    public static String getDocCodeKey(String documentCode) {
        return HOLD_FLIGHT_DOC_CODE_PREFIX + documentCode;
    }

}