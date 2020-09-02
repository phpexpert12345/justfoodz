package com.justfoodz.activities;

import com.justfoodz.models.Restaurantlistmodel;

import java.util.ArrayList;

public class Model_restaurant_list {

    public ArrayList<Restaurantlistmodel> getRestaurantList() {
        return RestaurantList;
    }

    public void setRestaurantList(ArrayList<Restaurantlistmodel> restaurantList) {
        RestaurantList = restaurantList;
    }

    public ArrayList<Restaurantlistmodel> RestaurantList;
}
