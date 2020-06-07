package com.qwackly.user.service;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.AddressEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public AddressEntity findAddressByUserEntity(UserEntity userEntity){
        AddressEntity addressEntity;
        try {
             addressEntity= addressRepository.findByUserEntity(userEntity);
        }
        catch (Exception e){
            throw new QwacklyException("Address not found", ResponseStatus.FAILURE);
        }
        return addressEntity;
    }

    public AddressEntity findAddressById(Integer id){
        AddressEntity addressEntity;
        try {
            addressEntity= addressRepository.findById(id);
        }
        catch (Exception e){
            throw new QwacklyException("Address not found", ResponseStatus.FAILURE);
        }
        return addressEntity;
    }

    public void addAddress(AddressEntity addressEntity){
        try {
            addressRepository.save(addressEntity);
        }
        catch (Exception e){
            throw new QwacklyException("Error while saving password",ResponseStatus.FAILURE);
        }
    }
}
