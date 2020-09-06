package com.qwackly.user.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CashFreeCreateOrderResponse {
    private String status;
    private String paymentLink;
    private String reason;
}
