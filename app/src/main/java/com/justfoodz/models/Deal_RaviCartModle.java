package com.justfoodz.models;

public class Deal_RaviCartModle {
    String item_idd,item_name,price,item_quantity;

    public Deal_RaviCartModle(String item_idd ,String item_name,String price,String item_quantity)
    {
        this.item_idd = item_idd;
        this.item_name = item_name;
        this.price = price;
        this.item_quantity = item_quantity;
    }

    public String getItem_id() {
        return item_idd;
    }

    public void setItem_id(String item_id) {
        this.item_idd = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

}
