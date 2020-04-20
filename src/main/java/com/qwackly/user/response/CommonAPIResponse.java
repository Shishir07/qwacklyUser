package com.qwackly.user.response;

import org.springframework.stereotype.Component;

@Component
public class CommonAPIResponse {

    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
