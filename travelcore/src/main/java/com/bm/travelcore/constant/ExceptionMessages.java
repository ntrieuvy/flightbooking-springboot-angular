package com.bm.travelcore.constant;

public class ExceptionMessages {

    public static final String ROLE_USER_WAS_NOT_INITIALIZED = "ROLE USER WAS NOT INITIALIZED";
    public static final String USER_ALREADY_EXISTS = "User with this %s already exists.";
    public static final String USER_ALREADY_ACTIVE_OR_OTP_INVALID = "The user is already active, or the activation token is invalid or has expired.";
    public static final String USER_NOT_FOUND = "User not found with %s: %s";
    public static final String ACTIVATION_EMAIL_SENT_WAIT_5_MINUTES = "An activation email has been sent to your inbox. If you do not receive it, please wait 5 minutes before requesting another one.";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email: %s";
    public static final String USER_NOT_FOUND_WITH_PHONE = "User not found with phone number: %s";
}
