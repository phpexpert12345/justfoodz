package com.justfoodz.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.Database.Database;
import com.justfoodz.R;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.fragments.Model_MealDealList;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.UrlConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AllMealofDayActivity extends AppCompatActivity {

    private RecyclerView dealsrecycler;
    private ArrayList<RestaurantModel> DealsList;
    private HomeFragment.DealsAdapter dealsAdapter;
    CharSequence currentdate;
    ImageView iv_back;
    private AuthPreference authPreference;
    public   static RestaurantModel restaurantModel;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_mealof_day);
        authPreference = new AuthPreference(this);
        dealsrecycler = findViewById(R.id.dealsrecycler);
        iv_back=findViewById(R.id.iv_back);

        myPref = new MyPref(AllMealofDayActivity.this);
        Date d = new Date();
        currentdate  = DateFormat.format("yyyy-MM-dd", d.getTime());


        DealsList = new ArrayList<>();

        HitUrlMealDealss();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void HitUrlMealDealss() {
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_MealDealList> userLoginCall = interfaceRetrofit.mealsDeals(String.valueOf(currentdate), myPref.getState(),
                myPref.getCity(),
                myPref.getLatitude(), myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_MealDealList>() {
            @Override
            public void onResponse(Call<Model_MealDealList> call, retrofit2.Response<Model_MealDealList> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        if (!response.body().getSearchResult().get(i).getError().equalsIgnoreCase("2")) {
                            DealsList.add(response.body().getSearchResult().get(i));
                        }
                    }
                    if (DealsList.size() > 0) {

                        DealsAdapter dealsAdapter = new DealsAdapter(AllMealofDayActivity.this, DealsList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(AllMealofDayActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        dealsrecycler.setLayoutManager(horizontalLayoutManager1);
                        dealsrecycler.setAdapter(dealsAdapter);

                    } else {

                        dealsrecycler.setVisibility(View.GONE);
                    }
                }

              /*  if (Network.isNetworkCheck(getActivity())) {

                    HitURLforCuisineDisplay();


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<Model_MealDealList> call, Throwable t) {
//                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();

              /*  if (Network.isNetworkCheck(getActivity())) {

                    HitURLforCuisineDisplay();


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }
    public void HitUrlMealDeals(){
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.MealofDay, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse",response);
//                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray meallist = jsonObj.getJSONArray("MealDealList");
                    DealsList.clear();
                    for (int i = 0; i < meallist.length(); i++) {
                        JSONObject c = meallist.getJSONObject(i);

                        String id = c.getString("id");
                        String deal_name = c.getString("deal_name");
                        String deal_avilable_days = c.getString("deal_avilable_days");
                        String deal_price = c.getString("deal_price");
                        String deal_discount_amount = c.getString("deal_discount_amount");
                        String  start_hours = c.getString("meal_available_start_hours");
                        String  start_mins = c.getString("meal_available_start_mins");
                        String  end_hours = c.getString("meal_available_end_hours");
                        String  end_mins = c.getString("meal_available_end_mins");
                        String  deal_description = c.getString("deal_description");
                        String  restaurant_name = c.getString("restaurant_name");
                        String  restaurant_address = c.getString("restaurant_address");
                        String  restaurant_id = c.getString("restaurant_id");
                        String  restaurant_pickup_time = c.getString("restaurant_pickup_time");
                        String  restaurant_delivery_time = c.getString("restaurant_delivery_time");
                        String  restaurant_img = c.getString("restaurant_img");
                        String  restaurantsupervisory = c.getString("restaurantsupervisory");
                        String  restaurant_cuisine = c.getString("restaurant_cuisine");
                        String  restaurant_discount_covered = c.getString("restaurant_discount_covered");
                        String  homeDeliveryStatus = c.getString("homeDeliveryStatus");
                        String  PickupStatus = c.getString("PickupStatus");
                        String  SocialSharingMessage = c.getString("SocialSharingMessage");

                        String  Pre_homeDeliveryStatus = c.getString("Pre_homeDeliveryStatus");
                        String  Pre_Order_PickupStatus = c.getString("Pre_Order_PickupStatus");
                        String  restaurant_phone = c.getString("restaurant_phone");
                        String  restaurant_fax = c.getString("restaurant_fax");
                        String restaurant_contact_name = c.optString("restaurant_contact_name");
                        String restaurant_contact_phone = c.optString("restaurant_contact_phone");
                        String restaurant_contact_mobile = c.optString("restaurant_contact_mobile");
                        String restaurant_contact_email = c.optString("restaurant_contact_email");
                        String restaurant_sms_mobile = c.optString("restaurant_sms_mobile");
                        String restaurant_OrderEmail = c.optString("");
                        String restaurant_onlycashonAvailable = c.optString("restaurant_onlycashonAvailable");
                        String restaurant_onlycashon = c.getString("restaurant_onlycashon");
                        String restaurant_cardacceptAvailable = c.optString("restaurant_cardacceptAvailable");
                        String restaurant_cardaccept = c.optString("restaurant_cardaccept");
                        String shopfeature = c.optString("shopfeature");
                        String shopfeature_display = c.optString("shopfeature_display");
                        String restaurant_serviceFees = c.optString("restaurant_serviceFees");
                        String restaurant_saleTaxPercentage = c.optString("restaurant_saleTaxPercentage");
                        String restaurant_serviceVat = c.optString("restaurant_serviceVat");
                        String BookaTablesupport = c.optString("BookaTablesupport");

                        String bookatable = c.optString("BookaTablesupportAvailable");
                        String kidfriendly = c.optString("KidFriendlyAvailable");
                        String petfriendly = c.optString("PetFriendlyAvailable");
                        String dinein = c.optString("DineInAvailable");
                        String collection = c.optString("PickupAvailable");
                        String delievery = c.optString("HomeDeliveryAvailable");

                        String monday = c.optString("monday");
                        String tuesday = c.optString("tuesday");
                        String wednesday = c.optString("wednesday");
                        String thursday = c.optString("thursday");
                        String friday = c.optString("friday");
                        String saturday = c.optString("saturday");
                        String sunday = c.optString("sunday");
                        String displayTime = c.optString("displayTime");
                        String restaurant_LatitudePoint = c.optString("restaurant_LatitudePoint");
                        String restaurant_LongitudePoint = c.optString("restaurant_LongitudePoint");
                        String restaurantOffer = c.optString("restaurantOffer");
                        String restaurantOfferIcon = c.optString("restaurantOfferIcon");
                        String RestaurantOfferPrice = c.optString("RestaurantOfferPrice");
                        String DiscountType = c.optString("DiscountType");
                        String RestaurantOfferDescription = c.optString("RestaurantOfferDescription");
                        String TotalRestaurantReview = c.optString("TotalRestaurantReview");
                        String RestaurantTimeStatus = c.optString("RestaurantTimeStatus");
                        String RestaurantTimeStatus1 = c.optString("RestaurantTimeStatus1");
                        String restaurant_deliverycharge = c.optString("restaurant_deliverycharge");
                        String restaurant_minimumorder = c.optString("restaurant_minimumorder");
                        String restaurant_avarage_deliveryTime = c.optString("restaurant_avarage_deliveryTime");
                        String restaurant_avarage_PickupTime = c.optString("restaurant_avarage_PickupTime");
                        String restaurantCity = c.optString("restaurantCity");
                        String ratingAvg = c.optString("ratingAvg");
                        String restaurant_locality = c.optString("restaurant_locality");
                        String restaurant_Paypal_URL = c.optString("");
                        String restaurant_Paypal_bussiness_act = c.optString("");
                        String rating = c.optString("");
                        String ratingValue = c.optString("ratingValue");
                        String restaurant_about = c.optString("restaurant_about");
                        String featured = c.optString("featured");
                        String restaurant_deliveryDistance = c.optString("restaurant_deliveryDistance");
                        String restaurant_Logo = c.optString("restaurant_Logo");
                        String  deal_image = c.getString("deal_image");
                        String deal_price_add=c.getString("deal_price_add");
                        String restaurant_distance_check=c.getString("restaurant_distance_check");
                        String mealDealAvailable=c.getString("mealDealAvailable");
                        String  error = c.getString("error");

                        String Mealid=c.getString("Mealid");
                        String RestaurantTimeStatusText=c.getString("RestaurantTimeStatusText");

                        restaurantModel = new RestaurantModel();

                        restaurantModel.setId(id);
                        restaurantModel.setRestaurant_address(restaurant_address);
                        restaurantModel.setRestaurant_name(restaurant_name);
                        restaurantModel.setRestaurant_Logo(restaurant_Logo);
                        restaurantModel.setRestaurant_minimumorder(restaurant_minimumorder);
                        restaurantModel.setRestaurantOfferPrice(RestaurantOfferPrice);
                        restaurantModel.setRestaurantTimeStatus1(RestaurantTimeStatus1);
                        restaurantModel.setRestaurant_contact_phone(restaurant_contact_phone);
                        restaurantModel.setRestaurant_contact_mobile(restaurant_contact_mobile);
                        restaurantModel.setRestaurant_contact_email(restaurant_contact_email);
                        restaurantModel.setRestaurant_deliveryDistance(restaurant_deliveryDistance);
                        restaurantModel.setRestaurant_deliverycharge(restaurant_deliverycharge);
                        restaurantModel.setRestaurant_about(restaurant_about);
                        restaurantModel.setMonday(monday);
                        restaurantModel.setTuesday(tuesday);
                        restaurantModel.setTotalRestaurantReview(TotalRestaurantReview);
                        restaurantModel.setWednesday(wednesday);
                        restaurantModel.setThursday(thursday);
                        restaurantModel.setFriday(friday);
                        restaurantModel.setSaturday(saturday);
                        restaurantModel.setSunday(sunday);
                        restaurantModel.setRestaurant_cuisine(restaurant_cuisine);
                        restaurantModel.setRatingValue(ratingValue);
                        restaurantModel.setRatingAvg(ratingAvg);

                        restaurantModel.setHomeDeliveryStatus(homeDeliveryStatus);
                        restaurantModel.setPickupStatus(PickupStatus);
                        restaurantModel.setPre_homeDeliveryStatus(Pre_homeDeliveryStatus);
                        restaurantModel.setPre_Order_PickupStatus(Pre_Order_PickupStatus);
                        //Cash Accept
                        restaurantModel.setRestaurant_onlycashon(restaurant_onlycashon);
                        //card Accept
                        restaurantModel.setRestaurant_cardaccept(restaurant_cardaccept);
                        restaurantModel.setRestaurant_LatitudePoint(restaurant_LatitudePoint);
                        restaurantModel.setRestaurant_LongitudePoint(restaurant_LongitudePoint);
                        restaurantModel.setRestaurant_cardacceptAvailable(restaurant_cardacceptAvailable);
                        restaurantModel.setRestaurant_onlycashonAvailable(restaurant_onlycashonAvailable);

                        restaurantModel.setBookaTable(bookatable);
                        restaurantModel.setKidFriendly(kidfriendly);
                        restaurantModel.setPetFriendly(petfriendly);
                        restaurantModel.setDINEin(dinein);
                        restaurantModel.setCollection(collection);
                        restaurantModel.setDelivery(delievery);
                        restaurantModel.setDeal_price_toadd(deal_price_add);

                        restaurantModel.setDeal_name(deal_name);
                        restaurantModel.setDeal_avilable_days(deal_avilable_days);
                        restaurantModel.setDeal_price(deal_price);
                        restaurantModel.setDeal_discount_amount(deal_discount_amount);
                        restaurantModel.setStart_hours(start_hours);
                        restaurantModel.setStart_mins(start_mins);
                        restaurantModel.setEnd_hours(end_hours);
                        restaurantModel.setEnd_mins(end_mins);
                        restaurantModel.setDeal_description(deal_description);
                        restaurantModel.setDeal_image(deal_image);
                        restaurantModel.setRestaurant_id(restaurant_id);
                        restaurantModel.setRestaurant_pickup_time(restaurant_pickup_time);
                        restaurantModel.setRestaurant_delivery_time(restaurant_delivery_time);
                        restaurantModel.setRestaurant_img(restaurant_img);
                        restaurantModel.setMealid(Mealid);
                        restaurantModel.setrestaurant_distance_check(restaurant_distance_check);
                        restaurantModel.setmealDealAvailable(mealDealAvailable);
                        restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                        DealsList.add(restaurantModel);


                        DealsAdapter dealsAdapter = new  DealsAdapter(AllMealofDayActivity.this,DealsList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(AllMealofDayActivity.this, LinearLayoutManager.VERTICAL, false);
                        dealsrecycler.setLayoutManager(horizontalLayoutManager1);
                        dealsrecycler.setAdapter(dealsAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ex",""+e);

                }

            }
        }, new  Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline("Please check your network connection");
                Log.e("error",""+error);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("meal_current_date", String.valueOf(currentdate));
                params.put("customer_country",myPref.getState());
                params.put("customer_city",myPref.getCity());
                params.put("customer_lat",myPref.getLatitude());
                params.put("customer_long",myPref.getLongitude());
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(AllMealofDayActivity.this);
        requestQueue.add(strReq);
    }
    private void showCustomDialog1decline (String s)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(AllMealofDayActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
    public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

        Context context;

        ArrayList<RestaurantModel> DealsList;

        public DealsAdapter(Context c, ArrayList<RestaurantModel> DealsList) {
            this.context = c;
            this.DealsList = DealsList;
        }

        @Override
        public  DealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.allmealofdaylist, parent, false);
            return new  DealsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final DealsAdapter.ViewHolder holder, final int position) {

            holder.dealname.setText(DealsList.get(position).getDeal_name());
            Picasso.get().load(DealsList.get(position).getDeal_image()).into(holder.dealimage);
            holder.dealprice.setText(SplashScreenActivity.currency_symbol+" "+DealsList.get(position).getDeal_price());
            holder.dealdiscountamount.setText("("+DealsList.get(position).getDeal_discount_amount()+"% OFF)");
            holder.dealdescription.setText(DealsList.get(position).getDeal_description());
            holder.drestraurantname.setText(DealsList.get(position).getRestaurant_name());
            Picasso.get().load(DealsList.get(position).getRestaurant_img()).into(holder.restraurantimagege);
            holder.pickuptime.setText(DealsList.get(position).getRestaurant_pickup_time()+" Mins");
            holder.delieverytime.setText(DealsList.get(position).getRestaurant_delivery_time()+" Mins");
            holder.restaurantaddress.setText(DealsList.get(position).getRestaurant_address());
            holder.dealavilabledays.setText(DealsList.get(position).getDeal_avilable_days());
            holder.mealstarthour.setText("between - "+DealsList.get(position).getStart_hours()+":");
            holder.mealstartmint.setText(DealsList.get(position).getStart_mins()+" - ");
            holder.mealendhour.setText(DealsList.get(position).getEnd_hours()+":");
            holder.mealendmint.setText(DealsList.get(position).getEnd_mins());

            holder.ordernow.setOnClickListener(view -> {
                try {
                    if (DealsList.get(position).getRestaurantTimeStatus1().equals("Closed") || DealsList.get(position).getRestaurantTimeStatus1().equals("Busy")) {
                        showCustomDialog1decline(DealsList.get(position).getRestaurantTimeStatusText());
                    } else {
                        final Database database = new Database(context);
                        final String subMenuItemId = DealsList.get(position).getMealid();
                        int qunt = 0;
                        double price = 0.0;
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM deal_item_table where deal_item_id='" + subMenuItemId + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(3)) + 1;
                            price = Double.parseDouble(DealsList.get(position).getDeal_price()) * qunt;
                            database.deal_update_item(subMenuItemId, qunt, price);
                        } else {
                            database.deal_InsertItem(subMenuItemId, DealsList.get(position).getDeal_name(), Double.parseDouble(DealsList.get(position).getDeal_price_toadd()), 1);
                            Ravifinalitem.cart_Item_number = Ravifinalitem.cart_Item_number + 1;
                        }
                        database.close();

                        HomeFragment.restaurant_distance_check = DealsList.get(position).getrestaurant_distance_check();
                        SubMenuActivity.restaurantAddress = DealsList.get(position).getRestaurant_address();
                        SubMenuActivity.restaurantName = DealsList.get(position).getRestaurant_name();
                        SubMenuActivity.id = DealsList.get(position).getRestaurant_id();

                        RestaurantsListActivity.res_lat = DealsList.get(position).getRestaurant_LatitudePoint();
                        RestaurantsListActivity.res_lng = DealsList.get(position).getRestaurant_LongitudePoint();
                        RestaurantsListActivity.cash_avaible = DealsList.get(position).getRestaurant_onlycashonAvailable();
                        RestaurantsListActivity.card_avaible = DealsList.get(position).getRestaurant_cardacceptAvailable();
                        HomeFragment.restaurant_distance_check = DealsList.get(position).getrestaurant_distance_check();
                        Intent intent = new Intent(context, MainMenuActivity.class);
                        String id = DealsList.get(position).getId();
                        RestaurantModel restaurantModel = DealsList.get(position);
                        authPreference.setRestraurantCity("" + DealsList.get(position).getRestaurantCity());
                        authPreference.setMinprice(DealsList.get(position).getRestaurant_minimumorder());
                        intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                        intent.putExtra("restaurantModelArrayListReceived", DealsList);
                        intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                        intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                        intent.putExtra("restaurantAddress", SubMenuActivity.restaurantAddress);
                        intent.putExtra("restaurantName", SubMenuActivity.restaurantName);
                        intent.putExtra("id", SubMenuActivity.id);
                        startActivity(intent);
                    }
                }catch (Exception e){

                }
            });
        }

        @Override
        public int getItemCount() {
            return DealsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder  {

            TextView dealname,dealprice,dealdiscountamount,dealdescription,drestraurantname ,pickuptime,delieverytime,
                    restaurantaddress,dealavilabledays,mealstarthour,mealstartmint,mealendhour,mealendmint;
            ImageView dealimage,restraurantimagege;
            Button ordernow;


            public ViewHolder(final View itemView) {
                super(itemView);
                dealname =  itemView.findViewById(R.id.dealname);
                dealimage =  itemView.findViewById(R.id.dealimage);
                dealprice=itemView.findViewById(R.id.dealprice);
                dealdiscountamount = itemView.findViewById(R.id.dealdiscountamount);
                dealdescription = itemView.findViewById(R.id.dealdescription);
                restraurantimagege = itemView.findViewById(R.id.restraurantimagege);
                drestraurantname = itemView.findViewById(R.id.drestraurantname);
                pickuptime = itemView.findViewById(R.id.pickuptime);
                delieverytime=itemView.findViewById(R.id.delieverytime);
                ordernow=itemView.findViewById(R.id.ordernow);
                dealavilabledays=itemView.findViewById(R.id.dealavilabledays);
                mealstarthour=itemView.findViewById(R.id.mealstarthour);
                restaurantaddress=itemView.findViewById(R.id.restaurantaddress);
                mealstartmint=itemView.findViewById(R.id.mealstartmint);
                mealendhour=itemView.findViewById(R.id.mealendhour);
                mealendmint=itemView.findViewById(R.id.mealendmint);

            }
        }
    }
}