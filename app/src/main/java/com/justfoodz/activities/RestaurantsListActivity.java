package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.adapters.CuisinesAdapter;
import com.justfoodz.adapters.RestaurantListAdapter;

import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.fragments.Model_Reasurant;
import com.justfoodz.interfaces.RestaurantItemClickListener;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.CuisinesModel;
import com.justfoodz.models.FeatureAminitiesModel;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class RestaurantsListActivity extends AppCompatActivity implements View.OnClickListener, RestaurantItemClickListener {
    private final String TAG = "RestaurantList";
    private ImageView ivCuisines, ivBack;
    private RecyclerView rvRestaurantList;
    private TextView tvRestaurantName, city;
    public RestaurantListAdapter restaurantListAdapter;
    public RestaurantListAdapter restaurantListAdapter2;
    public RestaurantListAdapter restaurantListAdapter4;
    private AuthPreference authPreference;
    private EditText searchView;
    public ArrayList<RestaurantModel> restaurantModelArrayListReceived = null;
    public ArrayList<RestaurantModel> restaurantModelArrayListReceived2 = null;
    public ArrayList<RestaurantModel> restaurantModelArrayListReceived3 = null;


    private String passCode, CITYNAME;
    private ProgressDialog pDialog;
    private ArrayList<RestaurantModel> restaurantModelArrayList;
    private RestaurantModel restaurantModel;
    private RestaurantModel restaurantMode2;
    TextView filter;
    Dialog filterdialog;

    public static String res_lat, res_lng, card_avaible, cash_avaible;

    /////////////////cuisine bottom///////////////
    private RecyclerView rvCuisinesList;
    private CuisinesAdapterr cuisinesAdapter;
    private ArrayList<CuisinesModel> cuisinesModelArrayList;
    private CuisinesModel cuisinesModel;
    private String cuisine_name;
    ImageView downpopup;

    //////////feature & aminities/////////////
    private RecyclerView rv_featureaminities;
    private FeatureAminitiesAdapter featureAminitiesAdapter;
    private ArrayList<FeatureAminitiesModel> featureaminitieslist;
    private FeatureAminitiesModel featureAminitiesModel;

    ////////////////serving/////////////////
    private RecyclerView rv_serving;
    private ServingAdapter servingAdapter;
    private ArrayList<FeatureAminitiesModel> servinglist;


    ////////////////dress code/////////////////
    private RecyclerView rv_dresscode;
    private DressCodeAdapter dressCodeAdapter;
    private ArrayList<FeatureAminitiesModel> dresscodelist;

    ImageView uncheckedrating, checkedrating, checkedlatest, uncheckedlatest, uncheckeddistance, checkeddistance,
            checkedpopularity, uncheckedpopularity;

    String KidFriendlyAvailable = "", PetFriendlyAvailabl = "", HomeDeliverAvailable = "", PickupAvailable = "",
            BookaTablesupport = "", shopfeature = "", cashpaymentAvailable = "", onlinepaymentAvailable = "", sortBY = "",
            cuisine = "", facilities = "", service = "", dresscode = "";

    NestedScrollView nn;

    private int startPoint = 0, endPoint = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);
       statusBarColor();
        initialization();
        initializedListener();

        rvRestaurantList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    startPoint = startPoint + 15;
                    endPoint = endPoint + 15;
