package com.justfoodz.models;

import java.io.Serializable;

public class ExtrasModel implements Serializable {
    int id;
    String Food_addonsselection,extra,Food_Addons,Food_PriceAddons;
    boolean isTrue;

    private int quantity = 1;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
