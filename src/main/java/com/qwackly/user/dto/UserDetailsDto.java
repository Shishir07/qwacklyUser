package com.qwackly.user.dto;

import com.qwackly.user.model.UserEntity;

import java.io.Serializable;
import java.util.Date;

public class UserDetailsDto implements Serializable {

    private Integer id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String gender;
    private Date dob;
    private String contactNumber;
    private String email;
    private String country;
    private String state;
    private String[] preferences;

    public UserDetailsDto(Integer id, String firstName, String secondName, Integer age, String gender, Date dob, String contactNumber, String email, String country, String state, String[] preferences) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.contactNumber = contactNumber;
        this.email = email;
        this.country = country;
        this.state = state;
        this.preferences = preferences;
    }

    public UserDetailsDto(UserEntity user){
        firstName = user.getFirstName();
        secondName = user.getSecondName();
        age = user.getAge();
        gender = user.getGender();
        dob = user.getDob();
        contactNumber = user.getContactNumber();
        email = user.getEmail();
        country = user.getCountry();
        state = user.getState();
        preferences = user.getPreferences();
    }

    public UserDetailsDto() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String[] getPreferences() {
        return preferences;
    }

    public void setPreferences(String[] preferences) {
        this.preferences = preferences;
    }
}
