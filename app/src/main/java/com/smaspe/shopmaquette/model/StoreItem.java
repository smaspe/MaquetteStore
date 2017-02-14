package com.smaspe.shopmaquette.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 13/02/17.
 */

public class StoreItem {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private int price;

    @SerializedName("description")
    private String description;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("in_stock")
    private boolean inStock;

    // as hex strings
    @SerializedName("colours")
    private List<String> colours;

    @SerializedName("size")
    private String size;

    @SerializedName("image_url")
    private String imageURL;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public boolean isInStock() {
        return inStock;
    }

    public List<String> getColours() {
        return colours;
    }

    public String getDescription() {
        return description;
    }

    public String getSize() {
        return size;
    }
}
