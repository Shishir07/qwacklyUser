package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@Component
@Entity
@Table(name = "orderProducts", uniqueConstraints = @UniqueConstraint(
        columnNames = { "orderId", "productId" }))
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String state;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private OrderEntity orderEntity;
    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productEntity;
    @CreationTimestamp
    private Timestamp createdTimestamp, modifiedTimestamp;

    public OrderProductEntity(){

    }

    public OrderProductEntity(String state, OrderEntity orderEntity, ProductEntity productEntity){
        this.state=state;
        this.orderEntity=orderEntity;
        this.productEntity=productEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Timestamp getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }
}
