package com.qwackly.user.repository;

import com.qwackly.user.model.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    CouponEntity findByCouponName(String couponName);
}
