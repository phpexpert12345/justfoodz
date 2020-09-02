package com.justfoodz.activities;

import com.justfoodz.models.FeatureAminitiesModel;

import java.util.ArrayList;

public class Model_AmenitiesSearchList {
    public ArrayList<FeatureAminitiesModel> getAmenitiesSearch() {
        return AmenitiesSearchList;
    }

    public void setAmenitiesSearch(ArrayList<FeatureAminitiesModel> amenitiesSearch) {
        AmenitiesSearchList = amenitiesSearch;
    }

    public ArrayList<FeatureAminitiesModel> AmenitiesSearchList;
}
