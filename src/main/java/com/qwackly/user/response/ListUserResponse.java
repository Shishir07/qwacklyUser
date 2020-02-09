package com.qwackly.user.response;

import com.qwackly.user.model.UserEntity;

import java.util.List;

public class ListUserResponse extends CommonAPIResponse {

    private List<UserEntity> listOfUsers;

    public void setListOfUsers(List<UserEntity> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
}
