package com.justfoodz.fragments;

import java.util.ArrayList;

public class Model_AppFlashBannerList {

    public ArrayList<Model_BannerDetails> getFrontBannersList() {
        return FrontBannersList;
    }

    public void setFrontBannersList(ArrayList<Model_BannerDetails> frontBannersList) {
        FrontBannersList = frontBannersList;
    }

    public ArrayList<Model_BannerDetails>FrontBannersList;
}
