package com.qwackly.user.response;

import com.qwackly.user.model.CelebEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CelebApiResponse extends CommonAPIResponse{

    private List<CelebEntity> celebEntities;

    public CelebApiResponse (){}

    public CelebApiResponse(List<CelebEntity> celebEntities){
        this.celebEntities=celebEntities;
    }

    public List<CelebEntity> getCelebEntities() {
        return celebEntities;
    }

    public void setCelebEntities(List<CelebEntity> celebEntities) {
        this.celebEntities = celebEntities;
    }

}
