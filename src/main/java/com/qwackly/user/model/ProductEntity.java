package com.qwackly.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwackly.user.util.Images;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "products")
@TypeDefs({

        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "json-node", typeClass = JsonNodeStringType.class)
})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "celebId")
    private CelebEntity celebEntity;
    @ManyToOne
    @JoinColumn(name = "ngoId")
    private NgoEntity ngo;
    private String name;
    private String shortDescription;
    @Column(columnDefinition = "TEXT")
    private String longDescription;
    @Column(name = "price")
    private Integer price;
    @Column(name = "delivery_charge")
    private Integer deliveryCharge;

    @Formula("0.18 * price")
    private Integer gst;

    @Formula("1.18*price + delivery_charge")
    private Integer finalPrice;

    private Integer noOfProducts;

    private Integer percentageDonation;

    private String thumbNailImage;

    @Type(type = "serializable")
    private List<Images> images;

    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdTimestamp;

    @UpdateTimestamp
    @JsonIgnore
    private Timestamp  modifiedTimestamp;

    private String productType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CelebEntity getCelebEntity() {
        return celebEntity;
    }

    public void setCelebEntity(CelebEntity celebEntity) {
        this.celebEntity = celebEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Timestamp getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Timestamp modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getThumbNailImage() {
        return thumbNailImage;
    }

    public void setThumbNailImage(String thumbNailImage) {
        this.thumbNailImage = thumbNailImage;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public Integer getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Integer deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Integer getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(Integer noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public NgoEntity getNgo() {
        return ngo;
    }

    public void setNgo(NgoEntity ngo) {
        this.ngo = ngo;
    }

    public Integer getPercentageDonation() {
        return percentageDonation;
    }

    public void setPercentageDonation(Integer percentageDonation) {
        this.percentageDonation = percentageDonation;
    }

    public Integer getGst() {
        return gst;
    }

    public void setGst(Integer gst) {
        this.gst = gst;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

}
