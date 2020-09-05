package com.qwackly.user.repository;

import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.model.EmailEntity;
import com.qwackly.user.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, String> {

    EmailEntity findByOrderEntity(OrderEntity orderEntity);
    EmailEntity findById(Integer id);
    List<EmailEntity> findByStatus(EmailStatus emailStatus);
}
