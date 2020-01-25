package com.qwackly.user.controller;

import com.qwackly.user.model.CelebEntity;
import com.qwackly.user.response.CelebApiResponse;
import com.qwackly.user.service.CelebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1")
@RestController
public class CelebrityController {

    @Autowired
    CelebService celebService;

    @RequestMapping(value = "/celebs")
    public CelebApiResponse getCelebs(){
        CelebApiResponse celebApiResponse = new CelebApiResponse();
        List<CelebEntity> celebs;
        try {
           celebs= celebService.getAllCelebs();
           celebApiResponse.setCelebEntities(celebs);
           celebApiResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            celebApiResponse.setErrorMessage(e.getMessage());
            celebApiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return celebApiResponse;
    }
}
