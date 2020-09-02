package com.justfoodz.models;

public class FeaturedPartnerModel {
     public String id,servicename, serviceimage;

        public FeaturedPartnerModel(String id, String servicename,String serviceimage) {
        this.servicename=servicename;
        this.serviceimage=serviceimage;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServiceimage() {
        return serviceimage;
    }

    public void setServiceimage(String serviceimage) {
        this.serviceimage = serviceimage;
    }


    public String getRestaurantServiceName() {
        return RestaurantServiceName;
    }

    public void setRestaurantServiceName(String restaurantServiceName) {
        RestaurantServiceName = restaurantServiceName;
    }

    public String getService_img() {
        return service_img;
    }

    public void setService_img(String service_img) {
        this.service_img = service_img;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String RestaurantServiceName;
    public String service_img;
    public String error;
}
