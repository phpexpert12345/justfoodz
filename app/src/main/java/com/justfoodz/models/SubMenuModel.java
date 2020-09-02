package com.justfoodz.models;

import java.io.Serializable;

public class SubMenuModel implements Serializable {
    private Integer id;

    private Integer Itemid;

    private String error;
    private Integer restaurantPizzaID;
    private String restaurantCategoryID;
    private String subMenuItemId;
    private String restaurantPizzaItemName;
    private String extraavailable;
    private String restaurantPizzaItemPrice;
    private String restaurantPizzaItemSizeName;
    private String sizeavailable;
    private String resPizzaDescription;
    private String foodNameNo;
    private String additivesDescription;
    private String dishIngredients;
    private String foodType;
    private String foodTypeNon;
    private String foodPopular;
    private String foodSpicy;
    private String midFoodSpicy;
    private String veryFoodSpicy;
    private String greenFoodSpicy;

    private int quantity = 1;
    private String menu="";

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemidId() {
        return Itemid;
    }

    public void setItemidId(Integer id) {
        this.Itemid = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getRestaurantPizzaID() {
        return restaurantPizzaID;
    }

    public void setRestaurantPizzaID(Integer restaurantPizzaID) {
        this.restaurantPizzaID = restaurantPizzaID;
    }

    public String getRestaurantCategoryID() {
        return restaurantCategoryID;
    }

    public void setRestaurantCategoryID(String restaurantCategoryID) {
        this.restaurantCategoryID = restaurantCategoryID;
    }

    public String getsubMenuItemId() {
        return subMenuItemId;
    }

    public void setsubMenuItemId(String restaurantCategoryID) {
        this.subMenuItemId = restaurantCategoryID;
    }


    @Override
    public int hashCode() {
        System.out.println("In ");
        int hashcode = 0;
        hashcode = getId().hashCode();
        System.out.println("In " + hashcode);
        return hashcode;
    }
    @Override
    public boolean equals(Object obj) {
        System.out.println("Hello");
        if (obj instanceof SubMenuModel) {
            SubMenuModel pp = (SubMenuModel) obj;
            boolean isTrue = (pp.getId().equals(getId()));
            if (isTrue){
                pp.setQuantity(pp.getQuantity()+1);
                Double price=Double.parseDouble(pp.getRestaurantPizzaItemPrice());
                price=price*pp.getQuantity();
                pp.setRestaurantPizzaItemPrice(""+price);
            }
            return (pp.getId().equals(getId()));
        } else {
            return false;
        }
    }

    public String getRestaurantPizzaItemName() {
        return restaurantPizzaItemName;
    }

    public void setRestaurantPizzaItemName(String restaurantPizzaItemName) {
        this.restaurantPizzaItemName = restaurantPizzaItemName;
    }

    public String getExtraavailable() {
        return extraavailable;
    }

    public void setExtraavailable(String extraavailable) {
        this.extraavailable = extraavailable;
    }

    public String getRestaurantPizzaItemPrice() {
        return restaurantPizzaItemPrice;
    }

    public void setRestaurantPizzaItemPrice(String restaurantPizzaItemPrice) {
        this.restaurantPizzaItemPrice = restaurantPizzaItemPrice;
    }

    public String getRestaurantPizzaItemSizeName() {
        return restaurantPizzaItemSizeName;
    }

    public void setRestaurantPizzaItemSizeName(String restaurantPizzaItemSizeName) {
        this.restaurantPizzaItemSizeName = restaurantPizzaItemSizeName;
    }

    public String getSizeavailable() {
        return sizeavailable;
    }

    public void setSizeavailable(String sizeavailable) {
        this.sizeavailable = sizeavailable;
    }

    public String getResPizzaDescription() {
        return resPizzaDescription;
    }

    public void setResPizzaDescription(String resPizzaDescription) {
        this.resPizzaDescription = resPizzaDescription;
    }

    public String getFoodNameNo() {
        return foodNameNo;
    }

    public void setFoodNameNo(String foodNameNo) {
        this.foodNameNo = foodNameNo;
    }

    public String getAdditivesDescription() {
        return additivesDescription;
    }

    public void setAdditivesDescription(String additivesDescription) {
        this.additivesDescription = additivesDescription;
    }

    public String getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(String dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodTypeNon() {
        return foodTypeNon;
    }

    public void setFoodTypeNon(String foodTypeNon) {
        this.foodTypeNon = foodTypeNon;
    }

    public String getFoodPopular() {
        return foodPopular;
    }

    public void setFoodPopular(String foodPopular) {
        this.foodPopular = foodPopular;
    }

    public String getFoodSpicy() {
        return foodSpicy;
    }

    public void setFoodSpicy(String foodSpicy) {
        this.foodSpicy = foodSpicy;
    }

    public String getMidFoodSpicy() {
        return midFoodSpicy;
    }

    public void setMidFoodSpicy(String midFoodSpicy) {
        this.midFoodSpicy = midFoodSpicy;
    }

    public String getVeryFoodSpicy() {
        return veryFoodSpicy;
    }

    public void setVeryFoodSpicy(String veryFoodSpicy) {
        this.veryFoodSpicy = veryFoodSpicy;
    }

    public String getGreenFoodSpicy() {
        return greenFoodSpicy;
    }

    public void setGreenFoodSpicy(String greenFoodSpicy) {
        this.greenFoodSpicy = greenFoodSpicy;
    }
}
