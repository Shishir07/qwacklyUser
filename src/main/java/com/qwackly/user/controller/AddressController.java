package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.AddressEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.service.AddressService;
import com.qwackly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/v1")
@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;

    private static Map<String,String> response= new HashMap<>();

    @GetMapping(value = "/address/users/{userId}")
    public ResponseEntity<AddressEntity> getAddressOfUser(@PathVariable Integer userid){
        UserEntity userEntity=userService.getUserDetails(userid);
        AddressEntity addressEntity;
        try{
            addressEntity=addressService.findAddressByUserEntity(userEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(addressEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/address/{id}")
    public ResponseEntity<AddressEntity> getAddress(@PathVariable Integer id){
        AddressEntity addressEntity;
        try{
            addressEntity=addressService.findAddressById(id);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(addressEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/address")
    public ResponseEntity<Map> addAddress(@RequestBody AddressEntity addressEntity){
        AddressEntity address = new AddressEntity();
        if (Objects.nonNull(addressService.findAddressByUserEntity(addressEntity.getUserEntity()))){
            response.put("status","SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        try {
            addressService.addAddress(addressEntity);
            response.put("status","SUCCESS");
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/address")
    public ResponseEntity<Map> updateAddress(@RequestBody AddressEntity addressEntity){
        AddressEntity address = new AddressEntity();
        if (Objects.nonNull(addressService.findAddressById(addressEntity.getId()))){
            response.put("errorMessage","Address not added yet");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            addressService.addAddress(addressEntity);
            response.put("status","SUCCESS");
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
