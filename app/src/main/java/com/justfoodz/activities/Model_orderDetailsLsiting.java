package com.justfoodz.activities;

import com.justfoodz.models.OrderModel;

import java.util.ArrayList;

public class Model_orderDetailsLsiting {

    public ArrayList<OrderModel> getOrderViewResult() {
        return OrderViewResult;
    }

    public void setOrderViewResult(ArrayList<OrderModel> orderViewResult) {
        OrderViewResult = orderViewResult;
    }

    public ArrayList<OrderModel>OrderViewResult;
}
