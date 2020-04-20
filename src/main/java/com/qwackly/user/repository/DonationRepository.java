package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<OrderEntity,Integer> {
    @Query(value = "SELECT user_id, SUM(donation) FROM (SELECT a.* , (SELECT SUM(p.amount) FROM payments p WHERE p.order_id = a.id) as donation FROM orders a ORDER BY user_id ASC) d Group By d.user_id order by sum desc limit 5", nativeQuery = true)
    List<String[]> findTop5();
}
