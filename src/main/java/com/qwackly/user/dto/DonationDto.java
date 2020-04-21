package com.qwackly.user.dto;

public class DonationDto {

    private Integer userId;
    private Integer amountDonated;


    public DonationDto(Integer userId,Integer amountDonated){
        this.userId=userId;
        this.amountDonated=amountDonated;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(Integer amountDonated) {
        this.amountDonated = amountDonated;
    }
}
