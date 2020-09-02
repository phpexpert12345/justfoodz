package com.justfoodz.activities;

import com.justfoodz.models.Citymodel;

import java.util.ArrayList;

public class Model_CityListing {

    public ArrayList<Citymodel> getCityList() {
        return CityList;
    }

    public void setCityList(ArrayList<Citymodel> cityList) {
        CityList = cityList;
    }

    public ArrayList<Citymodel>CityList;
}
