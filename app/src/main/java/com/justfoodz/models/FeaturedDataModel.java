package com.justfoodz.models;

public class FeaturedDataModel {

    public String id,Cuisinename, Cuisineimage,restra_address,ratingValue;

    public FeaturedDataModel(String id, String Cuisinename,String Cuisineimage,String restra_address,String ratingValue) {
        this.Cuisinename=Cuisinename;
        this.Cuisineimage=Cuisineimage;
        this.restra_address=restra_address;
        this.ratingValue=ratingValue;
        this.id=id;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRestra_address() {
        return restra_address;
    }

    public void setRestra_address(String restra_address) {
        this.restra_address = restra_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuisinename() {
        return Cuisinename;
    }

    public void setCuisinename(String cuisinename) {
        Cuisinename = cuisinename;
    }

    public String getCuisineimage() {
        return Cuisineimage;
    }

    public void setCuisineimage(String cuisineimage) {
        Cuisineimage = cuisineimage;
    }
}
