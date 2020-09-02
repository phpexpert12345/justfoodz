package com.justfoodz.utils;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class MyPref {


    Context context;
    SharedPreferences sharedPreferences;
    private String userId,mobile,bookid,firebaseTokenId,profileImage,referId,isFirstTime;
    private String userName,mail,latitude,longitude,city,state,BookingData,emergency,
    hrsPackData,pickupAdd,dropAdd,googleApikey,pickupCity,dropCity;


    public String getDropCity(){
        dropCity=sharedPreferences.getString("dropCity","");
        return dropCity;
    }
    public void setDropCity(String dropCity) {
        this.dropCity = dropCity;
        sharedPreferences.edit().putString("dropCity", dropCity).commit();
    }

    public String getPickupCity(){
        pickupCity=sharedPreferences.getString("pickupCity","");
        return pickupCity;
    }
    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
        sharedPreferences.edit().putString("pickupCity", pickupCity).commit();
    }

    public String getGoogleApikey(){
        googleApikey=sharedPreferences.getString("googleApikey","");
        return googleApikey;
    }
    public void setGoogleApikey(String googleApikey) {
        this.googleApikey = googleApikey;
        sharedPreferences.edit().putString("googleApikey", googleApikey).commit();
    }

    public String getDropAdd(){
        dropAdd=sharedPreferences.getString("dropAdd","");
        return dropAdd;
    }
    public void setDropAdd(String dropAdd) {
        this.dropAdd = dropAdd;
        sharedPreferences.edit().putString("dropAdd", dropAdd).commit();
    }
    public String getPickupAdd(){
        pickupAdd=sharedPreferences.getString("pickupAdd","");
        return pickupAdd;
    }
    public void setPickupAdd(String pickupAdd) {
        this.pickupAdd = pickupAdd;
        sharedPreferences.edit().putString("pickupAdd", pickupAdd).commit();
    }
    public String getHrsPackData(){
        hrsPackData=sharedPreferences.getString("hrsPackData","");
        return hrsPackData;
    }
    public void setHrsPackData(String hrsPackData) {
        this.hrsPackData = hrsPackData;
        sharedPreferences.edit().putString("hrsPackData", hrsPackData).commit();
    }
    public String getIsFirstTime(){
        isFirstTime=sharedPreferences.getString("isFirstTime","");
        return isFirstTime;
    }
    public void setIsFirstTime(String isFirstTime) {
        this.isFirstTime = isFirstTime;
        sharedPreferences.edit().putString("isFirstTime", isFirstTime).commit();
    }

    public String getReferId(){
        referId=sharedPreferences.getString("referId","");
        return referId;
    }
    public void setReferId(String referId) {
        this.referId = referId;
        sharedPreferences.edit().putString("referId", referId).commit();
    }

    public String getEmergency(){
        emergency=sharedPreferences.getString("emergency","");
        return emergency;
    }
    public void setEmergency(String emergency) {
        this.emergency = emergency;
        sharedPreferences.edit().putString("emergency", emergency).commit();
    }
    public String getProfileImage(){
        profileImage=sharedPreferences.getString("profileImage","");
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        sharedPreferences.edit().putString("profileImage", profileImage).commit();
    }
    public String getBookingData(){
        BookingData=sharedPreferences.getString("BookingData","");
        return BookingData;
    }
    public void setBookingData(String BookingData) {
        this.BookingData = BookingData;
        sharedPreferences.edit().putString("BookingData", BookingData).commit();
    }public String getState(){
        state=sharedPreferences.getString("state","");
        return state;
    }
    public void setState(String state) {
        this.state = state;
        sharedPreferences.edit().putString("state", state).commit();
    }

    public String getCity(){
        city=sharedPreferences.getString("city","");
        return city;
    }
    public void setCity(String city) {
        this.city = city;
        sharedPreferences.edit().putString("city", city).commit();
    }

    public String getLatitude(){
        latitude=sharedPreferences.getString("latitude","");
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
        sharedPreferences.edit().putString("latitude", latitude).commit();
    }

    public String getLongitude(){
        longitude=sharedPreferences.getString("longitude","");
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
        sharedPreferences.edit().putString("longitude", longitude).commit();
    }

    public String getMail(){
        mail=sharedPreferences.getString("mail","");
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
        sharedPreferences.edit().putString("mail", mail).commit();
    }

    public String getFirebaseTokenId(){
        firebaseTokenId=sharedPreferences.getString("firebaseTokenId","");
        return firebaseTokenId;
    }
    public void setFirebaseTokenId(String firebaseTokenId) {
        this.firebaseTokenId = firebaseTokenId;
        sharedPreferences.edit().putString("firebaseTokenId", firebaseTokenId).commit();
    }

    public String getBookid(){
        bookid=sharedPreferences.getString("bookid","");
        return bookid;
    }
    public void setBookid(String bookid) {
        this.bookid = bookid;
        sharedPreferences.edit().putString("bookid", bookid).commit();
    }

    public String getMobile(){
        mobile=sharedPreferences.getString("mobile","");
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
        sharedPreferences.edit().putString("mobile", mobile).commit();
    }
    public String getUserName() {
        userName =sharedPreferences.getString("userName","");
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
        sharedPreferences.edit().putString("userName", userName).commit();
    }


    public String getUserId() {
        userId =sharedPreferences.getString("userId","");
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
        sharedPreferences.edit().putString("userId", userId).commit();
    }
    public MyPref(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }

    public void logOut()
    {
        sharedPreferences.edit().clear().commit();

    }
    public void logOut(Context context)
    {
        sharedPreferences.edit().clear().commit();
        deleteCache(context);

    }

    //for clearing cache of app
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
