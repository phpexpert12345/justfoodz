package com.justfoodz.models;

import java.io.Serializable;

public class RestaurantModel  implements Serializable{



    String error_msg;
    String id;
    String restaurant_name;
    String restaurantsupervisory;
    String restaurant_address;
    String restaurant_cuisine;
    String restaurant_discount_covered;
    String homeDeliveryStatus;
    String PickupStatus;
    String Pre_homeDeliveryStatus;
    String Pre_Order_PickupStatus;
    String restaurant_phone;
    String restaurant_fax;
    String restaurant_contact_name;
    String restaurant_contact_phone;
    String restaurant_contact_mobile;
    String restaurant_contact_email;
    String restaurant_sms_mobile;
    String restaurant_OrderEmail;
    String restaurant_onlycashonAvailable;
    String restaurant_onlycashon;
    String restaurant_cardacceptAvailable;
    String restaurant_cardaccept;
    String restaurant_serviceFees;
    String restaurant_saleTaxPercentage;
    String restaurant_serviceVat;
    String BookaTablesupport;
    String monday;
    String tuesday;
    String wednesday;
    String thursday;
    String friday;
    String saturday;
    String sunday;
    String displayTime;
    String restaurant_LatitudePoint;
    String restaurant_LongitudePoint;
    String restaurantOffer;
    String restaurantOfferIcon;
    String RestaurantOfferPrice;
    String DiscountType;
    String RestaurantOfferDescription;
    String TotalRestaurantReview;
    String RestaurantTimeStatus;
    String RestaurantTimeStatus1;
    String restaurant_deliverycharge;
    String restaurant_minimumorder;
    String restaurant_avarage_deliveryTime;
    String restaurant_avarage_PickupTime;
    String restaurantCity;
    String ratingAvg;
    String restaurant_locality;
    String restaurant_Paypal_URL;
    String restaurant_Paypal_bussiness_act;
    String rating;
    String ratingValue;
    String restaurant_about;
    String featured;
    String restaurant_deliveryDistance;
    String restaurant_Logo;
    String error;
    String bookatable;
    String kidfriendly;
    String petfriendly;
    String dinein;
    String restaurantnotes;
    String collection;
    String delievery;
    String shield_available_message;
    String SocialSharingMessage;
    String shield_available_url;
    String deal_name;
    String deal_avilable_days;
    String deal_price;
    String deal_discount_amount;
    String start_hours;
    String start_mins;
    String end_hours;
    String end_mins;
    String deal_description;
    String restaurant_id;
    String restaurant_pickup_time;
    String restaurant_delivery_time;
    String restaurant_img;
    String deal_image;
    String deal_price_add;
    String Mealid;
    String restaurant_distance_check;
    String mealDealAvailable;
    String RestaurantTimeStatusText;


    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getDineInAvailable() {
        return DineInAvailable;
    }

    public void setDineInAvailable(String dineInAvailable) {
        DineInAvailable = dineInAvailable;
    }

    String DineInAvailable;


    public String getSocialSharingMessage() {
        return SocialSharingMessage;
    }

