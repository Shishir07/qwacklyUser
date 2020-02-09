package com.qwackly.user.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Entity
@Table(name = "celebrityTags")
public class CelebTagsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tags;
    //@Column(columnDefinition = "integer[]")
    private Integer[] celebirtyList;
    @CreationTimestamp
    private Date creationdt, lastmodified;

    public CelebTagsEntity(String tags,Integer[] celebirtyList){
        this.celebirtyList=celebirtyList;
        this.tags=tags;
    }

    public CelebTagsEntity(){}

    public Integer getId() {
        return id;
    }

    public String getTags() {
        return tags;
    }

    public Date getCreationdt() {
        return creationdt;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public Integer[] getCelebirtyList() {
        return celebirtyList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCreationdt(Date creationdt) {
        this.creationdt = creationdt;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public void setCelebirtyList(Integer[] celebirtyList) {
        this.celebirtyList = celebirtyList;
    }

}
