package com.justfoodz.fragments;

import com.justfoodz.models.RestaurantModel;

import java.util.ArrayList;

public class Model_feature_restaurant_List {

    public ArrayList<Model_feature_restaurant> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<Model_feature_restaurant> searchResult) {
        this.searchResult = searchResult;
    }

    public ArrayList<Model_feature_restaurant>searchResult;
}
