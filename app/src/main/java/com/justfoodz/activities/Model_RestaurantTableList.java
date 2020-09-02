package com.justfoodz.activities;

import com.justfoodz.models.Citymodel;

import java.util.ArrayList;

public class Model_RestaurantTableList {

    public ArrayList<Citymodel> getRestaurantTableList() {
        return RestaurantTableList;
    }

    public void setRestaurantTableList(ArrayList<Citymodel> restaurantTableList) {
        RestaurantTableList = restaurantTableList;
    }

    public ArrayList<Citymodel>RestaurantTableList;
}
