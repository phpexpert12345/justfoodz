package com.justfoodz.fragments;

import com.justfoodz.models.RestaurantFreeDeliveryModel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.models.SubList.RestaurantMencategory;

import java.util.ArrayList;

public class Model_Free_Delivery_Reasurant {

    public ArrayList<RestaurantModel> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<RestaurantModel> searchResult) {
        this.searchResult = searchResult;
    }

    public ArrayList<RestaurantModel> searchResult;
}
