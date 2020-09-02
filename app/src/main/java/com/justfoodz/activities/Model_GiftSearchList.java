package com.justfoodz.activities;

import com.justfoodz.models.BuyGiftModel;

import java.util.ArrayList;

public class Model_GiftSearchList {

    public ArrayList<BuyGiftModel> getGiftSearchList() {
        return GiftSearchList;
    }

    public void setGiftSearchList(ArrayList<BuyGiftModel> giftSearchList) {
        GiftSearchList = giftSearchList;
    }

    public ArrayList<BuyGiftModel>GiftSearchList;
}
