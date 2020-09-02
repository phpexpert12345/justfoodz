package com.justfoodz.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    String order_identifyno, order_time, restaurant_id, ordPrice, order_type, payment_mode,
            restaurant_name, restaurant_address, order_display_message, order_date, DriverID, rider_otp, rider_review,
            RiderRating, rider_order_close, Favorites_display;

    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    public String getRiderOtp() {
        return rider_otp;
    }

    public void setRiderOtp(String RiderOtp) {
        this.rider_otp = RiderOtp;
    }

    public String getRiderReview() {
        return rider_review;
    }

    public void setRiderReview(String rider_review) {
        this.rider_review = rider_review;
    }


    public String getOrder_identifyno() {
        return order_identifyno;
    }

    public void setOrder_identifyno(String order_identifyno) {
        this.order_identifyno = order_identifyno;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getOrdPrice() {
        return ordPrice;
    }

    public void setOrdPrice(String ordPrice) {
        this.ordPrice = ordPrice;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getOrder_display_message() {
        return order_display_message;
    }

    public void setOrder_display_message(String order_display_message) {
        this.order_display_message = order_display_message;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }


    public String getRiderRating() {
        return RiderRating;
    }

    public void setRiderRating(String RiderRating) {
        this.RiderRating = RiderRating;
    }


    public String getrider_order_close() {
        return rider_order_close;
    }

    public void setrider_order_close(String rider_order_close) {
        this.rider_order_close = rider_order_close;
    }

    public String getFavorites_display() {
        return Favorites_display;
    }

    public void setFavorites_display(String Favorites_display) {
        this.Favorites_display = Favorites_display;
    }
}
