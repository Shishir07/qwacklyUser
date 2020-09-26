package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "coupons")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String couponName;
    private Integer percentage;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdTimestamp;
    @UpdateTimestamp
    @JsonIgnore
    private Timestamp  modifiedTimestamp;

}
