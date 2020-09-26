package com.qwackly.user.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class SuccessResponse {
    private boolean isSuccess;
}

