package com.qwackly.user.model;

import com.qwackly.user.enums.EmailStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "emails")
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "orderId")
    private OrderEntity orderEntity;

    private String orderAmount;

    private String customerEmail;

    private String customerName;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EmailStatus status;

    @CreationTimestamp
    private Timestamp createdTimestamp,modifiedTimestamp;

    public EmailEntity (OrderEntity orderEntity, String orderAmount, String customerEmail, String customerName ,EmailStatus emailStatus){
        this.orderEntity = orderEntity;
        this.orderAmount = orderAmount;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.status = emailStatus;
    }
}
