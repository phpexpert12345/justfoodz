package com.justfoodz.activities;

import android.view.View;

import org.json.JSONObject;

public class Model_GiftRedeamList {

    public String success;

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }

    public String success_msg;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTotal_available_amount() {
        return total_available_amount;
    }

    public void setTotal_available_amount(String total_available_amount) {
        this.total_available_amount = total_available_amount;
    }



    public String total_available_amount;

    public Model_giftcards getGiftcards() {
        return giftcards;
    }

    public void setGiftcards(Model_giftcards giftcards) {
        this.giftcards = giftcards;
    }

    public Model_giftcards giftcards;


}
