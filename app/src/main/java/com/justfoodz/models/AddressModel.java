package com.justfoodz.models;

public class AddressModel {


    private String address, user_phone, zipcode, city_name, user_locality,
            customerState, customerCountry, company_street, GustUserLandlineAdress, GustUserFloor, company_streetNo, address_title
            ,customer_address_lat,customer_address_long,address_direction;
    private Integer id;

    boolean isTrue;

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getUser_locality() {
        return user_locality;
    }

    public void setUser_locality(String user_locality) {
        this.user_locality = user_locality;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCompany_street() {
        return company_street;
    }

    public void setCompany_street(String company_street) {
        this.company_street = company_street;
    }

    public String getGustUserLandlineAdress() {
        return GustUserLandlineAdress;
    }

    public void setGustUserLandlineAdress(String gustUserLandlineAdress) {
        GustUserLandlineAdress = gustUserLandlineAdress;
    }

    public String getGustUserFloor() {
        return GustUserFloor;
    }

    public void setGustUserFloor(String gustUserFloor) {
        GustUserFloor = gustUserFloor;
    }

    public String getCompany_streetNo() {
        return company_streetNo;
    }

    public void setCompany_streetNo(String company_streetNo) {
        this.company_streetNo = company_streetNo;
    }

    public String getAddress_title() {
        return address_title;
    }

    public void setAddress_title(String address_title) {
        this.address_title = address_title;
    }

    public String getcustomer_address_lat() {
        return customer_address_lat;
    }

    public void setcustomer_address_lat(String customer_address_lat) {
        this.customer_address_lat = customer_address_lat;
    }

    public String getcustomer_address_long() {
        return customer_address_long;
    }

    public void setcustomer_address_long(String customer_address_long) {
        this.customer_address_long = customer_address_long;
    }


    public String getaddress_direction() {
        return address_direction;
    }

    public void setaddress_direction(String address_direction) {
        this.address_direction = address_direction;
    }
}
