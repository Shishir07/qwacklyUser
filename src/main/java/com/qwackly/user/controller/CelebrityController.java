package com.qwackly.user.controller;

import com.qwackly.user.model.CelebEntity;
import com.qwackly.user.response.CelebApiResponse;
import com.qwackly.user.response.CelebTagsApiResponse;
import com.qwackly.user.service.CelebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1")
@RestController
public class CelebrityController {

    @Autowired
    CelebService celebService;

    @Autowired
    CelebTagsApiResponse celebTagsApiResponse;

   /* @Autowired
    CelebTagsEntity celebTagsEntity;*/

    @RequestMapping(value = "/celebs", method = RequestMethod.GET)
    public ResponseEntity<CelebApiResponse> getCelebs(){
        CelebApiResponse celebApiResponse ;
        List<CelebEntity> celebs;
        try {
           celebs= celebService.getAllCelebs();
           celebApiResponse= new CelebApiResponse(celebs);
           celebApiResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            celebApiResponse=new CelebApiResponse();
            celebApiResponse.setErrorMessage(e.getMessage());
            celebApiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(celebApiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/celebs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public void addCelebs(@RequestBody CelebEntity input){
        try {
            celebService.addCelebrity(input);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
