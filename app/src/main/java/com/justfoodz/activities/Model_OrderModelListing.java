package com.justfoodz.activities;

import android.view.View;

import org.json.JSONObject;

public class Model_OrderModelListing {

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(String success_msg) {
        this.success_msg = success_msg;
    }



    public String success;
    public String success_msg;

    public Model_orderDetailsLsiting getOrders() {
        return orders;
    }

    public void setOrders(Model_orderDetailsLsiting orders) {
        this.orders = orders;
    }

    public Model_orderDetailsLsiting orders;



}
