package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orderPrice")
public class OrderPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "orderId")
    private OrderEntity orderEntity;
    @Column(name = "price")
    private Integer price;
    @Column(name = "delivery_charge")
    private Integer deliveryCharge =0;

    @Column(name = "discount")
    private Integer discount = 0;

    @Formula("price - discount")
    private Integer discountedprice;

    @Formula("0.18 * (price - discount)")
    private Integer gst;

    @Formula("1.18*(price - discount)")
    private Integer finalPrice;

    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    @JsonIgnore
    private Timestamp  modifiedTimestamp;

    public OrderPriceEntity(OrderEntity orderEntity, Integer price){
        this.orderEntity=orderEntity;
        this.price=price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Integer deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getDiscountedprice() {
        return discountedprice;
    }

    public void setDiscountedprice(Integer discountedprice) {
        this.discountedprice = discountedprice;
    }

    public Integer getGst() {
        return gst;
    }

    public void setGst(Integer gst) {
        this.gst = gst;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
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
