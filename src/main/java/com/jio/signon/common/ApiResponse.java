package com.jio.signon.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class ApiResponse<T> {
    int code;
    String message;
    T data;

    static final String MESSAGE_SUCCESS = "Success";

    static final int CODE_SUCCESS = 2000;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Success<T> extends ApiResponse<T> {
        public Success(T object) {
            code = CODE_SUCCESS;
            message = MESSAGE_SUCCESS;
            data = object;
        }
    }

    public static class Error extends ApiResponse {
        public Error(IApiError ae) {
            code = ae.getCode();
            message = ae.getMessage();
            data = null;
        }

        public Error(IApiError ae, String customMessage) {
            code = ae.getCode();
            message = customMessage;
            data = null;
        }

    }
}