package com.justfoodz.activities;

import com.justfoodz.models.SubMenumodelravi;

import java.util.ArrayList;

public class Model_RestaurantMenuItem {
    public ArrayList<SubMenumodelravi> getRestaurantMenItems() {
        return RestaurantMenItems;
    }

    public void setRestaurantMenItems(ArrayList<SubMenumodelravi> restaurantMenItems) {
        RestaurantMenItems = restaurantMenItems;
    }

    public ArrayList<SubMenumodelravi>RestaurantMenItems;
}
