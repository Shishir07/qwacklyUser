package com.qwackly.user.exception;

import com.qwackly.user.enums.ResponseStatus;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class QwacklyException extends RuntimeException {

    private String errorMessage;
    private Integer errorCode;
    private ResponseStatus status;

    public QwacklyException(String errorMessage, ResponseStatus status)
    {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.status= status;
    }

    public QwacklyException(String errorMessage, Integer errorCode, ResponseStatus status)
    {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.status = status;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
