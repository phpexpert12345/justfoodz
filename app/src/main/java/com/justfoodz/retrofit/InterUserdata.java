package com.justfoodz.retrofit;

import com.justfoodz.activities.CartActivity;
import com.justfoodz.activities.Model_AccountRegistration;
import com.justfoodz.activities.Model_AmenitiesSearchList;
import com.justfoodz.activities.Model_BuyGiftCard;
import com.justfoodz.activities.Model_CityListing;
import com.justfoodz.activities.Model_CuisinesListing;
import com.justfoodz.activities.Model_CustomerDetails;
import com.justfoodz.activities.Model_DeliveryCharge;
import com.justfoodz.activities.Model_Discount;
import com.justfoodz.activities.Model_ExtraToppingGroupList;
import com.justfoodz.activities.Model_GetServiceCharge;
import com.justfoodz.activities.Model_GiftRedeam;
import com.justfoodz.activities.Model_GiftRedeamList;
import com.justfoodz.activities.Model_GiftSearchList;
import com.justfoodz.activities.Model_OrderModelListing;
import com.justfoodz.activities.Model_PaymentModel;
import com.justfoodz.activities.Model_RestaurantMenItemsSize;
import com.justfoodz.activities.Model_RestaurantMenuItem;
import com.justfoodz.activities.Model_RestaurantTableList;
import com.justfoodz.activities.Model_UpdateProfileUpdate;
import com.justfoodz.activities.Model_UpdateProfileUpdateNew;
import com.justfoodz.activities.Model_forgetpassword;
import com.justfoodz.activities.Model_loyalty_point;
import com.justfoodz.activities.Model_restaurant_list;
import com.justfoodz.activities.Model_walletMoney;
import com.justfoodz.activities.Model_wallet_password_reset;
import com.justfoodz.activities.SelectAddressActivity;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.fragments.Model_AppFlashBannerList;
import com.justfoodz.fragments.Model_BannerDetails;
import com.justfoodz.fragments.Model_CuisiineDisplay;
import com.justfoodz.fragments.Model_FeaturePartnerModel;
import com.justfoodz.fragments.Model_Free_Delivery_Reasurant;
import com.justfoodz.fragments.Model_MealDealList;
import com.justfoodz.fragments.Model_Must_Try_Reasurant;
import com.justfoodz.fragments.Model_National_Brands_Reasurant;
import com.justfoodz.fragments.Model_New_Foodz_Reasurant;
import com.justfoodz.fragments.Model_Reasurant;
import com.justfoodz.fragments.Model_RestaurantMenuCategory;
import com.justfoodz.fragments.Model_Special_Offer_Reasurant;
import com.justfoodz.fragments.Model_feature_restaurant_List;
import com.justfoodz.fragments.RestaurantMencategoryArray;
import com.justfoodz.models.MenuModel;
import com.justfoodz.models.SubList.RestaurantMencategory;

import java.util.Map;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

//
public interface InterUserdata {


    @POST("phpexpert_restaurantMenuCategory.php")
    @FormUrlEncoded
    Call<MenuModel> getLoginResponse(@FieldMap Map<String, String> params);

    @POST("charge.php")
    @FormUrlEncoded
    Call<StripeResponse> getStripePaymentResponse(@FieldMap Map<String, String> params);

    @POST("add_wallet_money_by_card.php")
    @FormUrlEncoded
    Call<StripeResponse> getWalletReponse(@FieldMap Map<String, String> params);




    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuCategory.php")
    Call<RestaurantMencategoryArray> phpexpert_restaurantMenuCategorys(@Field("resid") String resid, @Field("menu_type") String menu_type, @Field("menu_items_current_time") String menu_items_current_time,
                                                                       @Field("CustomerId") String CustomerId);



    @FormUrlEncoded
    @POST("phpexpert_app_open_location.php")
    Call<Model_location> userLocation(@Field("customer_long") String customer_long, @Field("customer_lat") String customer_lat);


    @FormUrlEncoded
    @POST("phpexpert_gift_card_redeem_action.php")
    Call<Model_GiftRedeam> GiftRadeemUpdateData(@Field("CustomerId") String CustomerId, @Field("GiftVoucherCode") String GiftVoucherCode,
                                                @Field("GiftVoucherPin") String GiftVoucherPin);

