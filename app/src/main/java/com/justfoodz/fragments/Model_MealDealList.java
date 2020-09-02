package com.justfoodz.fragments;

import com.justfoodz.models.RestaurantModel;

import java.util.ArrayList;

public class Model_MealDealList {

    public ArrayList<RestaurantModel> getSearchResult() {
        return MealDealList;
    }

    public void setSearchResult(ArrayList<RestaurantModel> searchResult) {
        this.MealDealList = searchResult;
    }

    public ArrayList<RestaurantModel> MealDealList;
}