//                    restaurantModelArrayListReceived = new ArrayList<>();

                    searchFromPassCode();

                }
            }
        });

    }


    private void searchFromPassCode() {
        MyProgressDialog.show(this, R.string.please_wait);


        String customerLocality = "";
        String uree = " https://www.justfoodz.com/api-access/phpexpert_search.php?where=" + getIntent().getStringExtra("cityname") + "&customerlocality=" + customerLocality +
                "&KidFriendlyAvailable=" + KidFriendlyAvailable + "&PetFriendlyAvailabl=" + PetFriendlyAvailabl + "&HomeDeliverAvailable=" + HomeDeliverAvailable + "&PickupAvailable="
                + PickupAvailable + "&BookaTablesupport=" + BookaTablesupport + "&shopfeature=" + shopfeature + "&cashpaymentAvailable=" + cashpaymentAvailable +
                "&onlinepaymentAvailable=" + onlinepaymentAvailable + "&sortBY=" + sortBY + "&cuisine=" + cuisine + "&facilities=" + facilities + "&service=" + service +
                "&dresscode=" + dresscode + "&startPoint=" + startPoint + "&endPoint=" + endPoint + "";


        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.locationsearching(uree);
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {

                        RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                        if (restaurantModel.getError().equalsIgnoreCase("1")) {
                            restaurantModelArrayListReceived.add(restaurantModel);
                        }
                    }


                    if (restaurantModelArrayListReceived.size() > 0) {


                        updateReasutureantList();

                    } else {
                        restaurantModelArrayListReceived = new ArrayList<>();
                        updateReasutureantList();
                    }

                }
                int count = restaurantModelArrayListReceived.size();
                tvRestaurantName.setText(Integer.toString(count).concat(" ").concat("Justfoodz restaurants in").concat(""));
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
                Toast.makeText(RestaurantsListActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

                if (startPoint != 0) {
                    startPoint = startPoint - 15;
                    endPoint = endPoint - 15;
                }


                MyProgressDialog.dismiss();
            }
        });
    }

    private void updateReasutureantList() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvRestaurantList.setLayoutManager(mLayoutManager);
        rvRestaurantList.setItemAnimator(new DefaultItemAnimator());
        restaurantListAdapter = new RestaurantListAdapter(RestaurantsListActivity.this, restaurantModelArrayListReceived, this);
        rvRestaurantList.setAdapter(restaurantListAdapter);
        restaurantListAdapter.notifyDataSetChanged();
    }


    private void initialization() {
        restaurantModelArrayListReceived = new ArrayList<>();
        restaurantModelArrayList = new ArrayList<>();
        restaurantModelArrayListReceived.clear();
        restaurantModelArrayList.clear();
        ivBack = findViewById(R.id.iv_back);
        searchView = findViewById(R.id.search_view);
        filter = findViewById(R.id.filter);
        ivCuisines = findViewById(R.id.iv_cuisines);
        tvRestaurantName = findViewById(R.id.tv_restaurant_name);
        city = findViewById(R.id.city);
        ////////////////////////cuisine//////////

        authPreference = new AuthPreference(this);
        passCode = getIntent().getStringExtra("passCode");

        authPreference = new AuthPreference(this);
//        city.setText(authPreference.getCityName());
        try {
            passCode = getIntent().getStringExtra("passCode");
            CITYNAME = getIntent().getStringExtra("cityname");
            city.setText(CITYNAME);
            restaurantModelArrayListReceived = (ArrayList<RestaurantModel>) getIntent().getSerializableExtra("restaurantModelArrayList");
            int count = restaurantModelArrayListReceived.size();
            tvRestaurantName.setText(Integer.toString(count).concat(" ").concat("Justfoodz restaurants in").concat(""));
            rvRestaurantList = findViewById(R.id.rv_restaurantList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rvRestaurantList.setLayoutManager(mLayoutManager);
            rvRestaurantList.setItemAnimator(new DefaultItemAnimator());
            restaurantListAdapter = new RestaurantListAdapter(this, restaurantModelArrayListReceived, this);
            rvRestaurantList.setAdapter(restaurantListAdapter);
            restaurantListAdapter.setRestaurantList(getApplicationContext(), restaurantModelArrayListReceived);
            restaurantModelArrayListReceived3 = restaurantModelArrayListReceived;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.equals("")) {
                    restaurantModelArrayListReceived = (ArrayList<RestaurantModel>) getIntent().getSerializableExtra("restaurantModelArrayList");
                    rvRestaurantList.removeAllViewsInLayout();
                    restaurantListAdapter = new RestaurantListAdapter(RestaurantsListActivity.this, restaurantModelArrayListReceived, RestaurantsListActivity.this);
                    rvRestaurantList.setAdapter(restaurantListAdapter);
                    restaurantListAdapter.notifyDataSetChanged();
                    restaurantListAdapter.setRestaurantList(getApplicationContext(), restaurantModelArrayListReceived);
                    restaurantModelArrayListReceived3 = restaurantModelArrayListReceived;
                } else {

                    restaurantModelArrayListReceived2 = new ArrayList<>();
                    restaurantModelArrayListReceived2.clear();
                    for (int i = 0; i < restaurantModelArrayListReceived.size(); i++) {
                        if (restaurantModelArrayListReceived.get(i).getRestaurant_name().toLowerCase().contains(s)) {
                            restaurantMode2 = new RestaurantModel();
                            restaurantMode2.setId(restaurantModelArrayListReceived.get(i).getId());
                            restaurantMode2.setRestaurant_address(restaurantModelArrayListReceived.get(i).getRestaurant_address());
                            restaurantMode2.setRestaurant_name(restaurantModelArrayListReceived.get(i).getRestaurant_name());
                            restaurantMode2.setRestaurant_Logo(restaurantModelArrayListReceived.get(i).getRestaurant_Logo());
                            restaurantMode2.setRestaurant_minimumorder(restaurantModelArrayListReceived.get(i).getRestaurant_minimumorder());
                            restaurantMode2.setRestaurantOfferPrice(restaurantModelArrayListReceived.get(i).getRestaurantOfferPrice());
                            restaurantMode2.setRestaurantTimeStatus1(restaurantModelArrayListReceived.get(i).getRestaurantTimeStatus1());
                            restaurantMode2.setRestaurant_contact_phone(restaurantModelArrayListReceived.get(i).getRestaurant_contact_phone());
                            restaurantMode2.setRestaurant_contact_mobile(restaurantModelArrayListReceived.get(i).getRestaurant_contact_mobile());
                            restaurantMode2.setRestaurant_contact_email(restaurantModelArrayListReceived.get(i).getRestaurant_contact_email());
                            restaurantMode2.setRestaurant_deliveryDistance(restaurantModelArrayListReceived.get(i).getRestaurant_deliveryDistance());
                            restaurantMode2.setRestaurant_deliverycharge(restaurantModelArrayListReceived.get(i).getRestaurant_deliverycharge());
                            restaurantMode2.setRestaurant_about(restaurantModelArrayListReceived.get(i).getRestaurant_about());
                            restaurantMode2.setMonday(restaurantModelArrayListReceived.get(i).getMonday());
                            restaurantMode2.setTuesday(restaurantModelArrayListReceived.get(i).getTuesday());
                            restaurantMode2.setWednesday(restaurantModelArrayListReceived.get(i).getWednesday());
                            restaurantMode2.setThursday(restaurantModelArrayListReceived.get(i).getThursday());
                            restaurantMode2.setFriday(restaurantModelArrayListReceived.get(i).getFriday());
                            restaurantMode2.setSaturday(restaurantModelArrayListReceived.get(i).getSaturday());
                            restaurantMode2.setSunday(restaurantModelArrayListReceived.get(i).getSunday());
                            restaurantMode2.setRestaurant_cuisine(restaurantModelArrayListReceived.get(i).getRestaurant_cuisine());
                            restaurantMode2.setRatingValue(restaurantModelArrayListReceived.get(i).getRatingValue());
                            restaurantMode2.setRatingAvg(restaurantModelArrayListReceived.get(i).getRatingAvg());

                            restaurantMode2.setHomeDeliveryStatus(restaurantModelArrayListReceived.get(i).getHomeDeliveryStatus());
                            restaurantMode2.setPickupStatus(restaurantModelArrayListReceived.get(i).getPickupStatus());
                            restaurantMode2.setPre_homeDeliveryStatus(restaurantModelArrayListReceived.get(i).getPre_homeDeliveryStatus());
                            restaurantMode2.setPre_Order_PickupStatus(restaurantModelArrayListReceived.get(i).getPre_Order_PickupStatus());
                            //Cash Accept
                            restaurantMode2.setRestaurant_onlycashon(restaurantModelArrayListReceived.get(i).getRestaurant_onlycashon());
                            //card Accept
                            restaurantMode2.setRestaurant_cardaccept(restaurantModelArrayListReceived.get(i).getRestaurant_cardaccept());
                            restaurantMode2.setRestaurant_LatitudePoint(restaurantModelArrayListReceived.get(i).getRestaurant_LatitudePoint());
                            restaurantMode2.setRestaurant_LongitudePoint(restaurantModelArrayListReceived.get(i).getRestaurant_LongitudePoint());
                            restaurantMode2.setRestaurant_cardacceptAvailable(restaurantModelArrayListReceived.get(i).getRestaurant_cardacceptAvailable());
                            restaurantMode2.setRestaurant_onlycashonAvailable(restaurantModelArrayListReceived.get(i).getRestaurant_onlycashonAvailable());
                            restaurantMode2.setBookaTable(restaurantModelArrayListReceived.get(i).getBookaTable());
                            restaurantMode2.setKidFriendly(restaurantModelArrayListReceived.get(i).getKidFriendly());
                            restaurantMode2.setPetFriendly(restaurantModelArrayListReceived.get(i).getPetFriendly());
                            restaurantMode2.setDINEin(restaurantModelArrayListReceived.get(i).getDINEin());
                            restaurantMode2.setCollection(restaurantModelArrayListReceived.get(i).getCollection());
                            restaurantMode2.setDelivery(restaurantModelArrayListReceived.get(i).getDelivery());
                            restaurantMode2.setmealDealAvailable(restaurantModelArrayListReceived.get(i).getmealDealAvailable());
                            restaurantMode2.setRestaurantTimeStatusText(restaurantModelArrayListReceived.get(i).getRestaurantTimeStatusText());
                            restaurantMode2.setTotalRestaurantReview(restaurantModelArrayListReceived.get(i).getTotalRestaurantReview());
                            restaurantModelArrayListReceived2.add(restaurantMode2);
                        }
                    }
                    restaurantListAdapter2 = new RestaurantListAdapter(RestaurantsListActivity.this, restaurantModelArrayListReceived2, RestaurantsListActivity.this);
                    rvRestaurantList.removeAllViewsInLayout();
                    rvRestaurantList.setAdapter(restaurantListAdapter2);
                    //restaurantListAdapter2.notifyDataSetChanged();
                    restaurantListAdapter2.setRestaurantList(getApplicationContext(), restaurantModelArrayListReceived2);
                    //restaurantModelArrayListReceived=restaurantModelArrayListReceived2;
                    restaurantModelArrayListReceived3 = restaurantModelArrayListReceived2;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void DresscodeList() {
        pDialog = new ProgressDialog(RestaurantsListActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        dresscodelist = new ArrayList<FeatureAminitiesModel>();
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_DressCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("DressCodeSearchList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String restaurantfeature = jsonObject.getString("RestaurantServiceName");
                        String service_img = jsonObject.getString("service_img");
                        featureAminitiesModel = new FeatureAminitiesModel();
                        featureAminitiesModel.setRestaurantfeaturename(restaurantfeature);
                        dresscodelist.add(featureAminitiesModel);
                    }
                    dressCodeAdapter = new DressCodeAdapter(RestaurantsListActivity.this, dresscodelist);
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_dresscode.setLayoutManager(horizontalLayoutManager1);
                    rv_dresscode.setAdapter(dressCodeAdapter);
                    rv_dresscode.setNestedScrollingEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantsListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsListActivity.this);
        requestQueue.add(strReq);
    }

    public void ServingList() {
        servinglist = new ArrayList<FeatureAminitiesModel>();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_Serving, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("ServingSearchList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String restaurantfeature = jsonObject.getString("RestaurantServiceName");
                        String service_img = jsonObject.getString("service_img");
                        featureAminitiesModel = new FeatureAminitiesModel();
                        featureAminitiesModel.setRestaurantfeaturename(restaurantfeature);
                        servinglist.add(featureAminitiesModel);
                    }
                    servingAdapter = new ServingAdapter(RestaurantsListActivity.this, servinglist);
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_serving.setLayoutManager(horizontalLayoutManager1);
                    rv_serving.setAdapter(servingAdapter);
                    rv_serving.setNestedScrollingEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantsListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsListActivity.this);
        requestQueue.add(strReq);
    }

    public void FeatureAminitiesList() {
        featureaminitieslist = new ArrayList<FeatureAminitiesModel>();

        MyProgressDialog.show(RestaurantsListActivity.this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_AmenitiesSearchList> userLoginCall = interfaceRetrofit
                .phpexpert_quick_food_Amenities("https://www.justfoodz.com/api-access/phpexpert_quick_food_Amenities.php");
        userLoginCall.enqueue(new Callback<Model_AmenitiesSearchList>() {
            @Override
            public void onResponse(Call<Model_AmenitiesSearchList> call, retrofit2.Response<Model_AmenitiesSearchList> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getAmenitiesSearch().size(); i++) {
                        FeatureAminitiesModel featureAminitiesModel = response.body().getAmenitiesSearch().get(i);
                        featureaminitieslist.add(featureAminitiesModel);
                    }

                    if (featureaminitieslist.size() > 0) {
                        featureAminitiesAdapter = new FeatureAminitiesAdapter(RestaurantsListActivity.this, featureaminitieslist);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_featureaminities.setLayoutManager(horizontalLayoutManager1);
                        rv_featureaminities.setAdapter(featureAminitiesAdapter);
                        rv_featureaminities.setNestedScrollingEnabled(false);
                    }

                }


                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_AmenitiesSearchList> call, Throwable t) {
                Toast.makeText(RestaurantsListActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

/*
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_FeatureAMinities, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("AmenitiesSearchList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String restaurantfeaturename = jsonObject.getString("RestaurantServiceName");
                        String service_img = jsonObject.getString("service_img");
                        featureAminitiesModel = new FeatureAminitiesModel();
                        featureAminitiesModel.setRestaurantfeaturename(restaurantfeaturename);
                        featureaminitieslist.add(featureAminitiesModel);


                    }
                    featureAminitiesAdapter = new FeatureAminitiesAdapter(RestaurantsListActivity.this, featureaminitieslist);
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_featureaminities.setLayoutManager(horizontalLayoutManager1);
                    rv_featureaminities.setAdapter(featureAminitiesAdapter);
                    rv_featureaminities.setNestedScrollingEnabled(false);


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantsListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsListActivity.this);
        requestQueue.add(strReq);*/
    }

    public void cuisinesList() {
        cuisinesModelArrayList = new ArrayList<CuisinesModel>();

        MyProgressDialog.show(RestaurantsListActivity.this, R.string.please_wait);

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
                        cuisinesAdapter = new CuisinesAdapterr(RestaurantsListActivity.this, cuisinesModelArrayList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                        rvCuisinesList.setLayoutManager(horizontalLayoutManager1);
                        rvCuisinesList.setAdapter(cuisinesAdapter);
                        rvCuisinesList.setNestedScrollingEnabled(false);
                    }

                    MyProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Model_CuisinesListing> call, Throwable t) {
                Toast.makeText(RestaurantsListActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

                MyProgressDialog.dismiss();
            }
        });





       /* StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_CUISINES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                    cuisinesAdapter = new CuisinesAdapterr(RestaurantsListActivity.this, cuisinesModelArrayList);
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(RestaurantsListActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvCuisinesList.setLayoutManager(horizontalLayoutManager1);
                    rvCuisinesList.setAdapter(cuisinesAdapter);
                    rvCuisinesList.setNestedScrollingEnabled(false);


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantsListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsListActivity.this);
        requestQueue.add(strReq);*/
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        ivCuisines.setOnClickListener(this);
        filter.setOnClickListener(this);
    }

    @Override
    public void restaurantItemClick(int position) {
        try {
            if (restaurantModelArrayListReceived3.get(position).getRestaurantTimeStatus1().equals("Closed") || restaurantModelArrayListReceived3.get(position).getRestaurantTimeStatus1().equals("Busy")) {
                showCustomDialog1decline(restaurantModelArrayListReceived3.get(position).getRestaurantTimeStatusText());
            } else {
                res_lat = restaurantModelArrayListReceived3.get(position).getRestaurant_LatitudePoint();
                res_lng = restaurantModelArrayListReceived3.get(position).getRestaurant_LongitudePoint();
                cash_avaible = restaurantModelArrayListReceived3.get(position).getRestaurant_onlycashonAvailable();
                card_avaible = restaurantModelArrayListReceived3.get(position).getRestaurant_cardacceptAvailable();
                HomeFragment.restaurant_distance_check = restaurantModelArrayListReceived3.get(position).getrestaurant_distance_check();
                Intent intent = new Intent(this, MainMenuActivity.class);
                String id = restaurantModelArrayListReceived3.get(position).getId();
                RestaurantModel restaurantModel = restaurantModelArrayListReceived3.get(position);
                authPreference.setRestraurantCity("" + restaurantModelArrayListReceived3.get(position).getRestaurantCity());
                authPreference.setMinprice(restaurantModelArrayListReceived3.get(position).getRestaurant_minimumorder());
                intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                intent.putExtra("restaurantModelArrayListReceived", restaurantModelArrayListReceived3);
                intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                intent.putExtra("Booktable", restaurantModel.getBookaTable());
                startActivity(intent);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                startActivity(new Intent(RestaurantsListActivity.this, HomeActivity.class));
                break;
            case R.id.iv_cuisines:
                Intent intent = new Intent(RestaurantsListActivity.this, CuisinesListActivity.class);
                intent.putExtra("passCode", passCode);
                intent.putExtra("cityname", getIntent().getStringExtra("cityname") );
                startActivity(intent);
                break;
            case R.id.filter:
                showFilterDailogPopup();
            default:
                break;
        }
    }

    public void showFilterDailogPopup() {
        filterdialog = new Dialog(this);
        filterdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filterdialog.setContentView(R.layout.filterdialogbottompopup);
        filterdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        filterdialog.getWindow().setGravity(Gravity.BOTTOM);
        filterdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        filterdialog.setCanceledOnTouchOutside(true);

        rvCuisinesList = filterdialog.findViewById(R.id.rv_cuisinesList);
        rv_featureaminities = filterdialog.findViewById(R.id.rv_featureaminities);
        rv_serving = filterdialog.findViewById(R.id.rv_serving);
        rv_dresscode = filterdialog.findViewById(R.id.rv_dresscode);
        downpopup = filterdialog.findViewById(R.id.downpopup);
        nn = filterdialog.findViewById(R.id.nn);
        nn.setFocusableInTouchMode(true);
        nn.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        final RadioButton rbkid, rbpet, rbdelivery, rbcollection, rbbooktable, rbfeature, rbcash, rbonline;
        LinearLayout llrate, llnew, lldistance, llatoz;
        TextView txtapply, txtreset;
        txtapply = filterdialog.findViewById(R.id.txtapply);
        txtreset = filterdialog.findViewById(R.id.txtreset);
        rbkid = filterdialog.findViewById(R.id.rbkid);
        rbpet = filterdialog.findViewById(R.id.rbpet);
        rbdelivery = filterdialog.findViewById(R.id.rbdelivery);
        rbcollection = filterdialog.findViewById(R.id.rbcollection);
        rbbooktable = filterdialog.findViewById(R.id.rbbooktable);
        rbfeature = filterdialog.findViewById(R.id.rbfeature);
        rbcash = filterdialog.findViewById(R.id.rbcash);
        rbonline = filterdialog.findViewById(R.id.rbonline);
//        llrate = filterdialog.findViewById(R.id.llrate);
//        llnew = filterdialog.findViewById(R.id.llnew);
//        lldistance = filterdialog.findViewById(R.id.lldistance);
//        llatoz = filterdialog.findViewById(R.id.llatoz);

        uncheckedrating = filterdialog.findViewById(R.id.uncheckedrating);
        checkedrating = filterdialog.findViewById(R.id.checkedrating);
        checkedlatest = filterdialog.findViewById(R.id.checkedlatest);
        uncheckedlatest = filterdialog.findViewById(R.id.uncheckedlatest);
        uncheckeddistance = filterdialog.findViewById(R.id.uncheckeddistance);
        checkeddistance = filterdialog.findViewById(R.id.checkeddistance);
        checkedpopularity = filterdialog.findViewById(R.id.checkedpopularity);
        uncheckedpopularity = filterdialog.findViewById(R.id.uncheckedpopularity);

//        KidFriendlyAvailable="";PetFriendlyAvailabl="";HomeDeliverAvailable="";PickupAvailable="";
//        BookaTablesupport="";shopfeature="";cashpaymentAvailable="";onlinepaymentAvailable="";
//        sortBY="";cuisine="";facilities="";service="";dresscode="";

        uncheckedrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckedrating.setVisibility(View.GONE);
                checkedrating.setVisibility(View.VISIBLE);
                sortBY = "avgrating";
            }
        });
        checkedrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedrating.setVisibility(View.GONE);
                uncheckedrating.setVisibility(View.VISIBLE);
            }
        });

        uncheckedlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckedlatest.setVisibility(View.GONE);
                checkedlatest.setVisibility(View.VISIBLE);
                sortBY = "new";
            }
        });
        checkedlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedlatest.setVisibility(View.GONE);
                uncheckedlatest.setVisibility(View.VISIBLE);
            }
        });

        uncheckeddistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckeddistance.setVisibility(View.GONE);
                checkeddistance.setVisibility(View.VISIBLE);
                sortBY = "distance";
            }
        });
        checkeddistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkeddistance.setVisibility(View.GONE);
                uncheckeddistance.setVisibility(View.VISIBLE);
            }
        });
        uncheckedpopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckedpopularity.setVisibility(View.GONE);
                checkedpopularity.setVisibility(View.VISIBLE);
                sortBY = "atoz";
            }
        });
        checkedpopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedpopularity.setVisibility(View.GONE);
                uncheckedpopularity.setVisibility(View.VISIBLE);
            }
        });

        txtreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KidFriendlyAvailable = "";
                PetFriendlyAvailabl = "";
                HomeDeliverAvailable = "";
                PickupAvailable = "";
                BookaTablesupport = "";
                shopfeature = "";
                cashpaymentAvailable = "";
                onlinepaymentAvailable = "";
                sortBY = "";
                cuisine = "";
                facilities = "";
                service = "";
                dresscode = "";
                filterdialog.dismiss();
                restaurantModelArrayListReceived = new ArrayList<>();
                searchFromPassCode();
            }
        });

        txtapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbkid.isChecked()) {
                    KidFriendlyAvailable = "0";
                } else {
                    KidFriendlyAvailable = "1";
                }
                if (rbpet.isChecked()) {
                    PetFriendlyAvailabl = "0";
                } else {
                    PetFriendlyAvailabl = "1";
                }
                if (rbdelivery.isChecked()) {
                    HomeDeliverAvailable = "0";
                } else {
                    HomeDeliverAvailable = "1";
                }
                if (rbcollection.isChecked()) {
                    PickupAvailable = "0";
                } else {
                    PickupAvailable = "1";
                }
                if (rbbooktable.isChecked()) {
                    BookaTablesupport = "1";
                } else {
                    BookaTablesupport = "0";
                }
                if (rbfeature.isChecked()) {
                    shopfeature = "1";
                } else {
                    shopfeature = "0";
                }
                if (rbcash.isChecked()) {
                    cashpaymentAvailable = "0";
                } else {
                    cashpaymentAvailable = "1";
                }
                if (rbonline.isChecked()) {
                    onlinepaymentAvailable = "0";
                } else {
                    onlinepaymentAvailable = "1";
                }
                filterdialog.dismiss();
                restaurantModelArrayListReceived = new ArrayList<>();
                searchFromPassCode();

            }
        });

        downpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterdialog.dismiss();
            }
        });

        if (Network.isNetworkCheck(this)) {
            cuisinesList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        if (Network.isNetworkCheck(this)) {
            DresscodeList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        if (Network.isNetworkCheck(this)) {
            ServingList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        if (Network.isNetworkCheck(this)) {
            FeatureAminitiesList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        filterdialog.show();
    }

    public class DressCodeAdapter extends RecyclerView.Adapter<DressCodeAdapter.ViewHolder> {

        Context context;
        private ArrayList<FeatureAminitiesModel> dresscodelist;
        private int lastSelectedPosition = -1;
        RadioButton globalradio = null;

        public DressCodeAdapter(Context c, ArrayList<FeatureAminitiesModel> dresscodelist) {
            this.context = c;
            this.dresscodelist = dresscodelist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.filteritemcuisine, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_cuisines_name.setText(dresscodelist.get(position).getRestaurantfeaturename());
            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton checked_rb = (RadioButton) view;
                    String a = dresscodelist.get(position).getRestaurantfeaturename();
                    if (globalradio != null) {
                        globalradio.setChecked(false);
                    }
                    dresscode = a;
                    globalradio = checked_rb;
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return dresscodelist.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_cuisines_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;


            public ViewHolder(View itemView) {
                super(itemView);
                tv_cuisines_name = (TextView) itemView.findViewById(R.id.tv_cuisines_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);


            }
        }
    }

    public class ServingAdapter extends RecyclerView.Adapter<ServingAdapter.ViewHolder> {

        Context context;
        private ArrayList<FeatureAminitiesModel> servinglist;
        RadioButton globalradio = null;

        public ServingAdapter(Context c, ArrayList<FeatureAminitiesModel> servinglist) {
            this.context = c;
            this.servinglist = servinglist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.filteritemcuisine, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_cuisines_name.setText(servinglist.get(position).getRestaurantfeaturename());
            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton checked_rb = (RadioButton) view;
                    String a = servinglist.get(position).getRestaurantfeaturename();
                    if (globalradio != null) {
                        globalradio.setChecked(false);
                    }
                    service = a;
                    globalradio = checked_rb;
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return servinglist.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_cuisines_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;


            public ViewHolder(View itemView) {
                super(itemView);
                tv_cuisines_name = (TextView) itemView.findViewById(R.id.tv_cuisines_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);


            }
        }
    }

    public class FeatureAminitiesAdapter extends RecyclerView.Adapter<FeatureAminitiesAdapter.ViewHolder> {

        Context context;
        private ArrayList<FeatureAminitiesModel> featureaminitieslist;
        RadioButton globalradio = null;

        public FeatureAminitiesAdapter(Context c, ArrayList<FeatureAminitiesModel> featureaminitieslist) {
            this.context = c;
            this.featureaminitieslist = featureaminitieslist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.filteritemcuisine, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_cuisines_name.setText(featureaminitieslist.get(position).getRestaurantServiceName());
            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton checked_rb = (RadioButton) view;
                    String a = featureaminitieslist.get(position).getRestaurantServiceName();
                    if (globalradio != null) {
                        globalradio.setChecked(false);
                    }
                    facilities = a;
                    globalradio = checked_rb;
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return featureaminitieslist.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_cuisines_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_cuisines_name = (TextView) itemView.findViewById(R.id.tv_cuisines_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);
            }
        }
    }

    public class CuisinesAdapterr extends RecyclerView.Adapter<CuisinesAdapterr.ViewHolder> {

        Context context;
        private ArrayList<CuisinesModel> cuisinesModelArrayList;
        RadioButton globalradio = null;


        public CuisinesAdapterr(Context c, ArrayList<CuisinesModel> cuisinesModelArrayList) {
            this.context = c;
            this.cuisinesModelArrayList = cuisinesModelArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.filteritemcuisine, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_cuisines_name.setText(cuisinesModelArrayList.get(position).getCuisine_name());


            holder.citychecked.setOnClickListener(view -> {
                RadioButton checked_rb = (RadioButton) view;
                String a = cuisinesModelArrayList.get(position).getCuisine_name();
                if (globalradio != null) {
                    globalradio.setChecked(false);
                }
                cuisine = a;
                globalradio = checked_rb;
            });

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return cuisinesModelArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_cuisines_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;


            public ViewHolder(View itemView) {
                super(itemView);
                tv_cuisines_name = itemView.findViewById(R.id.tv_cuisines_name);
                cuisinesLayout = itemView.findViewById(R.id.cuisines_layout);
                citychecked = itemView.findViewById(R.id.citychecked);
            }
        }
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
        startActivity(new Intent(RestaurantsListActivity.this, HomeActivity.class));
        super.onBackPressed();
    }

    private void searchFromPassCodes() {
        restaurantModelArrayList.clear();
        pDialog = new ProgressDialog(RestaurantsListActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
        restaurantModelArrayList = new ArrayList<RestaurantModel>();
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
                Log.e("homere", "" + response);
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
                            String restaurant_distance_check = jsonObject.getString("restaurant_distance_check");
                            String mealDealAvailable = jsonObject.optString("mealDealAvailable");
                            String RestaurantTimeStatusText = jsonObject.optString("RestaurantTimeStatusText");
                            String DineInAvailable = jsonObject.getString("DineInAvailable");
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
                            restaurantModel.setrestaurant_distance_check(restaurant_distance_check);
                            /////////////icon show/////
                            restaurantModel.setBookaTable(bookatable);
                            restaurantModel.setKidFriendly(kidfriendly);
                            restaurantModel.setPetFriendly(petfriendly);
                            restaurantModel.setDINEin(dinein);
                            restaurantModel.setCollection(collection);
                            restaurantModel.setDelivery(delievery);
                            restaurantModel.setmealDealAvailable(mealDealAvailable);
                            restaurantModel.setDineInAvailable(DineInAvailable);
                            restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                            //////////

                            restaurantModelArrayList.add(restaurantModel);

                            Intent intent = (new Intent(RestaurantsListActivity.this, RestaurantsListActivity.class));
                            intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                            intent.putExtra("passCode", passCode);
                            intent.putExtra("cityname", getIntent().getStringExtra("cityname"));
                            startActivity(intent);
                            finish();

                        } else {
                            String errorMsg = jsonObject.optString("error_msg");
                            Toast.makeText(RestaurantsListActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                            showFilterDailogPopup();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to Search

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", "" + getIntent().getStringExtra("cityname"));
                params.put("customerlocality", "");
                params.put("KidFriendlyAvailable", "" + KidFriendlyAvailable);
                params.put("PetFriendlyAvailabl", "" + PetFriendlyAvailabl);
                params.put("HomeDeliverAvailable", "" + HomeDeliverAvailable);
                params.put("PickupAvailable", "" + PickupAvailable);
                params.put("BookaTablesupport", "" + BookaTablesupport);
                params.put("shopfeature", "" + shopfeature);
                params.put("cashpaymentAvailable", "" + cashpaymentAvailable);
                params.put("onlinepaymentAvailable", "" + onlinepaymentAvailable);
                params.put("sortBY", "" + sortBY);
                params.put("cuisine", "" + cuisine);
                params.put("facilities", "" + facilities);
                params.put("service", "" + service);
                params.put("dresscode", "" + dresscode);
                Log.e("params", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsListActivity.this);
        requestQueue.add(strReq);
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(RestaurantsListActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}