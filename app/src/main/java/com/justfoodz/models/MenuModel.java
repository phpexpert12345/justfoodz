package com.justfoodz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.justfoodz.models.SubList.RestaurantMencategory;

import java.util.List;

public class MenuModel {

    @SerializedName("RestaurantMencategory")
    @Expose
    private List<RestaurantMencategory> restaurantMencategory = null;

    public List<RestaurantMencategory> getRestaurantMencategory() {
        return restaurantMencategory;
    }

    public void setRestaurantMencategory(List<RestaurantMencategory> restaurantMencategory) {
        this.restaurantMencategory = restaurantMencategory;
    }

    private int id;
    private String restaurantId;
    private String scObjId;
    private String categoryName;
    private String categoryDescription;
    private String categoryImg;
    private Integer error;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getScObjId() {
        return scObjId;
    }

    public void setScObjId(String scObjId) {
        this.scObjId = scObjId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
}
