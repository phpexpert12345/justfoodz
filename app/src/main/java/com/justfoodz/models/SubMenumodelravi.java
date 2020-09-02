package com.justfoodz.models;

public class SubMenumodelravi {

    String id, error, RestaurantPizzaID, RestaurantCategoryID, RestaurantPizzaItemName,
            extraavailable, RestaurantPizzaItemOldPrice, RestaurantPizzaItemPrice,
            RestaurantPizzaItemSizeName, sizeavailable, ResPizzaDescription, Food_NameNo, food_Icon;

    public SubMenumodelravi(String id, String error, String RestaurantPizzaID, String RestaurantCategoryID, String RestaurantPizzaItemName,
                            String extraavailable, String RestaurantPizzaItemOldPrice, String RestaurantPizzaItemPrice,
                            String RestaurantPizzaItemSizeName, String sizeavailable, String ResPizzaDescription, String Food_NameNo, String Food_TypeImage) {
        this.id = id;
        this.error = error;
        this.RestaurantPizzaID = RestaurantPizzaID;
        this.RestaurantCategoryID = RestaurantCategoryID;
        this.RestaurantPizzaItemName = RestaurantPizzaItemName;
        this.extraavailable = extraavailable;
        this.RestaurantPizzaItemOldPrice = RestaurantPizzaItemOldPrice;
        this.RestaurantPizzaItemPrice = RestaurantPizzaItemPrice;
        this.RestaurantPizzaItemSizeName = RestaurantPizzaItemSizeName;
        this.sizeavailable = sizeavailable;
        this.ResPizzaDescription = ResPizzaDescription;
        this.Food_NameNo = Food_NameNo;
        this.food_Icon = Food_TypeImage;
    }

    public String getFood_TypeImage() {
        return food_Icon;
    }

    public void setFood_TypeImage(String food_TypeImage) {
        food_Icon = food_TypeImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRestaurantPizzaID() {
        return RestaurantPizzaID;
    }

    public void setRestaurantPizzaID(String restaurantPizzaID) {
        RestaurantPizzaID = restaurantPizzaID;
    }

    public String getRestaurantCategoryID() {
        return RestaurantCategoryID;
    }

    public void setRestaurantCategoryID(String restaurantCategoryID) {
        RestaurantCategoryID = restaurantCategoryID;
    }

    public String getRestaurantPizzaItemName() {
        return RestaurantPizzaItemName;
    }

    public void setRestaurantPizzaItemName(String restaurantPizzaItemName) {
        RestaurantPizzaItemName = restaurantPizzaItemName;
    }

    public String getExtraavailable() {
        return extraavailable;
    }

    public void setExtraavailable(String extraavailable) {
        this.extraavailable = extraavailable;
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

    public String getRestaurantPizzaItemSizeName() {
        return RestaurantPizzaItemSizeName;
    }

    public void setRestaurantPizzaItemSizeName(String restaurantPizzaItemSizeName) {
        RestaurantPizzaItemSizeName = restaurantPizzaItemSizeName;
    }

    public String getSizeavailable() {
        return sizeavailable;
    }

    public void setSizeavailable(String sizeavailable) {
        this.sizeavailable = sizeavailable;
    }

    public String getResPizzaDescription() {
        return ResPizzaDescription;
    }

    public void setResPizzaDescription(String resPizzaDescription) {
        ResPizzaDescription = resPizzaDescription;
    }

    public String getFood_NameNo() {
        return Food_NameNo;
    }

    public void setFood_NameNo(String food_NameNo) {
        Food_NameNo = food_NameNo;
    }


}
