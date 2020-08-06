package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orderShipmentDetails")
public class OrderShipmentDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    private String currentStatus;
    @OneToOne
    @JoinColumn(name = "orderShipmentId")
    private OrderShipmentEntity orderShipmentEntity;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdTimestamp, modifiedTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public OrderShipmentEntity getOrderShipmentEntity() {
        return orderShipmentEntity;
    }

    public void setOrderShipmentEntity(OrderShipmentEntity orderShipmentEntity) {
        this.orderShipmentEntity = orderShipmentEntity;
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
