package com.qwackly.user.service;

import com.qwackly.user.dto.StateContextDto;
import com.qwackly.user.enums.OrderEvent;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.Optional;

import static com.qwackly.user.config.OrderStateMachineConfig.hasErrorInTransition;

public class OrderLifeCycleService {

    private OrderService orderService;
    private OrderProductService orderProductService;
    private WishListService wishListService;
    private StateMachineFactory<OrderStatus, OrderEvent> factory;

    @Autowired
    public OrderLifeCycleService(OrderService orderService, OrderProductService orderProductService,
                                 WishListService wishListService,StateMachineFactory<OrderStatus,OrderEvent> factory){
        this.orderService=orderService;
        this.orderProductService=orderProductService;
        this.wishListService=wishListService;
        this.factory=factory;
    }

    private StateMachine<OrderStatus, OrderEvent> build(String orderId) {
        OrderEntity order = this.orderService.getOrder(orderId);

        StateMachine<OrderStatus, OrderEvent> stateMachine = this.factory.getStateMachine(orderId);
        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {

                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<OrderStatus, OrderEvent>() {

                        @Override
                        public void postStateChange(State<OrderStatus, OrderEvent> state, Message<OrderEvent> message, Transition<OrderStatus, OrderEvent> transition, StateMachine<OrderStatus, OrderEvent> stateMachine) {

                            Optional.ofNullable(message).ifPresent(msg -> {

                                Optional.ofNullable(StateContextDto.class.cast(msg.getHeaders().getOrDefault(Constants.STATE_CONTEXT, -1L)))
                                        .ifPresent(stateContextDto -> {
                                            OrderEntity orderEntity = orderService.getOrder(stateContextDto.getOrderId());

                                            orderEntity.setState(state.getId());
                                            orderService.addOrder(orderEntity);
                                        });
                            });

                        }
                    });
                    sma.resetStateMachine(new DefaultStateMachineContext<>(order.getState(), null, null, null));
                });
        stateMachine.start();
        return stateMachine;
    }

    public StateMachine<OrderStatus, OrderEvent> makePayment(String orderId, OrderLifeCycleService orderLifeCycleService)  {
        StateMachine<OrderStatus, OrderEvent> stateMachine = this.build(orderId);

        StateContextDto stateContextDto = new StateContextDto();

        stateContextDto.setOrderId(orderId);
        stateContextDto.setOrderLifeCycleService(orderLifeCycleService);

        Message<OrderEvent> makePaymentMessage = MessageBuilder.withPayload(OrderEvent.INITIATE_PAYMENNT)
                .setHeader(Constants.STATE_CONTEXT, stateContextDto)
                .build();
        stateMachine.sendEvent(makePaymentMessage);

        if(hasErrorInTransition(stateMachine)){
            throw new QwacklyException(new StringBuilder("Failed to make the payment for ").append(orderId).toString(), ResponseStatus.FAILURE);
        }

        return stateMachine;
    }



}
