package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.adapters.CuisinesAdapter;
import com.justfoodz.fragments.Model_Reasurant;
import com.justfoodz.interfaces.CuisinesItemClickListener;
import com.justfoodz.models.CuisinesModel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CuisinesListActivity extends AppCompatActivity implements View.OnClickListener, CuisinesItemClickListener {
    private ImageView ivBack;
    private RecyclerView rvCuisinesList;
    private CuisinesAdapter cuisinesAdapter;
    private ProgressDialog pDialog;
    private ArrayList<CuisinesModel> cuisinesModelArrayList;
    private CuisinesModel cuisinesModel;
    private EditText searchView;
    private static final String TAG = "CuisinesList";
    private String passCode, cuisine_name, errorMsg;
    private AuthPreference authPreference;
    private ArrayList<RestaurantModel> restaurantModelArrayList;
    private RestaurantModel restaurantModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisines_list);
        statusBarColor();
        initialization();
        initializedListener();
        if (Network.isNetworkCheck(this)) {
            cuisinesList();

        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        searchView = findViewById(R.id.search_view);
        rvCuisinesList = findViewById(R.id.rv_cuisinesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCuisinesList.setLayoutManager(mLayoutManager);
        rvCuisinesList.setItemAnimator(new DefaultItemAnimator());
        authPreference = new AuthPreference(this);
        passCode = getIntent().getStringExtra("passCode");

    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CuisinesListActivity.this.cuisinesAdapter.getFilter().filter(s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            default:
                break;
        }
    }

    public void cuisinesList() {
        /*pDialog = new ProgressDialog(CuisinesListActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);*/
        cuisinesModelArrayList = new ArrayList<CuisinesModel>();
//        pDialog.show();

        MyProgressDialog.show(CuisinesListActivity.this, R.string.please_wait);

        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CuisinesListing> userLoginCall = interfaceRetrofit.phpexpert_cuisine("https://www.justfoodz.com/api-access/phpexpert_cuisine.php");
        userLoginCall.enqueue(new Callback<Model_CuisinesListing>() {
            @Override
            public void onResponse(Call<Model_CuisinesListing> call, retrofit2.Response<Model_CuisinesListing> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getCuisineList().size(); i++) {
                        cuisinesModelArrayList.add(response.body().getCuisineList().get(i));
                    }

                    if (cuisinesModelArrayList.size() > 0) {
                        cuisinesAdapter = new CuisinesAdapter(CuisinesListActivity.this, cuisinesModelArrayList, CuisinesListActivity.this);
                        rvCuisinesList.setAdapter(cuisinesAdapter);
                        cuisinesAdapter.setCuisineList(getApplicationContext(), cuisinesModelArrayList);
                    }

                    MyProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Model_CuisinesListing> call, Throwable t) {
                Toast.makeText(CuisinesListActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

                MyProgressDialog.dismiss();
            }
        });


       /* StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_CUISINES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("CuisineList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cuisine_name = jsonObject.getString("cuisine_name");
                        cuisinesModel = new CuisinesModel();
                        cuisinesModel.setCuisineName(cuisine_name);
                        cuisinesModelArrayList.add(cuisinesModel);
                    }
                    cuisinesAdapter = new CuisinesAdapter(CuisinesListActivity.this, cuisinesModelArrayList, CuisinesListActivity.this);
                    rvCuisinesList.setAdapter(cuisinesAdapter);
                    cuisinesAdapter.setCuisineList(getApplicationContext(), cuisinesModelArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CuisinesListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to Search
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(CuisinesListActivity.this);
        requestQueue.add(strReq);*/
    }


    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void cuisinesItemClick(int position, String cuisineName) {
        if (Network.isNetworkCheck(this)) {
            getFilteredRestaurantList(cuisineName);

        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

        }


    }

    private void getFilteredRestaurantList(final String cuisineName) {


        restaurantModelArrayList = new ArrayList<RestaurantModel>();
        MyProgressDialog.show(this, R.string.please_wait);

        String customerLocality = "";
        String urees = " https://www.justfoodz.com/api-access/phpexpert_search.php?where=" + getIntent().getStringExtra("cityname") + "&customerlocality=" + customerLocality +
                "&cuisine=" + cuisineName + "";

        String uree="https://www.justfoodz.com/api-access/phpexpert_search.php?cuisine="+cuisineName+"&where="+getIntent().getStringExtra("cityname")+"";


        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.locationsearching(uree);
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                        if (restaurantModel.getError().equalsIgnoreCase("1")) {
                            restaurantModelArrayList.add(restaurantModel);

                        }
                    }
                    if (restaurantModelArrayList.size() > 0) {
                        Intent intent = (new Intent(CuisinesListActivity.this, RestaurantsListActivity.class));
                        intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                        intent.putExtra("passCode", passCode);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Toast.makeText(CuisinesListActivity.this, "There are no restaurants In Your area currently.", Toast.LENGTH_SHORT).show();
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
                Toast.makeText(CuisinesListActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


/*
        pDialog = new ProgressDialog(CuisinesListActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        restaurantModelArrayList = new ArrayList<RestaurantModel>();
        pDialog.show();
        String url = UrlConstants.URL_SEARCH + "?cuisine=" + cuisineName + "&where=" + passCode;
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("qwqwqwqw", response);
                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONArray jsonArray = jObj.getJSONArray("searchResult");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.has("error_msg")) {
                            String id = jsonObject.optString("id");
                            authPreference.setRestaurantId(id);
                            String restaurant_name = jsonObject.optString("restaurant_name");
                            authPreference.setRestaurantName(restaurant_name);
                            String restaurantsupervisory = jsonObject.optString("restaurantsupervisory");
                            String restaurant_address = jsonObject.optString("restaurant_address");
                            authPreference.setRestaurantAddress(restaurant_address);
                            String restaurant_cuisine = jsonObject.optString("restaurant_cuisine");
                            String restaurant_discount_covered = jsonObject.optString("restaurant_discount_covered");
                            String homeDeliveryStatus = jsonObject.optString("homeDeliveryStatus");
                            String PickupStatus = jsonObject.optString("PickupStatus");
                            String Pre_homeDeliveryStatus = jsonObject.optString("Pre_homeDeliveryStatus");
                            String Pre_Order_PickupStatus = jsonObject.optString("Pre_Order_PickupStatus");
                            String restaurant_phone = jsonObject.optString("restaurant_phone");
                            String restaurant_fax = jsonObject.optString("");
                            String restaurant_contact_name = jsonObject.optString("restaurant_contact_name");
                            String restaurant_contact_phone = jsonObject.optString("restaurant_contact_phone");
                            String restaurant_contact_mobile = jsonObject.optString("restaurant_contact_mobile");
                            String restaurant_contact_email = jsonObject.optString("restaurant_contact_email");
                            String restaurant_sms_mobile = jsonObject.optString("restaurant_sms_mobile");
                            String restaurant_OrderEmail = jsonObject.optString("");
                            String restaurant_onlycashonAvailable = jsonObject.optString("restaurant_onlycashonAvailable");
                            String restaurant_onlycashon = jsonObject.getString("restaurant_onlycashon");
                            String restaurant_cardacceptAvailable = jsonObject.optString("restaurant_cardacceptAvailable");
                            String restaurant_cardaccept = jsonObject.optString("restaurant_cardaccept");
                            String restaurant_serviceFees = jsonObject.optString("restaurant_serviceFees");
                            String restaurant_saleTaxPercentage = jsonObject.optString("restaurant_saleTaxPercentage");
                            String restaurant_serviceVat = jsonObject.optString("restaurant_serviceVat");
                            String BookaTablesupport = jsonObject.optString("BookaTablesupport");

                            String bookatable = jsonObject.optString("BookaTablesupportAvailable");
                            String kidfriendly = jsonObject.optString("KidFriendlyAvailable");
                            String petfriendly = jsonObject.optString("PetFriendlyAvailable");
                            String dinein = jsonObject.optString("DineInAvailable");
                            String collection = jsonObject.optString("PickupAvailable");
                            String delievery = jsonObject.optString("HomeDeliveryAvailable");

                            String monday = jsonObject.optString("monday");
                            String tuesday = jsonObject.optString("tuesday");
                            String wednesday = jsonObject.optString("wednesday");
                            String thursday = jsonObject.optString("thursday");
                            String friday = jsonObject.optString("friday");
                            String saturday = jsonObject.optString("saturday");
                            String sunday = jsonObject.optString("sunday");
                            String displayTime = jsonObject.optString("displayTime");
                            String restaurant_LatitudePoint = jsonObject.optString("restaurant_LatitudePoint");
                            String restaurant_LongitudePoint = jsonObject.optString("restaurant_LongitudePoint");
                            String restaurantOffer = jsonObject.optString("restaurantOffer");
                            String restaurantOfferIcon = jsonObject.optString("restaurantOfferIcon");
                            String RestaurantOfferPrice = jsonObject.optString("RestaurantOfferPrice");
                            String DiscountType = jsonObject.optString("DiscountType");
                            String RestaurantOfferDescription = jsonObject.optString("RestaurantOfferDescription");
                            String TotalRestaurantReview = jsonObject.optString("TotalRestaurantReview");
                            String RestaurantTimeStatus = jsonObject.optString("RestaurantTimeStatus");
                            String RestaurantTimeStatus1 = jsonObject.optString("RestaurantTimeStatus1");
                            String restaurant_deliverycharge = jsonObject.optString("restaurant_deliverycharge");
                            String restaurant_minimumorder = jsonObject.optString("restaurant_minimumorder");
                            String restaurant_avarage_deliveryTime = jsonObject.optString("restaurant_avarage_deliveryTime");
                            String restaurant_avarage_PickupTime = jsonObject.optString("restaurant_avarage_PickupTime");
                            String restaurantCity = jsonObject.optString("restaurantCity");
                            String ratingAvg = jsonObject.optString("ratingAvg");
                            String restaurant_locality = jsonObject.optString("restaurant_locality");
                            String restaurant_Paypal_URL = jsonObject.optString("");
                            String restaurant_Paypal_bussiness_act = jsonObject.optString("");
                            String rating = jsonObject.optString("");
                            String ratingValue = jsonObject.optString("ratingValue");
                            String restaurant_about = jsonObject.optString("restaurant_about");
                            String featured = jsonObject.optString("featured");
                            String restaurant_deliveryDistance = jsonObject.optString("restaurant_deliveryDistance");
                            String restaurant_Logo = jsonObject.optString("restaurant_Logo");
                            String SocialSharingMessage = jsonObject.optString("SocialSharingMessage");
                            String restaurant_distance_check1 = jsonObject.getString("restaurant_distance_check");
                            String mealDealAvailable = jsonObject.getString("mealDealAvailable");
                            String RestaurantTimeStatusText = jsonObject.getString("RestaurantTimeStatusText");

                            int error = jsonObject.getInt("error");

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
                            restaurantModel.setWednesday(wednesday);
                            restaurantModel.setThursday(thursday);
                            restaurantModel.setFriday(friday);
                            restaurantModel.setSaturday(saturday);
                            restaurantModel.setSunday(sunday);
                            restaurantModel.setRestaurant_cuisine(restaurant_cuisine);
                            restaurantModel.setRatingValue(ratingValue);
                            restaurantModel.setTotalRestaurantReview(TotalRestaurantReview);
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
                            restaurantModel.setSocialSharingMessage(SocialSharingMessage);

                            /////////////icon show/////
                            restaurantModel.setBookaTable(bookatable);
                            restaurantModel.setKidFriendly(kidfriendly);
                            restaurantModel.setPetFriendly(petfriendly);
                            restaurantModel.setDINEin(dinein);
                            restaurantModel.setCollection(collection);
                            restaurantModel.setDelivery(delievery);
                            restaurantModel.setmealDealAvailable(mealDealAvailable);
                            restaurantModel.setrestaurant_distance_check(restaurant_distance_check1);
                            restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                            //////////

                            restaurantModelArrayList.add(restaurantModel);
                            Intent intent = (new Intent(CuisinesListActivity.this, RestaurantsListActivity.class));
                            intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                            intent.putExtra("passCode", passCode);
                            startActivity(intent);
                            finish();
                        } else {

                            String error = jsonObject.optString("error");
                            errorMsg = jsonObject.optString("error_msg");
                            emptyCuisineListDialog();
                            //Toast.makeText(getApplicationContext(), "" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("where", "" + HomeActivity.where_para);
                params.put("customerlocality", "" + HomeActivity.customerlocality_para);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }


    private void emptyCuisineListDialog() {
        new AlertDialog.Builder(CuisinesListActivity.this)
                .setTitle("Justfoodz")
                .setMessage(errorMsg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CuisinesListActivity.this, HomeActivity.class));
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).show();
    }
}