package com.justfoodz.models;

public class ReviewModel {

    int id,review_id;
    String deliveryrating,review_img,friendlinessrating,foodqualityrating,restaurant_name,customerName,RestaurantReviewRating,reviewpostedDate
            ,customerReviewComment;

    public ReviewModel(int id, int review_id, String deliveryrating, String review_img, String friendlinessrating, String foodqualityrating, String restaurant_name, String customerName, String restaurantReviewRating, String reviewpostedDate, String customerReviewComment) {
        this.id = id;
        this.review_id = review_id;
        this.deliveryrating = deliveryrating;
        this.review_img = review_img;
        this.friendlinessrating = friendlinessrating;
        this.foodqualityrating = foodqualityrating;
        this.restaurant_name = restaurant_name;
        this.customerName = customerName;
        RestaurantReviewRating = restaurantReviewRating;
        this.reviewpostedDate = reviewpostedDate;
        this.customerReviewComment = customerReviewComment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public String getDeliveryrating() {
        return deliveryrating;
    }

    public void setDeliveryrating(String deliveryrating) {
        this.deliveryrating = deliveryrating;
    }

    public String getReview_img() {
        return review_img;
    }

    public void setReview_img(String review_img) {
        this.review_img = review_img;
    }

    public String getFriendlinessrating() {
        return friendlinessrating;
    }

    public void setFriendlinessrating(String friendlinessrating) {
        this.friendlinessrating = friendlinessrating;
    }

    public String getFoodqualityrating() {
        return foodqualityrating;
    }

    public void setFoodqualityrating(String foodqualityrating) {
        this.foodqualityrating = foodqualityrating;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRestaurantReviewRating() {
        return RestaurantReviewRating;
    }

    public void setRestaurantReviewRating(String restaurantReviewRating) {
        RestaurantReviewRating = restaurantReviewRating;
    }

    public String getReviewpostedDate() {
        return reviewpostedDate;
    }

    public void setReviewpostedDate(String reviewpostedDate) {
        this.reviewpostedDate = reviewpostedDate;
    }

    public String getCustomerReviewComment() {
        return customerReviewComment;
    }

    public void setCustomerReviewComment(String customerReviewComment) {
        this.customerReviewComment = customerReviewComment;
    }

}
