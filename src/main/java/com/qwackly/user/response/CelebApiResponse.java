package com.qwackly.user.response;

import com.qwackly.user.model.CelebEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CelebApiResponse extends CommonAPIResponse{

    private CelebEntity celebEntity;
    private List<CelebEntity> celebEntities;

    public CelebEntity getCelebEntity() {
        return celebEntity;
    }

    public void setCelebEntity(CelebEntity celebEntity) {
        this.celebEntity = celebEntity;
    }

    public List<CelebEntity> getCelebEntities() {
        return celebEntities;
    }

    public void setCelebEntities(List<CelebEntity> celebEntities) {
        this.celebEntities = celebEntities;
    }

}
