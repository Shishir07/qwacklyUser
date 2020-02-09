package com.qwackly.user.response;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CelebTagsApiResponse extends CommonAPIResponse {

    private Integer[] celebIdList;

    public Integer[] getCelebIdList() {
        return celebIdList;
    }

    public void setCelebIdList(Integer[] celebIdList) {
        this.celebIdList = celebIdList;
    }

}
