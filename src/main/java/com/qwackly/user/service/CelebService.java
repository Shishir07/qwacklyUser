package com.qwackly.user.service;

import com.qwackly.user.model.CelebEntity;
import com.qwackly.user.repository.CelebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CelebService {

    @Autowired
    CelebRepository celebRepository;

    public  List<CelebEntity> getAllCelebs(){
        List<CelebEntity> celebs=celebRepository.findAll();
        return celebs;
    }

    public void addCelebrity(CelebEntity addCelebDetails){
        celebRepository.save(addCelebDetails);
    }

    public  CelebEntity getCelebById(Integer id){
        Optional<CelebEntity> optionalCelebEntity = celebRepository.findById(id);
        CelebEntity celebEntity;
        if (optionalCelebEntity.isPresent()) {
            celebEntity = optionalCelebEntity.get();
        }
        else {
            celebEntity  = new CelebEntity();
        }
        return celebEntity;
    }
}