    @FormUrlEncoded
    @POST("phpexpert_feature_restaurant.php")
    Call<Model_Reasurant> featureReasurant(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                           @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);


    @FormUrlEncoded
    @POST("phpexpert_app_flash_banner.php")
    Call<Model_AppFlashBannerList> flashbanner(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                               @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);


    @FormUrlEncoded
    @POST("phpexpert_meal_a_deal_list.php")
    Call<Model_MealDealList> mealsDeals(@Field("meal_current_date") String meal_current_date, @Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                        @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @GET("phpexpert_home_cuisine_display.php")
    Call<Model_CuisiineDisplay> phpexpert_home_cuisine_display();

    @GET("phpexpert_quick_food_type.php")
    Call<Model_FeaturePartnerModel> FoodTypeQuickSearch();

    @GET("phpexpert_home_cuisine_display.php")
    Call<Model_National_Brands_Reasurant> NationalBrandsSearch();

    @FormUrlEncoded
    @POST("phpexpert_popular_restaurant.php")
    Call<Model_Reasurant> hitURLforPopularNear(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                               @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @FormUrlEncoded
    @POST("phpexpert_free_delivery_restaurant.php")
    Call<Model_Free_Delivery_Reasurant> hitURLforFreeDelivery(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                               @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @FormUrlEncoded
    @POST("phpexpert_new_restaurant.php")
    Call<Model_New_Foodz_Reasurant> hitURLforNewFoodz(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                                              @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @FormUrlEncoded
    @POST("phpexpert_discount_restaurant.php")
    Call<Model_Special_Offer_Reasurant> hitURLforSpecialOffer(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                                          @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @FormUrlEncoded
    @POST("phpexpert_must_try_restaurant.php")
    Call<Model_Must_Try_Reasurant> hitURLforMustTryRest(@Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                                     @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);


    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuItem.php")
    Call<Model_RestaurantMenuItem> phpexpert_restaurantMenuItem(@Field("resid") String resid, @Field("RestaurantCategoryID") String RestaurantCategoryID,
                                                                @Field("menu_items_current_time") String menu_items_current_time);

    @FormUrlEncoded
    @POST("phpexpert_customer_forgot_password.php")
    Call<Model_forgetpassword> forgetPassword(@Field("user_email") String user_email);

    @FormUrlEncoded
    @POST("phpexpert_gift_card_redeem_list.php")
    Call<Model_GiftRedeamList> phpexpert_gift_card_redeem_list(@Field("CustomerId") String CustomerId);

    @FormUrlEncoded
    @POST("discountGet.php")
    Call<Model_Discount> discountGet(@Field("resid") String resid, @Field("subTotal") String subTotal);


    @FormUrlEncoded
    @POST("phpexpert_customer_login.php")
    Call<Model_CustomerDetails> userLogin(@Field("user_email") String user_email, @Field("user_pass") String user_pass, @Field("device_id") String device_id,
                                          @Field("device_platform") String device_platform);


    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuItemExtra.php")
    Call<Model_ExtraToppingGroupList> phpexpert_restaurantMenuItemExtra(@Field("ItemID") String ItemID, @Field("SizeDI") String SizeDI);


    @FormUrlEncoded
    @POST("phpexpert_search.php")
    Call<Model_Reasurant> phpexpert_search(@Field("where") String where, @Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                           @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long, @Field("customer_search_display") String customer_search_display);

    @FormUrlEncoded
    @POST("phpexpert_search.php")
    Call<Model_Reasurant> phpexpert_search_where(@Field("where") String where);

    @FormUrlEncoded
    @POST("phpexpert_get_restaurant_table_list.php")
    Call<Model_RestaurantTableList> phpexpert_get_restaurant_table_list(@Field("restaurant_id") String restaurant_id);

    @FormUrlEncoded
    @POST("phpexpert_get_restaurant_list.php")
    Call<Model_restaurant_list> phpexpert_get_restaurant_list(@Field("restaurantCity") String restaurantCity);

    @FormUrlEncoded
    @POST("phpexpert_search.php")
    Call<Model_Reasurant> phpexpert_search_where_and_customerLocation(@Field("where") String where, @Field("customerlocality") String customerlocality);


    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuItemSize.php")
    Call<Model_RestaurantMenItemsSize> phpexpert_restaurantMenuItemSize(@Field("ItemID") String ItemID);

    @FormUrlEncoded
    @POST("phpexpert_customer_profile_info.php")
    Call<Model_CustomerProfile> phpexpert_customer_profile_info(@Field("CustomerId") String CustomerId);

    @FormUrlEncoded
    @POST("phpexpert_customer_wallet_moeny.php")
    Call<Model_walletMoney> phpexpert_customer_wallet_moeny(@Field("CustomerId") String CustomerId);

    @FormUrlEncoded
    @POST("phpexpert_OrderDisplay.php")
    Call<Model_OrderModelListing> phpexpert_OrderDisplay(@Field("CustomerId") String CustomerId);

    @FormUrlEncoded
    @POST("phpexpert_customer_loyalty_point.php")
    Call<Model_loyalty_point> phpexpert_customer_loyalty_point(@Field("CustomerId") String CustomerId);




    @FormUrlEncoded
    @POST("phpexpert_buy_gift_card_payment.php")
    Call<Model_BuyGiftCard> phpexpert_buy_gift_card_payments(@Field("CustomerId") String CustomerId,@Field("transaction_id") String transaction_id,@Field("giftcardAmount") String giftcardAmount
    ,@Field("giftcardID") String giftcardID,@Field("giftcard_user_email") String giftcard_user_email,@Field("giftcard_user_name") String giftcard_user_name);

    @FormUrlEncoded
    @POST("phpexpert_customer_profite_update.php")
    Call<Model_UpdateProfileUpdate> phpexpert_customer_profite_update(@Field("CustomerId") String CustomerId, @Field("CustomerFirstName") String CustomerFirstName,
                                                                      @Field("CustomerLastName") String CustomerLastName, @Field("CustomerEmail") String CustomerEmail,
                                                                      @Field("CustomerPhone") String CustomerPhone, @Field("customerAppFloor") String customerAppFloor,
                                                                      @Field("customerStreet") String customerStreet, @Field("customerZipcode") String customerZipcode,
                                                                      @Field("customerCity") String customerCity, @Field("customerState") String customerState,
                                                                      @Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                                                      @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);

    @FormUrlEncoded
    @POST("phpexpert_customer_profite_update.php")
    Call<Model_AccountRegistration> phpexpert_customer_account_register(@Field("first_name") String first_name, @Field("last_name") String last_name,
                                                                        @Field("user_email") String user_email, @Field("user_pass") String user_pass,
                                                                        @Field("user_phone") String user_phone, @Field("referral_code") String referral_code,
                                                                        @Field("device_id") String device_id, @Field("device_platform") String device_platform,
                                                                        @Field("customer_country") String customer_country, @Field("customer_city") String customer_city,
                                                                        @Field("customer_lat") String customer_lat, @Field("customer_long") String customer_long);


    @FormUrlEncoded
    @POST("phpexpert_wallet_payment_report.php")
    Call<Model_GetServiceCharge> phpexpert_wallet_payment_report(@Field("CustomerId") String CustomerId, @Field("transaction_start_date") String transaction_start_date,
                                                                 @Field("transaction_end_date") String transaction_end_date, @Field("digital_wallet_card_number") String digital_wallet_card_number);

    @FormUrlEncoded
    @POST("phpexpert_customer_wallet_password_reset.php")
    Call<Model_wallet_password_reset> phpexpert_customer_wallet_password_reset(@Field("digital_wallet_card_number") String digital_wallet_card_number,
                                                                               @Field("CustomerId") String CustomerId);


    @FormUrlEncoded
    @POST("ServiceChargetGet.php")
    Call<Model_GetServiceCharge> ServiceChargetGet(@Field("resid") String resid, @Field("subTotal") String subTotal);

    @FormUrlEncoded
    @POST("phpexpert_customer_checkout_account_register.php")
    Call<Model_UpdateProfileUpdateNew> phpexpert_customer_checkout_account_registers(@Field("first_name") String first_name, @Field("last_name") String last_name,
                                                                                     @Field("user_email") String user_email, @Field("user_pass") String user_pass,
                                                                                     @Field("user_phone") String user_phone, @Field("customerAddressLabel") String customerAddressLabel,
                                                                                     @Field("customerAppFloor") String customerAppFloor, @Field("customerStreet") String customerStreet,
                                                                                     @Field("customerCity") String customerCity, @Field("customerState") String customerState,
                                                                                     @Field("customerZipcode") String customerZipcode, @Field("customerCountry") String customerCountry,
                                                                                     @Field("device_id") String device_id, @Field("device_platform") String device_platform,
                                                                                     @Field("referral_code") String referral_code, @Field("customer_country") String customer_country,
                                                                                     @Field("customer_city") String customer_city, @Field("customer_lat") String customer_lat,
                                                                                     @Field("customer_long") String customer_long);


    @FormUrlEncoded
    @POST("DeliveryChargeGet.php")
    Call<Model_DeliveryCharge> DeliveryChargeGet(@Field("resid") String resid, @Field("subTotal") String subTotal, @Field("restaurant_locality") String restaurant_locality);

    @FormUrlEncoded
    @POST("phpexpert_payment_android_submit.php")
    Call<Model_PaymentModel> phpexpert_payment_android_submit(@Field("Id1[]") String Id1, @Field("CategoryId1[]") String CategoryId1, @Field("Quantity1[]") String Quantity1, @Field("mealID[]") String mealID
            , @Field("mealPrice[]") String mealPrice, @Field("mealquqntity[]") String mealquqntity, @Field("Price1[]") String Price1, @Field("strsizeid1[]") String strsizeid1, @Field("extraItemID1[]") String extraItemID1
            , @Field("extraItemIDName1[]") String extraItemIDName1, @Field("resid") String resid, @Field("CustomerId") String CustomerId, @Field("CustomerAddressId") String CustomerAddressId
            , @Field("payment_type") String payment_type, @Field("order_price") String order_price, @Field("subTotalAmount") String subTotalAmount, @Field("delivery_date") String delivery_date
            , @Field("delivery_time") String delivery_time, @Field("instructions1") String instructions1, @Field("deliveryCharge") String deliveryCharge, @Field("group_member_id") String group_member_id
            , @Field("CouponCode") String CouponCode, @Field("CouponCodePrice") String CouponCodePrice, @Field("couponCodeType") String couponCodeType, @Field("SalesTaxAmount") String SalesTaxAmount
            , @Field("order_type") String order_type, @Field("SpecialInstruction") String SpecialInstruction, @Field("extraTipAddAmount") String extraTipAddAmount, @Field("RestaurantNameEstimate") String RestaurantNameEstimate
            , @Field("discountOfferDescription") String discountOfferDescription, @Field("discountOfferPrice") String discountOfferPrice, @Field("RestaurantoffrType") String RestaurantoffrType
            , @Field("ServiceFees") String ServiceFees, @Field("PaymentProcessingFees") String PaymentProcessingFees, @Field("deliveryChargeValueType") String deliveryChargeValueType
            , @Field("ServiceFeesType") String ServiceFeesType, @Field("PackageFeesType") String PackageFeesType, @Field("PackageFees") String PackageFees, @Field("WebsiteCodePrice") String WebsiteCodePrice
            , @Field("WebsiteCodeType") String WebsiteCodeType, @Field("WebsiteCodeNo") String WebsiteCodeNo, @Field("preorderTime") String preorderTime, @Field("VatTax") String VatTax, @Field("FoodCosts") String FoodCosts
            , @Field("GiftCardPay") String GiftCardPay, @Field("WalletPay") String WalletPay, @Field("payment_transaction_paypal") String payment_transaction_paypal
            , @Field("customer_country") String customer_country, @Field("customer_current_address") String customer_current_address, @Field("table_number_assign") String table_number_assign, @Field("customer_lat") String customer_lat,
                                                              @Field("customer_long") String customer_long, @Field("loyptPoints") String loyptPoints);


    @GET
    Call<Model_CityListing> citylising(@Url String fileUrl);

    @GET
    Call<Model_GiftSearchList> phpexpert_buy_gift_card_list(@Url String fileUrl);


    @GET
    Call<Model_Reasurant> locationsearching(@Url String fileUrl);

    @GET
    Call<Model_CuisinesListing> phpexpert_cuisine(@Url String fileUrl);

    @GET
    Call<Model_AmenitiesSearchList> phpexpert_quick_food_Amenities(@Url String fileUrl);


}
