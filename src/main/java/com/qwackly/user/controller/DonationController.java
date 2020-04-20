package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1")
@RestController
public class DonationController {

    @Autowired
    DonationRepository donationRepository;

    @RequestMapping(value = "/donations", method = RequestMethod.GET )
    public ResponseEntity<List<String[]>> getProducts(){
        List<String[]> donationEntities;
        try {
            donationEntities=donationRepository.findTop5();
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(donationEntities, HttpStatus.OK);
    }
}
