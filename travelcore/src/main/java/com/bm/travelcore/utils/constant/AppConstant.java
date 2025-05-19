package com.bm.travelcore.utils.constant;

import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class AppConstant {
    public static final Integer ACTIVATION_CODE_LENGTH = 6;

    // email activation account
    public static final String SUBJECT_MAIL = "TRAVEL TEAM SOLUTIONS - ACTIVATION CODE";
    public static final String TEMPLATE_CONFIRM_ACCOUNT = "confirm-email";
    public static final String TEMPLATE_PROP_USERNAME = "username";
    public static final String TEMPLATE_PROP_CONFIRMATION_URL = "confirmationUrl";
    public static final String TEMPLATE_PROP_ACTIVATION_CODE = "activationCode";


    public static final String COMMISSION_CACHE = "commissions";
    public static final String AIRPORT_GROUP_CACHE = "airportGroups";

    // email booking
    public static final String TEMPLATE_BOOKING_SUCCESS = "booking-success";
    public static final String EMAIL_SUBJECT_BOOKING_SUCCESS = "Booking Confirmation";
    public static final String TEMPLATE_PROP_ORDER_DETAILS = "orderDetails";
    public static final String TEMPLATE_PROP_FLIGHTS = "flights";
    public static final String TEMPLATE_PROP_PASSENGERS = "passengers";
    public static final String TEMPLATE_PROP_CUSTOMER = "customer";
    public static final String TEMPLATE_PROP_INVOICE = "invoice";
    public static final String TEMPLATE_PROP_TOTAL_PRICE = "totalPrice";
    public static final String TEMPLATE_PROP_EXPIRY_DATE = "expiryDate";

    public static final String HOLD_FLIGHT_SUCCESS = "Hold flight successful";
    public static final String HOLD_FLIGHT_FAILED = "Hold flight failed";
    public static final String HOLD_FLIGHT_INVALID_RESPONSE = "Hold flight failed: Invalid response from provider";
}
