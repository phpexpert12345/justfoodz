package com.justfoodz.activities;

public class Model_CityDetail {
    public String id;
    public String city_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getSeo_url_call() {
        return seo_url_call;
    }

    public void setSeo_url_call(String seo_url_call) {
        this.seo_url_call = seo_url_call;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String seo_url_call;
    public String where;
    public String error;

}
