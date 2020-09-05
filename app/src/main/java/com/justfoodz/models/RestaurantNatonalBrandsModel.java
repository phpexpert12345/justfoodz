package com.justfoodz.models;

import java.io.Serializable;

public class RestaurantNatonalBrandsModel {
    public String id;

    public RestaurantNatonalBrandsModel(String id, String servicename,String serviceimage) {
        this.RestaurantCuisineName=servicename;
        this.cuisine_img=serviceimage;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRestaurantCuisineName() {
        return RestaurantCuisineName;
    }

    public void setRestaurantCuisineName(String restaurantCuisineName) {
        RestaurantCuisineName = restaurantCuisineName;
    }

    public String getCuisine_img() {
        return cuisine_img;
    }

    public void setCuisine_img(String cuisine_img) {
        this.cuisine_img = cuisine_img;
    }

    public String RestaurantCuisineName;
    public String cuisine_img;
    public String error;
}