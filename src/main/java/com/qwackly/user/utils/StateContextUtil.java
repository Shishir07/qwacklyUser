package com.qwackly.user.utils;

import com.qwackly.user.dto.StateContextDto;
import com.qwackly.user.enums.OrderEvent;
import com.qwackly.user.enums.OrderStatus;
import org.springframework.statemachine.StateContext;

public class StateContextUtil {
    public static StateContextDto getVariable(StateContext<OrderStatus, OrderEvent> stateContext){
        return (StateContextDto) stateContext.getMessage().getHeaders().get(Constants.STATE_CONTEXT);
    }
}
