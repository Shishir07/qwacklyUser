package com.qwackly.user.response;

import com.qwackly.user.dto.UserDetailsDto;

public class UserDetailResponse extends CommonAPIResponse{

    private UserDetailsDto userDetails;

    public void setUserDetails(UserDetailsDto userDetails) {
        this.userDetails = userDetails;
    }

    public UserDetailsDto getUserDetails() {
        return userDetails;
    }

}
