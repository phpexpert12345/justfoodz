package com.justfoodz.activities;

public class Model_PaymentModel {

    public String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getOrder_identifyno() {
        return order_identifyno;
    }

    public void setOrder_identifyno(String order_identifyno) {
        this.order_identifyno = order_identifyno;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String order_identifyno;
    public String error_msg;
}
