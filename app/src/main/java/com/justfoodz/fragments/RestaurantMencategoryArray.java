package com.justfoodz.fragments;

import com.justfoodz.models.SubList.RestaurantMencategory;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMencategoryArray {

    public List<com.justfoodz.models.SubList.RestaurantMencategory> getRestaurantMencategory() {
        return RestaurantMencategory;
    }

    public void setRestaurantMencategory(List<com.justfoodz.models.SubList.RestaurantMencategory> restaurantMencategory) {
        RestaurantMencategory = restaurantMencategory;
    }

    public List<RestaurantMencategory>RestaurantMencategory;
}
