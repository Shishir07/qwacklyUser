package com.qwackly.user.action;

import com.qwackly.user.enums.OrderEvent;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.utils.Constants;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ErrorAction implements Action<OrderStatus, OrderEvent> {

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> stateContext) {
        stateContext.getExtendedState().getVariables().put(Constants.HAS_ERROR, true);
    }
}