package com.qwackly.user.response;

import org.springframework.stereotype.Component;

@Component
public class CommonAPIResponse {

    private String errorMessage;
    private Integer statusCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
