package com.qwackly.user.repository;

import com.qwackly.user.model.AddressEntity;
import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, String> {

    AddressEntity findByUserEntity(UserEntity userEntity);
    List<AddressEntity> findByState(String state);
    List<AddressEntity> findByPinCode(String pinCode);
    List<AddressEntity> findByCity(String city);
    AddressEntity findById(Integer id);
}
