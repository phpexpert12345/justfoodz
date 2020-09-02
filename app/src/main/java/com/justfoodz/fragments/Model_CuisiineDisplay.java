package com.justfoodz.fragments;

import com.justfoodz.models.CuisineModel;

import java.util.ArrayList;

public class Model_CuisiineDisplay {

    public ArrayList<CuisineModel> getQuickSearchList() {
        return QuickSearchList;
    }

    public void setQuickSearchList(ArrayList<CuisineModel> quickSearchList) {
        QuickSearchList = quickSearchList;
    }

    public ArrayList<CuisineModel>QuickSearchList;
}
