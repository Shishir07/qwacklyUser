package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwackly.user.enums.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    @Column(name = "state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus state;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
    @CreationTimestamp
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    private Timestamp  modifiedTimestamp;

    public OrderEntity (String id,UserEntity userEntity,OrderStatus state){
        this.id=id;
        this.userEntity=userEntity;
        this.state=state;
    }

    public OrderEntity(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getState() {
        return state;
    }

    public void setState(OrderStatus state) {
        this.state = state;
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

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
