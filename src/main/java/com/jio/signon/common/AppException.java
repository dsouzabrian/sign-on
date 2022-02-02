package com.jio.signon.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AppException extends Exception {

	int code;
    String message;
    IApiError error;

    @AllArgsConstructor
    @Getter
    public enum Error implements IApiError {

        BAD_REQUEST(2001, "Bad Request"),
        PERMISSION_DENIED(2002, "Permission Denied"),

        // user errors
        USER_ALREADY_EXISTS(2101, "User Already Exists"),
        USER_NOT_FOUND(2102, "User Not found"),


        HARD_ERROR(2999, "Internal Server Error");

        int code;
        String message;
    }

    public AppException(Error er) {
        error = er;
        code = er.getCode();
        message = er.getMessage();
    }

    public AppException(Error er, String customMessage) {
        error = er;
        message = customMessage;
    }
}