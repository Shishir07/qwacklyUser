package com.qwackly.user.repository;

import com.qwackly.user.model.CelebEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelebRepository extends JpaRepository<CelebEntity, Integer> {
    //CelebEntity findById(Integer id);
    CelebEntity findByName(String name);
}
