package com.justfoodz.fragments;

import com.justfoodz.models.RestaurantModel;

import java.util.ArrayList;

public class Model_Reasurant {

    public ArrayList<RestaurantModel> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<RestaurantModel> searchResult) {
        this.searchResult = searchResult;
    }

    public ArrayList<RestaurantModel> searchResult;
}
