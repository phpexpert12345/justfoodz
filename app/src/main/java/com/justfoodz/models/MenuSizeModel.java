package com.justfoodz.models;

import java.io.Serializable;

public class MenuSizeModel implements Serializable {
    int id;
    String FoodItemName,menu, RestaurantPizzaItemName, RestaurantPizzaItemOldPrice, RestaurantPizzaItemPrice;
    boolean isTrue;
    private int quantity = 1;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
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

    public String getFoodItemName() {
        return FoodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        FoodItemName = foodItemName;
    }

    public String getRestaurantPizzaItemName() {
        return RestaurantPizzaItemName;
    }

    public void setRestaurantPizzaItemName(String restaurantPizzaItemName) {
        RestaurantPizzaItemName = restaurantPizzaItemName;
    }

    public String getRestaurantPizzaItemOldPrice() {
        return RestaurantPizzaItemOldPrice;
    }

    public void setRestaurantPizzaItemOldPrice(String restaurantPizzaItemOldPrice) {
        RestaurantPizzaItemOldPrice = restaurantPizzaItemOldPrice;
    }

    public String getRestaurantPizzaItemPrice() {
        return RestaurantPizzaItemPrice;
    }

    public void setRestaurantPizzaItemPrice(String restaurantPizzaItemPrice) {
        RestaurantPizzaItemPrice = restaurantPizzaItemPrice;
    }
}
