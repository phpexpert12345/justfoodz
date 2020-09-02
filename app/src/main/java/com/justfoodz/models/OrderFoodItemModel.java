package com.justfoodz.models;

public class OrderFoodItemModel {

    private String itemsName;
    private Integer quantity;

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMenuprice() {
        return menuprice;
    }

    public void setMenuprice(String menuprice) {
        this.menuprice = menuprice;
    }

    private String menuprice;

    public String getExtraTopping() {
        return ExtraTopping;
    }

    public void setExtraTopping(String extraTopping) {
        ExtraTopping = extraTopping;
    }

    private String ExtraTopping;
}
