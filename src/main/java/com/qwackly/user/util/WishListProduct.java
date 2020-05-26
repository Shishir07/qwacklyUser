package com.qwackly.user.util;

import com.qwackly.user.model.ProductEntity;

import java.io.Serializable;

public class WishListProduct implements Serializable {

    private ProductEntity productEntity;
    private boolean isWished;

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public boolean isWished() {
        return isWished;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }
}
