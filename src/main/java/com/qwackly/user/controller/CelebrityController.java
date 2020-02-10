package com.qwackly.user.controller;

import com.qwackly.user.model.CelebEntity;
import com.qwackly.user.model.CelebTagsEntity;
import com.qwackly.user.request.CelebTagsRequest;
import com.qwackly.user.response.CelebApiResponse;
import com.qwackly.user.response.CelebTagsApiResponse;
import com.qwackly.user.service.CelebService;
import com.qwackly.user.service.CelebTagsService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/v1")
@RestController
public class CelebrityController {

    @Autowired
    CelebService celebService;

    @Autowired
    CelebTagsService celebTagsService;

    @Autowired
    CelebTagsApiResponse celebTagsApiResponse;

    @Autowired
    CelebTagsEntity celebTagsEntity;

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

    @RequestMapping(value = "/celebTags", method = RequestMethod.GET )
    public ResponseEntity<CelebTagsApiResponse> getCelebsGivenTags(@RequestParam ("tags") String tags){
        try {
            Integer[] celebIdList =celebTagsService.getCelebTagsEntity(tags).getCelebirtyList();
            List<CelebEntity> celebsList = Arrays.stream(celebIdList).map(
                    x-> celebService.getCelebById(x)
            ).collect(Collectors.toList());
            celebTagsApiResponse.setCelebIdList(celebsList);
            celebTagsApiResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            celebTagsApiResponse.setErrorMessage(e.getMessage());
            celebTagsApiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(celebTagsApiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/celebTags", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public void addCelebsGivenTags(@RequestBody CelebTagsRequest input){
        try {
            String tags = input.getTags();
            if(!celebTagsService.isItExistingTag(tags)){
                Integer[] celebIdList = new Integer[1];
                celebIdList[0]=input.getCelebId();
                celebTagsEntity.setTags(tags);
                celebTagsEntity.setCelebirtyList(celebIdList);
                celebTagsService.addNewTags(celebTagsEntity);
            }
            else{
                CelebTagsEntity celebTagsEntity = celebTagsService.getCelebTagsEntity(tags);
                Integer[] celebIdList = celebTagsEntity.getCelebirtyList();
                Integer celebId = input.getCelebId();
                Optional<Integer> optional = Arrays.stream(celebIdList).filter(x -> x==celebId).findFirst();
                if(!optional.isPresent()) {
                    celebIdList = ArrayUtils.add(celebIdList, celebId);
                    celebTagsEntity.setCelebirtyList(celebIdList);
                    celebTagsService.addNewTags(celebTagsEntity);
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
