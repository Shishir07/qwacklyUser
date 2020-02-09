package com.qwackly.user.service;

import com.qwackly.user.model.CelebTagsEntity;
import com.qwackly.user.repository.CelebTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CelebTagsService {

    @Autowired
    CelebTagsRepository celebTagsRepository;

    public CelebTagsEntity getCelebTagsEntity(String tags){
        CelebTagsEntity celebTagsEntity= celebTagsRepository.findByTags(tags);
        return celebTagsEntity;
    }

    public boolean isItExistingTag(String tags){
        CelebTagsEntity celebTagsEntity = celebTagsRepository.findByTags(tags);
        return celebTagsEntity==null ? false : true;
    }

    public void addNewTags(CelebTagsEntity celebTagsEntity){
        celebTagsRepository.save(celebTagsEntity);
    }
}
