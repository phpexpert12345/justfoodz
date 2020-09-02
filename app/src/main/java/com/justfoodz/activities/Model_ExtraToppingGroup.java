package com.justfoodz.activities;

import org.json.JSONArray;

import java.util.ArrayList;

public class Model_ExtraToppingGroup {
    public String id;
    public String ToppingGroupID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToppingGroupID() {
        return ToppingGroupID;
    }

    public void setToppingGroupID(String toppingGroupID) {
        ToppingGroupID = toppingGroupID;
    }

    public String getFood_addonsgroup() {
        return Food_addonsgroup;
    }

    public void setFood_addonsgroup(String food_addonsgroup) {
        Food_addonsgroup = food_addonsgroup;
    }

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

    public ArrayList<Model_subItemsRecord> getSubItemsRecord() {
        return subItemsRecord;
    }

    public void setSubItemsRecord(ArrayList<Model_subItemsRecord> subItemsRecord) {
        this.subItemsRecord = subItemsRecord;
    }

    public String Food_addonsgroup;
    public String success;
    public String success_msg;
    public ArrayList<Model_subItemsRecord> subItemsRecord;




}
