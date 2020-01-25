package com.qwackly.user.service;

import com.qwackly.user.input.AddCelebDetails;
import com.qwackly.user.model.CelebEntity;
import com.qwackly.user.repository.CelebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CelebService {

    @Autowired
    CelebRepository celebRepository;

    public  List<CelebEntity> getAllCelebs(){
        List<CelebEntity> celebs=celebRepository.findAll();
        return celebs;
    }

    public void addCelebrity(AddCelebDetails addCelebDetails){
        celebRepository.save(addCelebDetails);
    }
}
