package com.qwackly.user.repository;

import com.qwackly.user.model.CelebTagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CelebTagsRepository extends JpaRepository<CelebTagsEntity, Integer> {

    CelebTagsEntity findByTags(String tags);
}
