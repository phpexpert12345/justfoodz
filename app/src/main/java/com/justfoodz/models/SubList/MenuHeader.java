package com.justfoodz.models.SubList;

import org.json.JSONArray;

import java.util.ArrayList;

public class MenuHeader {

    String id,ToppingGroupID,Food_addonsgroup,success,success_msg;
    ArrayList<ChildHeader> childHeaders;
    JSONArray jsonArray;

    public MenuHeader(String id,String ToppingGroupID,String Food_addonsgroup,String success,String success_msg,JSONArray jsonArray)
    {
        this.id = id;
        this.ToppingGroupID = ToppingGroupID;
        this.Food_addonsgroup = Food_addonsgroup;
        this.success = success;
        this.success_msg = success_msg;
        this.jsonArray = jsonArray;
    }


    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }





    public ArrayList<ChildHeader> getChildHeaders() {
        return childHeaders;
    }

    public void setChildHeaders(ArrayList<ChildHeader> childHeaders) {
        this.childHeaders = childHeaders;
    }

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

}
