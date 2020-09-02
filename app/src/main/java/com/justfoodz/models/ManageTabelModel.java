package com.justfoodz.models;

public class ManageTabelModel {
    public String id, table_id, status, booking_date, booking_time, booking_id, noofgusts, table_booking_number, customer_booking_name, customer_booking_mobile,
            customer_booking_email, booking_instruction, restaurant_name, restaurant_address, restaurant_phone, restaurant_mobile,
            table_booking_status_message;

    public ManageTabelModel(String id, String table_id, String status, String booking_date, String booking_time, String booking_id, String noofgusts
            , String table_booking_number, String customer_booking_name, String customer_booking_mobile, String customer_booking_email,
                            String booking_instruction, String restaurant_name, String restaurant_address, String restaurant_phone
            , String restaurant_mobile, String table_booking_status_message) {

        this.id = id;
        this.table_id = table_id;
        this.status = status;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.booking_id = booking_id;
        this.noofgusts = noofgusts;
        this.table_booking_number = table_booking_number;
        this.customer_booking_name = customer_booking_name;
        this.customer_booking_mobile = customer_booking_mobile;
        this.customer_booking_email = customer_booking_email;
        this.booking_instruction = booking_instruction;
        this.restaurant_name = restaurant_name;
        this.restaurant_address = restaurant_address;
        this.restaurant_phone = restaurant_phone;
        this.restaurant_mobile = restaurant_mobile;
        this.table_booking_status_message = table_booking_status_message;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getNoofgusts() {
        return noofgusts;
    }

    public void setNoofgusts(String noofgusts) {
        this.noofgusts = noofgusts;
    }

    public String getTable_booking_number() {
        return table_booking_number;
    }

    public void setTable_booking_number(String table_booking_number) {
        this.table_booking_number = table_booking_number;
    }

    public String getCustomer_booking_name() {
        return customer_booking_name;
    }

    public void setCustomer_booking_name(String customer_booking_name) {
        this.customer_booking_name = customer_booking_name;
    }

    public String getCustomer_booking_mobile() {
        return customer_booking_mobile;
    }

    public void setCustomer_booking_mobile(String customer_booking_mobile) {
        this.customer_booking_mobile = customer_booking_mobile;
    }

    public String getCustomer_booking_email() {
        return customer_booking_email;
    }

    public void setCustomer_booking_email(String customer_booking_email) {
        this.customer_booking_email = customer_booking_email;
    }

    public String getBooking_instruction() {
        return booking_instruction;
    }

    public void setBooking_instruction(String booking_instruction) {
        this.booking_instruction = booking_instruction;
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

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getRestaurant_mobile() {
        return restaurant_mobile;
    }

    public void setRestaurant_mobile(String restaurant_mobile) {
        this.restaurant_mobile = restaurant_mobile;
    }

    public String getTable_booking_status_message() {
        return table_booking_status_message;
    }

    public void setTable_booking_status_message(String table_booking_status_message) {
        this.table_booking_status_message = table_booking_status_message;
    }
}
