package com.qwackly.user.config;

import com.qwackly.user.action.ErrorAction;
import com.qwackly.user.action.MakePaymentAction;
import com.qwackly.user.enums.OrderEvent;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    public Object getVariable(StateContext<OrderStatus, OrderEvent> stateContext, String variableName){
        return stateContext.getMessage().getHeaders().get(variableName);
    }

    public static boolean hasErrorInTransition(StateMachine<OrderStatus,OrderEvent> stateMachine){
        return (Boolean)stateMachine.getExtendedState().getVariables().getOrDefault(Constants.HAS_ERROR, false);
    }



    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.PAYMENT_INITIATED)
                .state(OrderStatus.PAYMENT_COMPLETED)
                .state(OrderStatus.PAYMENT_FAILED)
                .state(OrderStatus.COLLECTED_FROM_CELEB)
                .state(OrderStatus.OUT_FOR_DELIVERY)
                .state(OrderStatus.REFUND_INITIATED)
                .end(OrderStatus.DELIVERED)
                .end(OrderStatus.PAYMENT_REVERSED);
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatus.PAYMENT_INITIATED).target(OrderStatus.PAYMENT_COMPLETED)
                .event(OrderEvent.INITIATE_PAYMENNT)
                .action(new MakePaymentAction(), new ErrorAction())
                .and().withExternal()
                .source(OrderStatus.PAYMENT_COMPLETED).target(OrderStatus.DELIVERED)
                .event(OrderEvent.DELIVER);
    }


    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {

        StateMachineListenerAdapter<OrderStatus, OrderEvent> adapter = new StateMachineListenerAdapter<OrderStatus, OrderEvent>() {
            @Override
            public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
                System.out.println(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
            }
        };
        config.withConfiguration()
                .autoStartup(false)
                .listener(adapter);
    }
}
