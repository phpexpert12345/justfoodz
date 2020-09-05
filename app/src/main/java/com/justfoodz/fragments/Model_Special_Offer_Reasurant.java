package com.justfoodz.fragments;


import com.justfoodz.models.RestaurantModel;
import com.justfoodz.models.RestaurantNewFoodzModel;
import com.justfoodz.models.RestaurantSpecialOfferModel;

import java.util.ArrayList;

public class Model_Special_Offer_Reasurant {

    public ArrayList<RestaurantModel> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(ArrayList<RestaurantModel> searchResult) {
        this.searchResult = searchResult;
    }

    public ArrayList<RestaurantModel> searchResult;
}
