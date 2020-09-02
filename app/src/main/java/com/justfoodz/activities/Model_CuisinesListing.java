package com.justfoodz.activities;

import com.justfoodz.models.CuisinesModel;

import java.util.ArrayList;

public class Model_CuisinesListing {
    public ArrayList<CuisinesModel> getCuisineList() {
        return CuisineList;
    }

    public void setCuisineList(ArrayList<CuisinesModel> cuisineList) {
        CuisineList = cuisineList;
    }

    public ArrayList<CuisinesModel>CuisineList;
}
