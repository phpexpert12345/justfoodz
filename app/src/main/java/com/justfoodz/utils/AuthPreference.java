package com.justfoodz.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreference {
    private static final String MY_PREFERENCES = "MY_PREFERENCES";
    private static final int MODE = Context.MODE_PRIVATE;
    private static AuthPreference pref;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public AuthPreference(Context context) {
        sharedPreference = context.getSharedPreferences(MY_PREFERENCES, MODE);
        editor = sharedPreference.edit();
    }

    public String getString(String key, String defValue) {
        return sharedPreference.getString(key, defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public void setRestaurantId(String value) {
        editor.putString("id", value).commit();
    }

    public String getRestaurantId() {
        return sharedPreference.getString("id", "");
    }




    public void setRestraurantCity(String value) {
        editor.putString("restaurantCity", value).commit();
    }

    public String getRestraurantCity() {
        return sharedPreference.getString("restaurantCity", "");
    }


    public void setRestaurantCategoryID(String value) {
        editor.putString("id", value).commit();
    }

    public String getRestaurantCategoryID() {
        return sharedPreference.getString("id", "");
    }

    public void setsubMenuItemId(String value) {
        editor.putString("id", value).commit();
    }

    public String getsubMenuItemId() {
        return sharedPreference.getString("id", "");
    }

    public void setCustomerId(String value) {
        editor.putString("CustomerId", value).commit();
    }

    public String getCustomerId() {
        return sharedPreference.getString("CustomerId", "");
    }


    public void setCustomerAddressId(String value) {
        editor.putString("CustomerAddressId", value).commit();
    }

    public String getCustomerAddressId() {
        return sharedPreference.getString("CustomerAddressId", "");
    }

    public void setFirstName(String value) {
        editor.putString("first_name", value).commit();
    }

    public String getFirstName() {
        return sharedPreference.getString("first_name", "");
    }

    public void setLastName(String value) {
        editor.putString("last_name", value).commit();
    }

    public String getLastName() {
        return sharedPreference.getString("last_name", "");
    }

    public void setUserEmail(String value) {
        editor.putString("user_email", value).commit();
    }

    public String getUserEmail() {
        return sharedPreference.getString("user_email", "");
    }

    public void setUserPhone(String value) {
        editor.putString("user_phone", value).commit();
    }

    public String getUserPhone() {
        return sharedPreference.getString("user_phone", "");


    }

    public void setCompanyStreet(String value) {
        editor.putString("company_street", value).commit();
    }

    public String getCompanyStreet() {
        return sharedPreference.getString("company_street", "");

    }

    public void setReferal_sharingmsg(String value) {
        editor.putString("referral_sharing_Message", value).commit();
    }

    public String getReferal_sharingmsg() {
        return sharedPreference.getString("referral_sharing_Message", "");

    }

    public void setReferal_codemsg(String value) {
        editor.putString("referral_codeMessage", value).commit();
    }

    public String getReferal_codemsg() {
        return sharedPreference.getString("referral_codeMessage", "");

    }

    public void setReferal_code(String value) {
        editor.putString("referral_code", value).commit();
    }

    public String getReferal_code() {
        return sharedPreference.getString("referral_code", "");

    }

    public void setREferalEarnPoints(String value) {
        editor.putString("referral_earn_points", value).commit();
    }

    public String getREferalEarnPoints() {
        return sharedPreference.getString("referral_earn_points", "");

    }

    public void setReferalJoinFriends(String value) {
        editor.putString("referral_join_friends", value).commit();
    }

    public String getReferalJoinFriends() {
        return sharedPreference.getString("referral_join_friends", "");

    }


    public void setCompanyStreetNo(String value) {//floor
        editor.putString("company_streetNo", value).commit();
    }

    public String getCompanyStreetNo() {
        return sharedPreference.getString("company_streetNo", "");


    }

    public void setUserCity(String value) {//floor
        editor.putString("user_city", value).commit();
    }

    public String getUserCity() {
        return sharedPreference.getString("user_city", "");


    }

    public void setUserState(String value) {//floor
        editor.putString("user_state", value).commit();
    }

    public String getUserState() {
        return sharedPreference.getString("user_state", "");


    }

    public void setUserPostcode(String value) {
        editor.putString("user_postcode", value).commit();
    }

    public String getUserPostcode() {
        return sharedPreference.getString("user_postcode", "");


    }


    public void setCustomerCity(String value) {
        editor.putString("customerCity", value).commit();
    }

    public String getCustomerCity() {
        return sharedPreference.getString("customerCity", "");


    }

    public void setCustomerState(String value) {
        editor.putString("customerState", value).commit();
    }

    public String getCustomerState() {
        return sharedPreference.getString("customerState", "");


    }


    public void setCustomerCountry(String value) {
        editor.putString("customerCountry", value).commit();
    }

    public String getCustomerCountry() {
        return sharedPreference.getString("customerCountry", "");


    }

    public void setCityName(String value) {
        editor.putString("city_name", value).commit();
    }

    public String getCityName() {
        return sharedPreference.getString("city_name", "");


    }



    public void setCustomerFloor(String value) {
        editor.putString("customerAppFloor", value).commit();
    }

    public String getCustomerFloor() {
        return sharedPreference.getString("customerAppFloor", "");


    }

    public void setAddressTitle(String value) {
        editor.putString("customerAddressLabel", value).commit();
    }

    public String getAddressTitle() {
        return sharedPreference.getString("customerAddressLabel", "");


    }


    public void setUserAddress(String value) {
        editor.putString("user_address", value).commit();
    }

    public String getUserAddress() {
        return sharedPreference.getString("user_address", "");
    }

    public void setRestaurantName(String value) {
        editor.putString("restaurant_name", value).commit();
    }

    public String getRestaurantName() {
        return sharedPreference.getString("restaurant_name", "");
    }

    public void setRestaurantAddress(String value) {
        editor.putString("restaurant_address", value).commit();
    }

    public String getRestaurantAddress() {
        return sharedPreference.getString("restaurant_address", "");
    }


    public void setItemId(String value) {
        editor.putString("id", value).commit();
    }

    public void setItemidId(String value) {
        editor.putString("FoodItemID", value).commit();
    }

    public String getItemId() {
        return sharedPreference.getString("id", "");
    }

    public String getItemidId() {
        return sharedPreference.getString("FoodItemID", "");
    }


    public void setOrderNo(String value) {
        editor.putString("orderIdentifyNo", value).commit();
    }

    public String getOrderNo() {
        return sharedPreference.getString("orderIdentifyNo", "");
    }


    public void setCouponCode(String value) {
        editor.putString("CouponCode", value).commit();
    }

    public String getCouponCode() {
        return sharedPreference.getString("CouponCode", "");
    }


    public void setMinprice(String value) {
        editor.putString("Minprice", value).commit();
    }

    public String getMinprice() {
        return sharedPreference.getString("Minprice", "");
    }

    public void setCouponCodePrice(String value) {
        editor.putString("CouponCodePrice", value).commit();
    }

    public String getCouponCodePrice() {
        return sharedPreference.getString("CouponCodePrice", "");
    }

    public void setCouponCodeType(String value) {
        editor.putString("couponCodeType", value).commit();
    }

    public String getCouponCodeType() {
        return sharedPreference.getString("couponCodeType", "");
    }

    public long getLong(String key, long defValue) {
        return sharedPreference.getLong(key, defValue);
    }
    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }


    public float getFloat(String key, float defValue) {
        return sharedPreference.getFloat(key, defValue);
    }
    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public boolean getBoolean(String key, boolean b) {
        return sharedPreference.getBoolean(key, false);
    }
    public void putBoolean(String key, boolean b) {
        editor.putBoolean(key, true).commit();
    }


    public boolean contains(String key) {
        return sharedPreference.contains(key);
    }

    public void remove(String key) {
        editor.remove(key).commit();
    }

    public void clear() {
        editor.clear().commit();
    }

    public void setGiftCardMoney(String total_gift_card_money_available) {
        editor.putString("Total_gift_card_money_available", total_gift_card_money_available).commit();
    }

    public String getGiftCardMoney() {
        return sharedPreference.getString("Total_gift_card_money_available", "");
    }

    public void setGiftWalletMoney(String loyality) {
        editor.putString("Total_wallet_money_available", loyality).commit();
    }

    public String getGiftWalletMoney() {
        return sharedPreference.getString("Total_wallet_money_available", "");
    }

    public void setDigitalWallet_CardNumber(String digital_wallet_card_number) {
        editor.putString("digital_wallet_card_number", digital_wallet_card_number).commit();
    }
    public String getDigitalWallet_CardNumber() {
        return sharedPreference.getString("digital_wallet_card_number", "");
    }

    public void setDigitalWallet_QrCode(String digital_wallet_card_number) {
        editor.putString("digital_wallet_qr_code", digital_wallet_card_number).commit();
    }
    public String getDigitalWallet_QrCode() {
        return sharedPreference.getString("digital_wallet_qr_code", "");
    }

    public void setDigitalWallet_PINNumber(String digital_wallet_card_number) {
        editor.putString("digital_wallet_card_pin_number", digital_wallet_card_number).commit();
    }
    public String getDigitalWallet_PINNumber() {
        return sharedPreference.getString("digital_wallet_card_pin_number", "");
    }

    public void setDigitalWallet_MemerSince(String digital_wallet_card_number) {
        editor.putString("digital_wallet_member_since", digital_wallet_card_number).commit();
    }
    public String getDigitalWallet_MemerSince() {
        return sharedPreference.getString("digital_wallet_member_since", "");
    }

    public void setBuyGiftImage(String giftimage) {
        editor.putString("GiftVoucherImage", giftimage).commit();
    }
    public String getBuyGiftImage() {
        return sharedPreference.getString("GiftVoucherImage", "");
    }

    public void setRestrarantid(String restaurantid) {
        editor.putString("restaurant_id", restaurantid).commit();
    }
    public String getRestrarantid() {
        return sharedPreference.getString("restaurant_id", "");
    }

    public void setTableId(String id) {
        editor.putString("id", id).commit();
    }
    public String getTableId() {
        return sharedPreference.getString("id", "");
    }

    public void setStatus(String grouPid) {
        editor.putString("status", grouPid).commit();
    }
    public String getStatus() {
        return sharedPreference.getString("status", "");
    }

    public void setgroup_id(String group_id) {
        editor.putString("group_id", group_id).commit();
    }
    public String getgroup_id() {
        return sharedPreference.getString("group_id", "");
    }

    public void setUserPhoto(String userProfileImage) {
        editor.putString("user_photo", userProfileImage).commit();
    }
    public String getUserPhoto() {
        return sharedPreference.getString("user_photo", "");
    }

    public void setDigitalWalletMessage(String website_digital_wallet_terms) {
        editor.putString("WEBSITE_DIGITAL_WALLET_TERMS", website_digital_wallet_terms).commit();
    }
    public String getDigitalWalletMessage() {
        return sharedPreference.getString("WEBSITE_DIGITAL_WALLET_TERMS", "");
    }
}