    public void setSocialSharingMessage(String socialSharingMessage) {
        SocialSharingMessage = socialSharingMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookaTable() {
        return bookatable;
    }
    public void setBookaTable(String bookatable) {
        this.bookatable = bookatable;
    }
    public String getKidFriendly() {
        return kidfriendly;
    }
    public void setKidFriendly(String kidfriendly) {
        this.kidfriendly = kidfriendly;
    }
    public String getPetFriendly() {
        return petfriendly;
    }
    public void setPetFriendly(String petfriendly) {
        this.petfriendly = petfriendly;
    }

    public String getDINEin() {
        return dinein;
    }
    public void setDINEin(String dinein) {
        this.dinein = dinein;
    }
    public String getCollection() {
        return collection;
    }
    public void setCollection(String collection) {
        this.collection = collection;
    }
    public String getDelivery() {
        return delievery;
    }
    public void setDelivery(String delievery) {
        this.delievery = delievery;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurantsupervisory() {
        return restaurantsupervisory;
    }

    public void setRestaurantsupervisory(String restaurantsupervisory) {
        this.restaurantsupervisory = restaurantsupervisory;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getRestaurant_cuisine() {
        return restaurant_cuisine;
    }

    public void setRestaurant_cuisine(String restaurant_cuisine) {
        this.restaurant_cuisine = restaurant_cuisine;
    }

    public String getRestaurant_discount_covered() {
        return restaurant_discount_covered;
    }

    public void setRestaurant_discount_covered(String restaurant_discount_covered) {
        this.restaurant_discount_covered = restaurant_discount_covered;
    }

    public String getHomeDeliveryStatus() {
        return homeDeliveryStatus;
    }

    public void setHomeDeliveryStatus(String homeDeliveryStatus) {
        this.homeDeliveryStatus = homeDeliveryStatus;
    }

    public String getPickupStatus() {
        return PickupStatus;
    }

    public void setPickupStatus(String pickupStatus) {
        PickupStatus = pickupStatus;
    }

    public String getPre_homeDeliveryStatus() {
        return Pre_homeDeliveryStatus;
    }

    public void setPre_homeDeliveryStatus(String pre_homeDeliveryStatus) {
        Pre_homeDeliveryStatus = pre_homeDeliveryStatus;
    }

    public String getPre_Order_PickupStatus() {
        return Pre_Order_PickupStatus;
    }

    public void setPre_Order_PickupStatus(String pre_Order_PickupStatus) {
        Pre_Order_PickupStatus = pre_Order_PickupStatus;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getRestaurant_fax() {
        return restaurant_fax;
    }

    public void setRestaurant_fax(String restaurant_fax) {
        this.restaurant_fax = restaurant_fax;
    }

    public String getRestaurant_contact_name() {
        return restaurant_contact_name;
    }

    public void setRestaurant_contact_name(String restaurant_contact_name) {
        this.restaurant_contact_name = restaurant_contact_name;
    }

    public String getRestaurant_contact_phone() {
        return restaurant_contact_phone;
    }

    public void setRestaurant_contact_phone(String restaurant_contact_phone) {
        this.restaurant_contact_phone = restaurant_contact_phone;
    }

    public String getRestaurant_contact_mobile() {
        return restaurant_contact_mobile;
    }

    public void setRestaurant_contact_mobile(String restaurant_contact_mobile) {
        this.restaurant_contact_mobile = restaurant_contact_mobile;
    }

    public String getRestaurant_contact_email() {
        return restaurant_contact_email;
    }

    public void setRestaurant_contact_email(String restaurant_contact_email) {
        this.restaurant_contact_email = restaurant_contact_email;
    }

    public String getRestaurant_sms_mobile() {
        return restaurant_sms_mobile;
    }

    public void setRestaurant_sms_mobile(String restaurant_sms_mobile) {
        this.restaurant_sms_mobile = restaurant_sms_mobile;
    }

    public String getRestaurant_OrderEmail() {
        return restaurant_OrderEmail;
    }

    public void setRestaurant_OrderEmail(String restaurant_OrderEmail) {
        this.restaurant_OrderEmail = restaurant_OrderEmail;
    }

    public String getRestaurant_onlycashonAvailable() {
        return restaurant_onlycashonAvailable;
    }

    public void setRestaurant_onlycashonAvailable(String restaurant_onlycashonAvailable) {
        this.restaurant_onlycashonAvailable = restaurant_onlycashonAvailable;
    }

    public String getRestaurant_onlycashon() {
        return restaurant_onlycashon;
    }

    public void setRestaurant_onlycashon(String restaurant_onlycashon) {
        this.restaurant_onlycashon = restaurant_onlycashon;
    }

    public String getRestaurant_cardacceptAvailable() {
        return restaurant_cardacceptAvailable;
    }

    public void setRestaurant_cardacceptAvailable(String restaurant_cardacceptAvailable) {
        this.restaurant_cardacceptAvailable = restaurant_cardacceptAvailable;
    }

    public String getRestaurant_cardaccept() {
        return restaurant_cardaccept;
    }

    public void setRestaurant_cardaccept(String restaurant_cardaccept) {
        this.restaurant_cardaccept = restaurant_cardaccept;
    }

    public String getRestaurant_serviceFees() {
        return restaurant_serviceFees;
    }

    public void setRestaurant_serviceFees(String restaurant_serviceFees) {
        this.restaurant_serviceFees = restaurant_serviceFees;
    }

    public String getRestaurant_saleTaxPercentage() {
        return restaurant_saleTaxPercentage;
    }

    public void setRestaurant_saleTaxPercentage(String restaurant_saleTaxPercentage) {
        this.restaurant_saleTaxPercentage = restaurant_saleTaxPercentage;
    }

    public String getRestaurant_serviceVat() {
        return restaurant_serviceVat;
    }

    public void setRestaurant_serviceVat(String restaurant_serviceVat) {
        this.restaurant_serviceVat = restaurant_serviceVat;
    }

    public String getBookaTablesupport() {
        return BookaTablesupport;
    }

    public void setBookaTablesupport(String bookaTablesupport) {
        BookaTablesupport = bookaTablesupport;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getRestaurant_LatitudePoint() {
        return restaurant_LatitudePoint;
    }

    public void setRestaurant_LatitudePoint(String restaurant_LatitudePoint) {
        this.restaurant_LatitudePoint = restaurant_LatitudePoint;
    }

    public String getRestaurant_LongitudePoint() {
        return restaurant_LongitudePoint;
    }

    public void setRestaurant_LongitudePoint(String restaurant_LongitudePoint) {
        this.restaurant_LongitudePoint = restaurant_LongitudePoint;
    }

    public String getRestaurantOffer() {
        return restaurantOffer;
    }

    public void setRestaurantOffer(String restaurantOffer) {
        this.restaurantOffer = restaurantOffer;
    }

    public String getRestaurantOfferIcon() {
        return restaurantOfferIcon;
    }

    public void setRestaurantOfferIcon(String restaurantOfferIcon) {
        this.restaurantOfferIcon = restaurantOfferIcon;
    }

    public String getRestaurantOfferPrice() {
        return RestaurantOfferPrice;
    }

    public void setRestaurantOfferPrice(String restaurantOfferPrice) {
        RestaurantOfferPrice = restaurantOfferPrice;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getRestaurantOfferDescription() {
        return RestaurantOfferDescription;
    }

    public void setRestaurantOfferDescription(String restaurantOfferDescription) {
        RestaurantOfferDescription = restaurantOfferDescription;
    }

    public String getTotalRestaurantReview() {
        return TotalRestaurantReview;
    }

    public void setTotalRestaurantReview(String totalRestaurantReview) {
        TotalRestaurantReview = totalRestaurantReview;
    }

    public String getRestaurantTimeStatus() {
        return RestaurantTimeStatus;
    }

    public void setRestaurantTimeStatus(String restaurantTimeStatus) {
        RestaurantTimeStatus = restaurantTimeStatus;
    }

    public String getRestaurantTimeStatus1() {
        return RestaurantTimeStatus1;
    }

    public void setRestaurantTimeStatus1(String restaurantTimeStatus1) {
        RestaurantTimeStatus1 = restaurantTimeStatus1;
    }

    public String getRestaurant_deliverycharge() {
        return restaurant_deliverycharge;
    }

    public void setRestaurant_deliverycharge(String restaurant_deliverycharge) {
        this.restaurant_deliverycharge = restaurant_deliverycharge;
    }

    public String getRestaurant_minimumorder() {
        return restaurant_minimumorder;
    }

    public void setRestaurant_minimumorder(String restaurant_minimumorder) {
        this.restaurant_minimumorder = restaurant_minimumorder;
    }

    public String getRestaurant_avarage_deliveryTime() {
        return restaurant_avarage_deliveryTime;
    }

    public void setRestaurant_avarage_deliveryTime(String restaurant_avarage_deliveryTime) {
        this.restaurant_avarage_deliveryTime = restaurant_avarage_deliveryTime;
    }

    public String getRestaurant_avarage_PickupTime() {
        return restaurant_avarage_PickupTime;
    }

    public void setRestaurant_avarage_PickupTime(String restaurant_avarage_PickupTime) {
        this.restaurant_avarage_PickupTime = restaurant_avarage_PickupTime;
    }

    public String getRestaurantCity() {
        return restaurantCity;
    }

    public void setRestaurantCity(String restaurantCity) {
        this.restaurantCity = restaurantCity;
    }

    public String getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(String ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public String getRestaurant_locality() {
        return restaurant_locality;
    }

    public void setRestaurant_locality(String restaurant_locality) {
        this.restaurant_locality = restaurant_locality;
    }

    public String getRestaurant_Paypal_URL() {
        return restaurant_Paypal_URL;
    }

    public void setRestaurant_Paypal_URL(String restaurant_Paypal_URL) {
        this.restaurant_Paypal_URL = restaurant_Paypal_URL;
    }

    public String getRestaurant_Paypal_bussiness_act() {
        return restaurant_Paypal_bussiness_act;
    }

    public void setRestaurant_Paypal_bussiness_act(String restaurant_Paypal_bussiness_act) {
        this.restaurant_Paypal_bussiness_act = restaurant_Paypal_bussiness_act;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRestaurant_about() {
        return restaurant_about;
    }

    public void setRestaurant_about(String restaurant_about) {
        this.restaurant_about = restaurant_about;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getRestaurant_deliveryDistance() {
        return restaurant_deliveryDistance;
    }

    public void setRestaurant_deliveryDistance(String restaurant_deliveryDistance) {
        this.restaurant_deliveryDistance = restaurant_deliveryDistance;
    }

    public String getRestaurant_Logo() {
        return restaurant_Logo;
    }

    public void setRestaurant_Logo(String restaurant_Logo) {
        this.restaurant_Logo = restaurant_Logo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getshield_available_url() {
        return shield_available_url;
    }

    public void setshield_available_url(String shield_available_url) {
        this.shield_available_url = shield_available_url;
    }

    public String getshield_available_message() {
        return shield_available_message;
    }

    public void setshield_available_message(String shield_available_message) {
        this.shield_available_message = shield_available_message;
    }

    public String getrestaurantnotes() {
        return restaurantnotes;
    }

    public void setrestaurantnotes(String restaurantnotes) {
        this.restaurantnotes = restaurantnotes;
    }
    public String getDeal_name() {
        return deal_name;
    }

    public void setDeal_name(String deal_name) {
        this.deal_name = deal_name;
    }

    public String getDeal_avilable_days() {
        return deal_avilable_days;
    }

    public void setDeal_avilable_days(String deal_avilable_days) {
        this.deal_avilable_days = deal_avilable_days;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getDeal_discount_amount() {
        return deal_discount_amount;
    }

    public void setDeal_discount_amount(String deal_discount_amount) {
        this.deal_discount_amount = deal_discount_amount;
    }

    public String getStart_hours() {
        return start_hours;
    }

    public void setStart_hours(String start_hours) {
        this.start_hours = start_hours;
    }

    public String getStart_mins() {
        return start_mins;
    }

    public void setStart_mins(String start_mins) {
        this.start_mins = start_mins;
    }

    public String getEnd_hours() {
        return end_hours;
    }

    public void setEnd_hours(String end_hours) {
        this.end_hours = end_hours;
    }

    public String getEnd_mins() {
        return end_mins;
    }

    public void setEnd_mins(String end_mins) {
        this.end_mins = end_mins;
    }

    public String getDeal_description() {
        return deal_description;
    }

    public void setDeal_description(String deal_description) {
        this.deal_description = deal_description;
    }



    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getRestaurant_pickup_time() {
        return restaurant_pickup_time;
    }

    public void setRestaurant_pickup_time(String restaurant_pickup_time) {
        this.restaurant_pickup_time = restaurant_pickup_time;
    }

    public String getRestaurant_delivery_time() {
        return restaurant_delivery_time;
    }

    public void setRestaurant_delivery_time(String restaurant_delivery_time) {
        this.restaurant_delivery_time = restaurant_delivery_time;
    }

    public String getRestaurant_img() {
        return restaurant_img;
    }

    public void setRestaurant_img(String restaurant_img) {
        this.restaurant_img = restaurant_img;
    }

    public String getDeal_image() {
        return deal_image;
    }

    public void setDeal_image(String deal_image) {
        this.deal_image = deal_image;
    }

    public String getDeal_price_toadd() {
        return deal_price_add;
    }

    public void setDeal_price_toadd(String deal_price_add) {
        this.deal_price_add=deal_price_add;
    }


    public String getMealid() {
        return Mealid;
    }

    public void setMealid(String Mealid) {
        this.Mealid=Mealid;
    }

    public String getrestaurant_distance_check() {
        return restaurant_distance_check;
    }

    public void setrestaurant_distance_check(String restaurant_distance_check) {
        this.restaurant_distance_check=restaurant_distance_check;
    }


    public String getmealDealAvailable() {
        return mealDealAvailable;
    }

    public void setmealDealAvailable(String mealDealAvailable) {
        this.mealDealAvailable=mealDealAvailable;
    }

    public String getRestaurantTimeStatusText() {
        return RestaurantTimeStatusText;
    }

    public void setRestaurantTimeStatusText(String RestaurantTimeStatusText) {
        this.RestaurantTimeStatusText = RestaurantTimeStatusText;
    }
}