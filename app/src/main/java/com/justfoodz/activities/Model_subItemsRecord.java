package com.justfoodz.activities;

import android.text.Html;

public class Model_subItemsRecord {

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodItemID() {
        return FoodItemID;
    }

    public void setFoodItemID(String foodItemID) {
        FoodItemID = foodItemID;
    }

    public String getFoodItemSizeID() {
        return FoodItemSizeID;
    }

    public void setFoodItemSizeID(String foodItemSizeID) {
        FoodItemSizeID = foodItemSizeID;
    }

    public String getFood_addonsselection() {
        return Food_addonsselection;
    }

    public void setFood_addonsselection(String food_addonsselection) {
        Food_addonsselection = food_addonsselection;
    }

    public String getFood_Addons() {
        return Food_Addons;
    }

    public void setFood_Addons(String food_Addons) {
        Food_Addons = food_Addons;
    }

    public String getFood_PriceAddons() {
        return Food_PriceAddons;
    }

    public void setFood_PriceAddons(String food_PriceAddons) {
        Food_PriceAddons = food_PriceAddons;
    }

    public String FoodItemID;
    public String FoodItemSizeID;
    public String Food_addonsselection;
    public String Food_Addons;
    public String Food_PriceAddons;

}
