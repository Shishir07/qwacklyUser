package com.qwackly.user.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "celebrity")
public class CelebEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String category;
    private Integer age;
    private String gender;

    @CreationTimestamp
    private Date creationdt, lastmodified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreationdt() {
        return creationdt;
    }

    public void setCreationdt(Date creationdt) {
        this.creationdt = creationdt;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

}
