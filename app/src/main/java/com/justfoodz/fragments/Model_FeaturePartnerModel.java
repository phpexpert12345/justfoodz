package com.justfoodz.fragments;

import com.justfoodz.models.FeaturedPartnerModel;

import java.util.ArrayList;

public class Model_FeaturePartnerModel {

    public ArrayList<FeaturedPartnerModel> getQuickSearchList() {
        return QuickSearchList;
    }

    public void setQuickSearchList(ArrayList<FeaturedPartnerModel> quickSearchList) {
        QuickSearchList = quickSearchList;
    }

    public ArrayList<FeaturedPartnerModel>QuickSearchList;
}
