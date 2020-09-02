package com.justfoodz.models;

public class FeatureAminitiesModel {
    private Integer id;
    private String restaurantfeaturename;
    public String RestaurantServiceName;
    public String service_img;



    public String getRestaurantServiceName() {
        return RestaurantServiceName;
    }

    public void setRestaurantServiceName(String restaurantServiceName) {
        RestaurantServiceName = restaurantServiceName;
    }

    public String getService_img() {
        return service_img;
    }

    public void setService_img(String service_img) {
        this.service_img = service_img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantfeaturename() {
        return restaurantfeaturename;
    }

    public void setRestaurantfeaturename(String restaurantfeaturename) {
        this.restaurantfeaturename = restaurantfeaturename;
    }
}

