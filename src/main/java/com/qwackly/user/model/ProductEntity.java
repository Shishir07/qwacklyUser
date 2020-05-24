package com.qwackly.user.model;

import com.qwackly.user.util.Images;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
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
    private String name;
    private String shortDescription;
    @Column(columnDefinition = "TEXT")
    private String longDescription;
    private Integer price;
    private String thumbNailImage;

    @Type(type = "serializable")
    private List<Images> images;

    @CreationTimestamp
    private Timestamp createdTimestamp,modifiedTimestamp;

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
}
