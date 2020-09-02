package com.justfoodz.activities;

import com.justfoodz.models.MenuSizeModel;

import java.util.ArrayList;

public class Model_RestaurantMenItemsSize {

    public ArrayList<MenuSizeModel> getRestaurantMenItemsSize() {
        return RestaurantMenItemsSize;
    }

    public void setRestaurantMenItemsSize(ArrayList<MenuSizeModel> restaurantMenItemsSize) {
        RestaurantMenItemsSize = restaurantMenItemsSize;
    }

    public ArrayList<MenuSizeModel>RestaurantMenItemsSize;
}
