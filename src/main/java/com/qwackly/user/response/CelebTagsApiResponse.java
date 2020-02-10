package com.qwackly.user.response;

import com.qwackly.user.model.CelebEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CelebTagsApiResponse extends CommonAPIResponse {

    private List<CelebEntity> celebIdList;

    public List<CelebEntity> getCelebIdList() {
        return celebIdList;
    }

    public void setCelebIdList(List<CelebEntity> celebIdList) {
        this.celebIdList = celebIdList;
    }

}
