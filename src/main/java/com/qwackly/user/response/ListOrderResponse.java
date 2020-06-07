package com.qwackly.user.response;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListOrderResponse extends CommonAPIResponse {

    private List<OrderProductEntity> listOfOrders;

    public List<OrderProductEntity> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<OrderProductEntity> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }
}
