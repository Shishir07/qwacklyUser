package com.qwackly.user.util;

import java.io.Serializable;


public class Images implements Serializable {


    private String imageUrl;
    private String productInfoUrl;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductInfoUrl() {
        return productInfoUrl;
    }

    public void setProductInfoUrl(String productInfoUrl) {
        this.productInfoUrl = productInfoUrl;
    }
}
