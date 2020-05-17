package com.qwackly.user.action;

import com.qwackly.user.dto.StateContextDto;
import com.qwackly.user.enums.OrderEvent;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Objects;

import static com.qwackly.user.utils.StateContextUtil.getVariable;

public class MakePaymentAction implements Action<OrderStatus, OrderEvent> {
    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> stateContext) {

        StateContextDto stateContextDto = getVariable(stateContext);

        String orderId = stateContextDto.getOrderId();
        String message = "Payment successful for "+orderId;

        if (Objects.isNull(message) ) {
            throw new QwacklyException(new StringBuilder("Failed to make the payment for ").append(orderId).toString(), ResponseStatus.FAILURE);
        }

    }
}

