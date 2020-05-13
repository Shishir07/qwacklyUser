package com.qwackly.user.handler;

import com.qwackly.user.exception.QwacklyException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @Data
    private class JsonResponse {
        String errorMessage;

        public JsonResponse() {
        }

        public JsonResponse(String errorMessage) {
            super();
            this.errorMessage = errorMessage;
        }
    }

    @ExceptionHandler(QwacklyException.class)
    public ResponseEntity<JsonResponse> qwacklyException(QwacklyException ex){
        return new ResponseEntity<>(new JsonResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
