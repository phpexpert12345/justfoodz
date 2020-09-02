package com.justfoodz.models;

public class CuisineModel {
    public String id, Cuisinename, Cuisineimage;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuisinename() {
        return Cuisinename;
    }

    public void setCuisinename(String cuisinename) {
        Cuisinename = cuisinename;
    }

    public String getCuisineimage() {
        return Cuisineimage;
    }

    public void setCuisineimage(String cuisineimage) {
        Cuisineimage = cuisineimage;
    }



    public String RestaurantCuisineName;

    public String getRestaurantCuisineName() {
        return RestaurantCuisineName;
    }

    public void setRestaurantCuisineName(String restaurantCuisineName) {
        RestaurantCuisineName = restaurantCuisineName;
    }

    public String getRestaurantCuisineContent() {
        return RestaurantCuisineContent;
    }

    public void setRestaurantCuisineContent(String restaurantCuisineContent) {
        RestaurantCuisineContent = restaurantCuisineContent;
    }

    public String getCuisine_img() {
        return cuisine_img;
    }

    public void setCuisine_img(String cuisine_img) {
        this.cuisine_img = cuisine_img;
    }

    public String RestaurantCuisineContent;
    public String cuisine_img;
}
