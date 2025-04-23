package com.bm.travelcore.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, "No code", NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorect", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "The new password does not match", BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account is locked", FORBIDDEN),
    ACCOUNT_DISABLE(303, "User account is disabled", FORBIDDEN),
    BAD_CREDENTIALS(304, "Login and / or password is incorrect", FORBIDDEN),
    NOT_FOUND(305, "Resource not found", HttpStatus.NOT_FOUND),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
