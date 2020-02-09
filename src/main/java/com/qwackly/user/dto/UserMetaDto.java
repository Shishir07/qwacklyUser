package com.qwackly.user.dto;

import java.io.Serializable;

public class UserMetaDto implements Serializable {

    private String firstName;
    private Integer age;
    private String gender;
    private String contactNumber;
    private String email;

    public UserMetaDto(String firstName, Integer age, String gender, String contactNumber, String email) {
        this.firstName = firstName;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
