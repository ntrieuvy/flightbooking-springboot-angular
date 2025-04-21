package com.bm.travelcore.config;

public class RedisKeyConfig {
    public static final String ACTIVATION_TOKEN_PREFIX = "activation:";
    public static final String USER_ACTIVATION_PREFIX = "user_activation:";

    //
    public static final Integer TIME_RESENT_TOKEN = 5;
    public static final Integer TOKEN_EXPIRED_TIME = 15;

    public static String getActivationTokenKey(String token) {
        return ACTIVATION_TOKEN_PREFIX + token;
    }

    public static String getUserActivationKey(String userId) {
        return USER_ACTIVATION_PREFIX + userId;
    }
}