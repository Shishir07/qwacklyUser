package com.qwackly.user.response;

import com.qwackly.user.model.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListOrderResponse extends CommonAPIResponse {

    private List<OrderEntity> listOfOrders;

    public List<OrderEntity> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<OrderEntity> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }
}
