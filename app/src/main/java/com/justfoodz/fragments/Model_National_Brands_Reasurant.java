package com.justfoodz.fragments;


import com.justfoodz.models.RestaurantNatonalBrandsModel;

import java.util.ArrayList;

public class Model_National_Brands_Reasurant {


    public ArrayList<RestaurantNatonalBrandsModel> getQuickSearchList() {
        return QuickSearchList;
    }

    public void setQuickSearchList(ArrayList<RestaurantNatonalBrandsModel> quickSearchList) {
        QuickSearchList = quickSearchList;
    }

    public ArrayList<RestaurantNatonalBrandsModel> QuickSearchList;
}
