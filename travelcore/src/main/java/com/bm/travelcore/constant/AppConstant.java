package com.bm.travelcore.constant;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class AppConstant {
    public static final Integer ACTIVATION_CODE_LENGTH = 6;
    public static final String SUBJECT_MAIL = "TRAVEL TEAM SOLUTIONS - ACTIVATION CODE";
    public static final String TEMPLATE_CONFIRM_ACCOUNT = "confirm-email";
    public static final String TEMPLATE_PROP_USERNAME = "username";
    public static final String TEMPLATE_PROP_CONFIRMATION_URL = "confirmationUrl";
    public static final String TEMPLATE_PROP_ACTIVATION_CODE = "activationCode";
}
