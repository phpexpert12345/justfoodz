package com.justfoodz.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.gson.Gson;
import com.justfoodz.Database.Database;
import com.justfoodz.MyApplication;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.activities.AllMealofDayActivity;
import com.justfoodz.activities.HomeActivity;
import com.justfoodz.activities.MainMenuActivity;
import com.justfoodz.activities.Model_CityListing;
import com.justfoodz.activities.QuickSearchLocation;

import com.justfoodz.activities.Ravifinalitem;
import com.justfoodz.activities.RestaurantsListActivity;
import com.justfoodz.activities.SliderImage;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.activities.SubMenuActivity;
import com.justfoodz.activities.TrackGPS;
import com.justfoodz.adapters.FreeDeliveryAdapter;
import com.justfoodz.adapters.MyAdapter;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.CuisineModel;

import com.justfoodz.models.FeaturedDataModel;
import com.justfoodz.models.FeaturedPartnerModel;
import com.justfoodz.models.PlaceAutoComplete;
import com.justfoodz.models.PlacePredictions;
import com.justfoodz.models.RestaurantFreeDeliveryModel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.DatabaseHelper;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.justfoodz.utils.VolleyJSONRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView tvSearch, city, feature, meal, popular, freedelivery;
    private EditText edtPassCode;
    private ProgressDialog pDialog, pd, pdialog, progress, pddd;
    private AuthPreference authPreference;
    public static String passCode;
    public ArrayList<RestaurantModel> restaurantModelArrayList;
    public static RestaurantModel restaurantModel;
    public static RestaurantFreeDeliveryModel restaurantFreeDeliveryModel;
    private Spinner selectcity;
    ArrayList<String> cityArray;
    public static String CITY;
    LinearLayout citydialog, localitydialog, placedialog, llbanner;
    TextView edt_placesearch;
    Dialog dialog, dialog1;
    private Citymodel citymodel;
    EditText search_view;
    private ArrayList<Citymodel> searchList;
    TrackGPS gps;
    String strlatitutee = "";
    String strlongitutee = "";

    public static String restaurant_distance_check;

    public ArrayList<RestaurantModel> restaurantModelArrayListReceived = null;
    public ArrayList<RestaurantFreeDeliveryModel> restaurantFreeDeliveryModelArrayListReceived = null;

    ///////////banner////////////
    private ViewPager viewPagerslider;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.banner, R.drawable.banner, R.drawable.banner};
    private ArrayList<String> XMENArray = new ArrayList<String>();
    CircleIndicator indicator;
    ////////////////////////////

    /////////featured partners///////////
    private RecyclerView featuredimagerecycler;
    private ArrayList<FeaturedDataModel> featuredimageList;

    /////////delievered by us///////////
    private RecyclerView delieveredrecycler;
    private ArrayList<CuisineModel> delieveredCuisineList;

    /////////Quick searchs///////////
    private RecyclerView quickrecycler;
    private ArrayList<FeaturedPartnerModel> quickimageList;

    /////////City recycler///////////
    private RecyclerView cityrecycler;
    private ArrayList<Citymodel> CityList;
    private CITYAdapter cityAdapter;

    /////////popular partners///////////
    private RecyclerView popularnearrecycler, freedeliveryrecycler;
    private ArrayList<RestaurantModel> popularnearList;
//    private PopularNearAdapter popularNearAdapter;

    /////////Deals recycler///////////
    private RecyclerView dealsrecycler;
    private ArrayList<RestaurantModel> DealsList;
    CharSequence currentdate;
    TextView viewall;
    RequestQueue requestQueue;

    private VolleyJSONRequest request;
    private Handler handler1;
    private PlacePredictions predictions;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private DatabaseHelper databaseHelper;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    MyPref myPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myPref = new MyPref(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        city = view.findViewById(R.id.city);
        quickrecycler = view.findViewById(R.id.quickrecycler);
        delieveredrecycler = view.findViewById(R.id.delieveredrecycler);
        featuredimagerecycler = view.findViewById(R.id.featuredrecycler);
        dealsrecycler = view.findViewById(R.id.dealsrecycler);
        viewall = view.findViewById(R.id.viewall);
        viewPagerslider = view.findViewById(R.id.myprofile_viewpager);
        indicator = view.findViewById(R.id.indicator);
        feature = view.findViewById(R.id.feature);
        meal = view.findViewById(R.id.meal);
        freedeliveryrecycler = view.findViewById(R.id.freedeliveryrecycler);
        freedelivery = view.findViewById(R.id.freedelivery);

        popularnearrecycler = view.findViewById(R.id.popularnearrecycler);
        popular = view.findViewById(R.id.popular);

        getData();
        Date d = new Date();
        currentdate = DateFormat.format("yyyy-MM-dd", d.getTime());

        citydialog = view.findViewById(R.id.citydialog);
        localitydialog = view.findViewById(R.id.localitydialog);
        placedialog = view.findViewById(R.id.placedialog);
        llbanner = view.findViewById(R.id.llbanner);
        edt_placesearch = view.findViewById(R.id.edt_placesearch);

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllMealofDayActivity.class);
                startActivity(intent);
            }
        });

        if (checkAndRequestPermissions()) {
        }
        gps = new TrackGPS(getActivity());

        try {
            gps = new TrackGPS(getActivity());
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                strlongitutee = String.valueOf(latitude);
                strlatitutee = String.valueOf(longitude);

            } else {
                gps.showSettingsAlert();
            }
        } catch (Exception er) {
            Log.d("Error", er.toString());
        }// create class object

        if (Network.isNetworkCheck(getActivity())) {
            HitURLforFeaturedpartner();
            HitUrlMealDeals();
            HitURLforCuisineDisplay();
            HitURLforPopularNear();
            HitURLforFreeDelivery();
            HitURLforQuickSearchData();


        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

        cityArray = new ArrayList<>();
        quickimageList = new ArrayList<>();
        delieveredCuisineList = new ArrayList<>();
        featuredimageList = new ArrayList<>();
        DealsList = new ArrayList<>();
        CityList = new ArrayList<>();
        searchList = new ArrayList<>();
        popularnearList = new ArrayList<>();

        citydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingPoup();
            }
        });


        placedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleSearch();
            }
        });

        /////////////bannercode///////////////////

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                viewPagerslider.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 3500);


        initialization(view);
        initializedListener(view);


        if (myPref.getReferId().equals("2")) {
            placedialog.setVisibility(View.GONE);
            localitydialog.setVisibility(View.VISIBLE);
            citydialog.setVisibility(View.VISIBLE);
        } else {
            placedialog.setVisibility(View.VISIBLE);
            edt_placesearch.setText("" + myPref.getPickupAdd());
            localitydialog.setVisibility(View.GONE);
            citydialog.setVisibility(View.GONE);
        }

    }

    public void HitURLforFreeDelivery() {

        restaurantFreeDeliveryModelArrayListReceived = new ArrayList<RestaurantFreeDeliveryModel>();
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Free_Delivery_Reasurant> userLoginCall = interfaceRetrofit.hitURLforFreeDelivery(myPref.getState(), myPref.getCity(),
                myPref.getLatitude(), myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_Free_Delivery_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Free_Delivery_Reasurant> call, retrofit2.Response<Model_Free_Delivery_Reasurant> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        if (!response.body().getSearchResult().get(i).getError().equalsIgnoreCase("2")) {

                            RestaurantFreeDeliveryModel restaurantFreeDeliveryModel = response.body().getSearchResult().get(i);
                            meal.setVisibility(View.VISIBLE);
                            dealsrecycler.setVisibility(View.VISIBLE);

                            HomeFragment.restaurantFreeDeliveryModel = restaurantFreeDeliveryModel;
                            restaurantFreeDeliveryModelArrayListReceived.add(HomeFragment.restaurantFreeDeliveryModel);


                        }
                    }

                    if (restaurantModelArrayListReceived.size() > 0) {
                        FreeDeliveryAdapter freeDeliveryAdapter = new FreeDeliveryAdapter(getActivity(), restaurantFreeDeliveryModelArrayListReceived, CITY);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        freedeliveryrecycler.setLayoutManager(horizontalLayoutManager1);
                        freedeliveryrecycler.setAdapter(freeDeliveryAdapter);
                    } else {
                        meal.setVisibility(View.GONE);
                        viewall.setVisibility(View.GONE);
                        dealsrecycler.setVisibility(View.GONE);
                    }

                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_Free_Delivery_Reasurant> call, Throwable t) {
//                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();

                MyProgressDialog.dismiss();

            }
        });


    }

    public void HitURLforPopularNear() {

        restaurantModelArrayListReceived = new ArrayList<RestaurantModel>();
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.hitURLforPopularNear(myPref.getState(), myPref.getCity(),
                myPref.getLatitude(), myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        if (!response.body().getSearchResult().get(i).getError().equalsIgnoreCase("2")) {

                            RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                            meal.setVisibility(View.VISIBLE);
                            dealsrecycler.setVisibility(View.VISIBLE);

                            HomeFragment.restaurantModel = restaurantModel;
                            restaurantModelArrayListReceived.add(HomeFragment.restaurantModel);


                        }
                    }

                    if (restaurantModelArrayListReceived.size() > 0) {
                        PopularNearAdapter popularNearAdapter = new PopularNearAdapter(getActivity(), restaurantModelArrayListReceived);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        popularnearrecycler.setLayoutManager(horizontalLayoutManager1);
                        popularnearrecycler.setAdapter(popularNearAdapter);
                    } else {
                        meal.setVisibility(View.GONE);
                        viewall.setVisibility(View.GONE);
                        dealsrecycler.setVisibility(View.GONE);
                    }

                   /* if (Network.isNetworkCheck(getActivity())) {

                        HitURLforQuickSearchData();


                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }*/
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
//                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();

                MyProgressDialog.dismiss();
              /*  if (Network.isNetworkCheck(getActivity())) {

                    HitURLforQuickSearchData();


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }

    public void showSettingPoup() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.searchcitydailog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);
        ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        cityrecycler = (RecyclerView) dialog.findViewById(R.id.cityrecycler);
        search_view = (EditText) dialog.findViewById(R.id.search_view);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ArrayList<Citymodel> cityListing = databaseHelper.getCityListing();
        if (cityListing.size() > 0) {
            CityList.clear();
            for (int i = 0; i < cityListing.size(); i++) {
                CityList.add(cityListing.get(i));
            }
            if (CityList.size() > 0) {
                CITYAdapter cityAdapter = new CITYAdapter(getActivity(), CityList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }
        } else {
            if (!Network.isNetworkCheck(getActivity())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
            } else {
                HitURLforCityLIst();
            }
        }


        /**
         * Enabling Search Filter
         * */
        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                for (int i = 0; i < CityList.size(); i++) {
                    String temp = CityList.get(i).getCity_name();
                    temp = temp.toLowerCase();
                    if (temp.contains(cs.toString())) {
                        searchList.clear();
                        String id = CityList.get(i).getId();
                        String name = CityList.get(i).getCity_name();
                        Citymodel citymodel = new Citymodel();
                        citymodel.setCity_name(name);
                        citymodel.setId(id);
                        searchList.add(citymodel);
                    } else {
                    }
                }
                cityAdapter = new CITYAdapter(getActivity(), searchList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        dialog.show();
    }

    public void showSingleSearch() {
        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.picklocation);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.setCanceledOnTouchOutside(true);
        final ListView mAutoCompleteList;
        final EditText Address;


        Address = (EditText) dialog1.findViewById(R.id.adressText);
        mAutoCompleteList = (ListView) dialog1.findViewById(R.id.searchResultLV);
        final String GETPLACESHIT = "places_hit";


        Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (Address.getText().length() > 1) {


//                    searchBtn.setVisibility(View.GONE);

                    Runnable run = new Runnable() {


                        @Override
                        public void run() {

                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                            MyApplication.volleyQueueInstance.cancelRequestInQueue(GETPLACESHIT);

                            //build Get url of Place Autocomplete and hit the url to fetch result.
                            request = new VolleyJSONRequest(Request.Method.GET, getPlaceAutoCompleteUrl(Address.getText().toString()), null, null, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                        searchBtn.setVisibility(View.VISIBLE);
                                    mAutoCompleteAdapter = null;
                                    Log.e("PLACES RESULT:::", response);
                                    Gson gson = new Gson();
                                    predictions = gson.fromJson(response, PlacePredictions.class);

                                    if (mAutoCompleteAdapter == null) {
                                        mAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), predictions.getPlaces(), getActivity());
                                        mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
                                    } else {
                                        mAutoCompleteAdapter.clear();
                                        mAutoCompleteAdapter.addAll(predictions.getPlaces());
                                        mAutoCompleteAdapter.notifyDataSetChanged();
                                        mAutoCompleteList.invalidate();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            //Give a tag to your request so that you can use this tag to cancle request later.
                            request.setTag(GETPLACESHIT);

                            MyApplication.volleyQueueInstance.addToRequestQueue(request);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler1 != null) {
                        handler1.removeCallbacksAndMessages(null);
                    } else {
                        handler1 = new Handler();
                    }
                    handler1.postDelayed(run, 1000);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mAutoCompleteList.setOnItemClickListener((parent, view, position, id) -> {
            // pass the result to the calling activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    edt_placesearch.setText(predictions.getPlaces().get(position).getPlaceDesc());

                    dialog1.dismiss();

                }
            }, 1000);
        });

        dialog1.show();
    }

    public void HitURLforCuisineDisplay() {
        delieveredCuisineList = new ArrayList<>();
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CuisiineDisplay> userLoginCall = interfaceRetrofit.phpexpert_home_cuisine_display();
        userLoginCall.enqueue(new Callback<Model_CuisiineDisplay>() {
            @Override
            public void onResponse(Call<Model_CuisiineDisplay> call, retrofit2.Response<Model_CuisiineDisplay> response) {
                if (response.isSuccessful()) {

                    try {

                        if (response.body().getQuickSearchList().size() > 0) {

                            for (int i = 0; i < response.body().getQuickSearchList().size(); i++) {

                                CuisineModel cuisineModel = response.body().getQuickSearchList().get(i);

                                delieveredCuisineList.add(cuisineModel);

                            }

                        }

                        if (delieveredCuisineList.size() > 0) {

                            DeliveredAdapter deliveredAdapter = new DeliveredAdapter(getActivity(), delieveredCuisineList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            delieveredrecycler.setLayoutManager(horizontalLayoutManager1);
                            delieveredrecycler.setAdapter(deliveredAdapter);
                        }
/*
                        if (Network.isNetworkCheck(getActivity())) {

                            HitURLforPopularNear();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();

                       /* if (Network.isNetworkCheck(getActivity())) {

                            HitURLforPopularNear();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }*/
                    }

                }

            }

            @Override
            public void onFailure(Call<Model_CuisiineDisplay> call, Throwable t) {

              /*  if (Network.isNetworkCheck(getActivity())) {

                    HitURLforPopularNear();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    public void HitURLforQuickSearchData() {

        quickimageList = new ArrayList<>();
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_FeaturePartnerModel> userLoginCall = interfaceRetrofit.FoodTypeQuickSearch();
        userLoginCall.enqueue(new Callback<Model_FeaturePartnerModel>() {
            @Override
            public void onResponse(Call<Model_FeaturePartnerModel> call, retrofit2.Response<Model_FeaturePartnerModel> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getQuickSearchList().size(); i++) {
                        FeaturedPartnerModel featuredPartnerModel = response.body().getQuickSearchList().get(i);
                        quickimageList.add(featuredPartnerModel);

                    }

                    if (quickimageList.size() > 0) {
                        QuickAdapter quickAdapter = new QuickAdapter(getActivity(), quickimageList);
                        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        quickrecycler.setLayoutManager(horizontalLayoutManager2);
                        quickrecycler.setAdapter(quickAdapter);
                    }


                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_FeaturePartnerModel> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


    }

    public void HitUrlMealDeals() {
        DealsList = new ArrayList<>();
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
                        meal.setVisibility(View.VISIBLE);
                        dealsrecycler.setVisibility(View.VISIBLE);
                        DealsAdapter dealsAdapter = new DealsAdapter(getActivity(), DealsList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        dealsrecycler.setLayoutManager(horizontalLayoutManager1);
                        dealsrecycler.setAdapter(dealsAdapter);
                    } else {
                        meal.setVisibility(View.GONE);
                        viewall.setVisibility(View.GONE);
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

    public void HitURLforFeaturedpartner() {
        MyProgressDialog.show(getActivity(), R.string.please_wait);
        restaurantModelArrayListReceived = new ArrayList<RestaurantModel>();


        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.featureReasurant(myPref.getState(), myPref.getCity(), myPref.getLatitude(),
                myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {
                    Model_Reasurant body = response.body();

                    ArrayList<RestaurantModel> searchResult = body.getSearchResult();
                    for (int i = 0; i < searchResult.size(); i++) {
                        if (!searchResult.get(i).getError().equalsIgnoreCase("2")) {
                            feature.setVisibility(View.VISIBLE);
                            featuredimagerecycler.setVisibility(View.VISIBLE);
                            HomeFragment.restaurantModel = searchResult.get(i);
                            restaurantModelArrayListReceived.add(HomeFragment.restaurantModel);
                            featuredimageList.add(new FeaturedDataModel(searchResult.get(i).getId(), searchResult.get(i).getRestaurant_name(), searchResult.get(i).getRestaurant_Logo()
                                    , searchResult.get(i).getRestaurant_address(), searchResult.get(i).getRatingValue()));

                        } else {
                            feature.setVisibility(View.GONE);
                            featuredimagerecycler.setVisibility(View.GONE);


                        }

                    }

                    if (featuredimageList.size() > 0) {

                        FeaturedPartnerAdapter featuredPartnerAdapter = new FeaturedPartnerAdapter(getActivity(), restaurantModelArrayListReceived);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        featuredimagerecycler.setLayoutManager(horizontalLayoutManager1);
                        featuredimagerecycler.setAdapter(featuredPartnerAdapter);
                    } else {
                        feature.setVisibility(View.GONE);
                        featuredimagerecycler.setVisibility(View.GONE);


                    }


                }

               /* if (Network.isNetworkCheck(getActivity())) {
                    HitUrlMealDeals();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
*/

            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
//                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();

              /*  if (Network.isNetworkCheck(getActivity())) {
                    HitUrlMealDeals();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }

    public void HitURLforCityLIst() {

        CityList = new ArrayList<Citymodel>();
        MyProgressDialog.show(getActivity(), R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CityListing> userLoginCall = interfaceRetrofit.citylising(UrlConstants.City);
        userLoginCall.enqueue(new Callback<Model_CityListing>() {
            @Override
            public void onResponse(Call<Model_CityListing> call, retrofit2.Response<Model_CityListing> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getCityList().size(); i++) {
                        Citymodel model_cityDetail = response.body().getCityList().get(i);
                        if (model_cityDetail.getError().equalsIgnoreCase("0")) {
                            CityList.add(model_cityDetail);
                            databaseHelper.saveCityDetails(model_cityDetail);
                        }
                    }
                    if (CityList.size() > 0) {
                        CITYAdapter cityAdapter = new CITYAdapter(getActivity(), CityList);

                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_CityListing> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


     /*
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        CityList = new ArrayList<Citymodel>();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.City, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray citylist = jsonObj.getJSONArray("CityList");
                    for (int i = 0; i < citylist.length(); i++) {
                        JSONObject c = citylist.getJSONObject(i);

                        String id = c.getString("id");
                        String cityname = c.getString("city_name");
                        String state = c.getString("seo_url_call");
                        String universal_name = c.getString("where");
                        String error = c.getString("error");

//                        AuthPreference authPreference=new AuthPreference(getActivity());
//                        authPreference.setCityName(cityname);
                        Citymodel citymodel=new Citymodel();
                        citymodel.setCity_name(cityname);
                        citymodel.setId(id);

                        CityList.add(citymodel);
                        CITYAdapter cityAdapter = new CITYAdapter(getActivity(), CityList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                        pDialog.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(getResources().getString(R.string.went_wrong));

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("customer_country", SplashScreenActivity.customer_country);
                params.put("customer_city", SplashScreenActivity.customer_city);
                params.put("customer_lat", SplashScreenActivity.customer_lat);
                params.put("customer_long", myPref.getLongitude());
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);*/
    }

    private void initialization(View view) {

        tvSearch = view.findViewById(R.id.tv_search_txt);
        edtPassCode = view.findViewById(R.id.edt_passCode);
        authPreference = new AuthPreference(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        final RelativeLayout layout = view.findViewById(R.id.layout);
        layout.setOnTouchListener(
                (view1, ev) -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    return false;
                });

    }


    private void initializedListener(View view) {
        tvSearch.setOnClickListener(this);

    }


    public void passCodeValidation() {

        if (CITY.equals("")) {
            if (myPref.getReferId().equals("2")) {
                city.setError("Please Select Your City");
                city.requestFocus();
            } else {
                edt_placesearch.setError("Please Select Your City");
                edt_placesearch.requestFocus();
            }
            city.setError("Please Select Your City");
            city.requestFocus();
        } else if (Network.isNetworkCheck(getActivity())) {
            HomeActivity.where_para = CITY;
            HomeActivity.customerlocality_para = edtPassCode.getText().toString();
            searchFromPassCode();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_txt:
                if (myPref.getReferId().equals("2")) {
                    CITY = city.getText().toString();
                } else {
                    CITY = edt_placesearch.getText().toString();
                }
                passCodeValidation();
                break;
        }
    }

    private void searchFromPassCode() {
        passCode = edtPassCode.getText().toString();
        Log.e("params", "" + myPref.getCity());
        Log.e("params", "" + CITY);
        Log.e("params", "" + myPref.getState());
        Log.e("params", "" + myPref.getLatitude());
        Log.e("params", "" + myPref.getLongitude());
        Log.e("params", "" + myPref.getReferId());
//        pDialog = new ProgressDialog(getActivity());
        restaurantModelArrayList = new ArrayList<RestaurantModel>();
        restaurantModelArrayList.clear();

        MyProgressDialog.show(getActivity(), R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.phpexpert_search(CITY, myPref.getState(), myPref.getCity(), myPref.getLatitude(),
                myPref.getLongitude(), myPref.getReferId());
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                        restaurantModelArrayList.add(restaurantModel);
                    }
                    Intent intent = (new Intent(getActivity(), RestaurantsListActivity.class));
                    intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                    intent.putExtra("passCode", passCode);
                    intent.putExtra("cityname", CITY);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });



       /* pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);

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
                            String restaurant_distance_check1 = jsonObject.getString("restaurant_distance_check");
                            String mealDealAvailable = jsonObject.getString("mealDealAvailable");
                            String RestaurantTimeStatusText = jsonObject.getString("RestaurantTimeStatusText");
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

                            /////////////icon show/////
                            restaurantModel.setBookaTable(bookatable);
                            restaurantModel.setKidFriendly(kidfriendly);
                            restaurantModel.setPetFriendly(petfriendly);
                            restaurantModel.setDINEin(dinein);
                            restaurantModel.setCollection(collection);
                            restaurantModel.setDelivery(delievery);
                            restaurantModel.setmealDealAvailable(mealDealAvailable);
                            restaurantModel.setDineInAvailable(DineInAvailable);
                            restaurantModel.setrestaurant_distance_check(restaurant_distance_check1);
                            restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                            //////////

                            restaurantModelArrayList.add(restaurantModel);
                            //  pDialog.dismiss();
                            Intent intent = (new Intent(getActivity(), RestaurantsListActivity.class));
                            intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                            intent.putExtra("passCode", passCode);
                            intent.putExtra("cityname", CITY);
                            getActivity().startActivity(intent);
                            getActivity().finish();

                        } else {

                            String error = jsonObject.optString("error");
                            String errorMsg = jsonObject.optString("error_msg");
                            showCustomDialog1decline(errorMsg);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                showCustomDialog1decline(getResources().getString(R.string.went_wrong));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to Search

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", CITY);
                params.put("customer_country", SplashScreenActivity.customer_country);
                params.put("customer_city", SplashScreenActivity.customer_city);
                params.put("customer_lat", SplashScreenActivity.customer_lat);
                params.put("customer_long", myPref.getLongitude());
                params.put("customer_search_display", SplashScreenActivity.customer_search_display);
                Log.e("params", "" + params);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);*/
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();

    }

    public class QuickAdapter extends RecyclerView.Adapter<QuickAdapter.ViewHolder> {

        Context context;
        ArrayList<FeaturedPartnerModel> pp;

        public QuickAdapter(Context c, ArrayList<FeaturedPartnerModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.quickimagelist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.servicename.setText(pp.get(position).getRestaurantServiceName());
            Picasso.get().load(pp.get(position).getService_img()).into(holder.serviceimage);
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView servicename;
         //   CircleImageView serviceimage;
            ImageView serviceimage;


            public ViewHolder(View itemView) {
                super(itemView);
                servicename = itemView.findViewById(R.id.servicename);
             //   serviceimage = (CircleImageView) itemView.findViewById(R.id.serviceimage);
                serviceimage = itemView.findViewById(R.id.serviceimage);

                itemView.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(getActivity(), QuickSearchLocation.class);
                    intent.putExtra("serviceName", pp.get(position).getRestaurantServiceName());
                    intent.putExtra("data", "1");
                    startActivity(intent);
                });
            }
        }
    }

    public class DeliveredAdapter extends RecyclerView.Adapter<DeliveredAdapter.ViewHolder> {

        Context context;
        ArrayList<CuisineModel> pp;

        public DeliveredAdapter(Context c, ArrayList<CuisineModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.delieverimagelist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.cuisinename.setText(pp.get(position).getRestaurantCuisineName());
            Picasso.get().load(pp.get(position).getCuisine_img()).into(holder.cuisineimage);
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView cuisinename;
            ImageView cuisineimage;

            public ViewHolder(View itemView) {
                super(itemView);
                cuisinename = (TextView) itemView.findViewById(R.id.cuisinename);
                cuisineimage = (ImageView) itemView.findViewById(R.id.cuisineimage);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Log.e("quickposition", "" + position);
                        Intent intent = new Intent(getActivity(), QuickSearchLocation.class);
                        intent.putExtra("cuisineName", pp.get(position).getRestaurantCuisineName());
                        intent.putExtra("data", "2");

                        startActivity(intent);
                    }
                });
            }
        }
    }

    public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

        Context context;
        ArrayList<RestaurantModel> DealsList;

        public DealsAdapter(Context c, ArrayList<RestaurantModel> DealsList) {
            this.context = c;
            this.DealsList = DealsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.mealofdaylist, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.dealname.setText(DealsList.get(position).getDeal_name());
            Picasso.get().load(DealsList.get(position).getDeal_image()).into(holder.dealimage);
            holder.dealprice.setText(SplashScreenActivity.currency_symbol + " " + DealsList.get(position).getDeal_price());
            holder.dealdiscountamount.setText("(" + DealsList.get(position).getDeal_discount_amount() + "% OFF )");
            holder.drestraurantname.setText(DealsList.get(position).getRestaurant_name());
            Picasso.get().load(DealsList.get(position).getRestaurant_img()).into(holder.restraurantimagege);
            holder.pickuptime.setText(DealsList.get(position).getRestaurant_pickup_time() + " Mins");
            holder.delieverytime.setText(DealsList.get(position).getRestaurant_delivery_time() + " Mins");

            String YourString = DealsList.get(position).getDeal_description();

            if (YourString != null) {
                if (YourString.length() > 40) {
                    YourString = YourString.substring(0, 37) + "...";
                    holder.dealdescription.setText(YourString);
                } else {
                    holder.dealdescription.setText(YourString);
                }
            }


            holder.ordernow.setOnClickListener(view -> {
                if (DealsList.get(position).getRestaurantTimeStatus1().equals("Closed") || DealsList.get(position).getRestaurantTimeStatus1().equals("Busy")) {
                    showCustomDialog1decline(DealsList.get(position).getRestaurantTimeStatusText());
                } else {
                    final Database database = new Database(context);
                    final String subMenuItemId = DealsList.get(position).getMealid();

                    Log.e("qwqwqwqwqw", "" + subMenuItemId);
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

                    restaurant_distance_check = DealsList.get(position).getrestaurant_distance_check();
                    RestaurantsListActivity.res_lat = DealsList.get(position).getRestaurant_LatitudePoint();
                    RestaurantsListActivity.res_lng = DealsList.get(position).getRestaurant_LongitudePoint();
                    RestaurantsListActivity.cash_avaible = DealsList.get(position).getRestaurant_onlycashonAvailable();
                    RestaurantsListActivity.card_avaible = DealsList.get(position).getRestaurant_cardacceptAvailable();

                    Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                    String id = DealsList.get(position).getId();

                    SubMenuActivity.restaurantAddress = DealsList.get(position).getRestaurant_address();
                    SubMenuActivity.restaurantName = DealsList.get(position).getRestaurant_name();
                    SubMenuActivity.id = DealsList.get(position).getRestaurant_id();
                    RestaurantModel model_feature_restaurant = DealsList.get(position);
                    authPreference.setRestraurantCity("" + DealsList.get(position).getRestaurantCity());

                    authPreference.setMinprice(DealsList.get(position).getRestaurant_minimumorder());
                    intent.putExtra("restaurantModel", (Serializable) model_feature_restaurant);
                    intent.putExtra("restaurantModelArrayListReceived", DealsList);
                    intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                    intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                    intent.putExtra("restaurantAddress", SubMenuActivity.restaurantAddress);
                    intent.putExtra("restaurantName", SubMenuActivity.restaurantName);
                    intent.putExtra("id", SubMenuActivity.id);
                    intent.putExtra("restaurantCategoryId", authPreference.getRestaurantCategoryID());
                    startActivity(intent);
                }

                try {

                } catch (Exception e) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return DealsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView dealname, dealprice, dealdiscountamount, dealdescription, drestraurantname, pickuptime, delieverytime, mealstarthour, mealstartmint, mealendhour, mealendmint;
            ImageView dealimage, restraurantimagege;
            Button ordernow;

            public ViewHolder(final View itemView) {
                super(itemView);
                dealname = (TextView) itemView.findViewById(R.id.dealname);
                dealimage = (ImageView) itemView.findViewById(R.id.dealimage);
                dealprice = (TextView) itemView.findViewById(R.id.dealprice);
                dealdiscountamount = itemView.findViewById(R.id.dealdiscountamount);
                dealdescription = itemView.findViewById(R.id.dealdescription);
                restraurantimagege = itemView.findViewById(R.id.restraurantimagege);
                drestraurantname = itemView.findViewById(R.id.drestraurantname);
                pickuptime = itemView.findViewById(R.id.pickuptime);
                delieverytime = itemView.findViewById(R.id.delieverytime);
                ordernow = itemView.findViewById(R.id.ordernow);
            }
        }
    }

    public class FeaturedPartnerAdapter extends RecyclerView.Adapter<FeaturedPartnerAdapter.ViewHolder> {

        Context context;
        ArrayList<RestaurantModel> pp;

        public FeaturedPartnerAdapter(Context c, ArrayList<RestaurantModel> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.featuredpartnersimage, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.cuisinename.setText(pp.get(position).getRestaurant_name());
            Picasso.get().load(pp.get(position).getRestaurant_Logo()).into(holder.cuisineimage);
            holder.featuredaddress.setText(pp.get(position).getRestaurant_address());
            String YourString = pp.get(position).getRestaurant_address();
            if (YourString.length() > 30) {
                YourString = YourString.substring(0, 27) + "...";
                holder.featuredaddress.setText(YourString);
            } else {
                holder.featuredaddress.setText(YourString);
            }

            Log.e("star", "" + pp.get(position).getRatingValue());
//           String ratingValue = pp.get(position).getRatingValue();
//
            if (pp.get(position).getRatingValue().equalsIgnoreCase("0")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_star);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (pp.get(position).getRatingValue().equalsIgnoreCase("1")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (pp.get(position).getRatingValue().equalsIgnoreCase("2")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (pp.get(position).getRatingValue().equalsIgnoreCase("3")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (pp.get(position).getRatingValue().equalsIgnoreCase("4")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (pp.get(position).getRatingValue().equalsIgnoreCase("5")) {
                holder.iv_review1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review4.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review5.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
            }
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView cuisinename, featuredaddress;
            ImageView cuisineimage;
            ImageView ivBack, iv_review1, iv_review2, iv_review3, iv_review4, iv_review5;

            public ViewHolder(final View itemView) {
                super(itemView);
                cuisinename = (TextView) itemView.findViewById(R.id.featuredname);
                cuisineimage = (ImageView) itemView.findViewById(R.id.featuredimage);
                featuredaddress = (TextView) itemView.findViewById(R.id.featuredaddress);
                iv_review1 = itemView.findViewById(R.id.review_1);
                iv_review2 = itemView.findViewById(R.id.review_2);
                iv_review3 = itemView.findViewById(R.id.review_3);
                iv_review4 = itemView.findViewById(R.id.review_4);
                iv_review5 = itemView.findViewById(R.id.review_5);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                restaurant_distance_check = pp.get(position).getrestaurant_distance_check();
                RestaurantsListActivity.res_lat = pp.get(position).getRestaurant_LatitudePoint();
                RestaurantsListActivity.res_lng = pp.get(position).getRestaurant_LongitudePoint();
                RestaurantsListActivity.cash_avaible = pp.get(position).getRestaurant_onlycashonAvailable();
                RestaurantsListActivity.card_avaible = pp.get(position).getRestaurant_cardacceptAvailable();

                Log.e("qwe", "" + pp.get(position).getRestaurant_LatitudePoint());
                Log.e("qwe1", "" + pp.get(position).getrestaurant_distance_check());

                Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                String id = pp.get(position).getId();
                RestaurantModel restaurantModel = pp.get(position);
                authPreference.setRestraurantCity("" + pp.get(position).getRestaurantCity());

                authPreference.setMinprice(pp.get(position).getRestaurant_minimumorder());
                intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                intent.putExtra("restaurantModelArrayListReceived", pp);
                intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                intent.putExtra("cityname", CITY);
                intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                startActivity(intent);
            }
        }
    }

    public class CITYAdapter extends RecyclerView.Adapter<CITYAdapter.ViewHolder> implements Filterable {

        Context context;
        private ArrayList<Citymodel> CityList;
        private int lastSelectedPosition = -1;
        private ArrayList<Citymodel> filterList;

        public CITYAdapter(Context c, ArrayList<Citymodel> CityList) {
            this.context = c;
            this.CityList = CityList;
            this.filterList = CityList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.citylistrecycler, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

//            holder.tv_city_name.setText(pp.get(position).getCityname());

            citymodel = CityList.get(position);
            holder.tv_city_name.setText(CityList.get(position).getCity_name());
//            holder.citychecked.setChecked(true);

//                    (lastSelectedPosition == position);

            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.citychecked.setChecked(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            notifyDataSetChanged();
                            city.setText(holder.tv_city_name.getText());
                            dialog.dismiss();
                        }
                    }, 1000);
                }
            });
        }

        @Override
        public int getItemCount() {
            return CityList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterList = CityList;
                    } else {
                        ArrayList<Citymodel> myList = new ArrayList<>();
                        for (Citymodel temp : CityList) {
                            if (temp.getCity_name().toLowerCase().contains(charString)) ;
                            filterList.add(temp);
                        }
                        filterList = myList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filterList = (ArrayList<Citymodel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_city_name;
            LinearLayout cuisinesLayout;
            RadioButton citychecked;
            RadioGroup citygroup;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
                cuisinesLayout = (LinearLayout) itemView.findViewById(R.id.cuisines_layout);
                citychecked = (RadioButton) itemView.findViewById(R.id.citychecked);
                citygroup = (RadioGroup) itemView.findViewById(R.id.citygroup);
            }
        }
    }

    public class PopularNearAdapter extends RecyclerView.Adapter<PopularNearAdapter.ViewHolder> {

        Context context;

        ArrayList<RestaurantModel> popularnearList;

        public PopularNearAdapter(Context c, ArrayList<RestaurantModel> popularnearList) {
            this.context = c;
            this.popularnearList = popularnearList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.popularlayout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.cuisinename.setText(popularnearList.get(position).getRestaurant_name());
            Picasso.get().load(popularnearList.get(position).getRestaurant_Logo()).into(holder.cuisineimage);
            holder.miles.setText(popularnearList.get(position).getRestaurant_deliveryDistance());
            holder.timee.setText(popularnearList.get(position).getRestaurant_avarage_deliveryTime());

            String YourString = popularnearList.get(position).getRestaurant_address();


            holder.featuredaddress.setText(popularnearList.get(position).getRestaurant_address());

            Log.e("star", "" + popularnearList.get(position).getRatingValue());
//           String ratingValue = pp.get(position).getRatingValue();
//
            if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("0")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_star);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("1")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_star);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("2")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_star);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("3")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_star);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("4")) {
                holder.iv_review1.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review2.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review3.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review4.setBackgroundResource(R.drawable.review_yellow);
                holder.iv_review5.setBackgroundResource(R.drawable.review_star);

            } else if (popularnearList.get(position).getRatingValue().equalsIgnoreCase("5")) {
                holder.iv_review1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review4.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
                holder.iv_review5.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.review_yellow));
            }
        }

        @Override
        public int getItemCount() {
            return popularnearList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView cuisinename, featuredaddress, timee, miles;
            ImageView cuisineimage;
            ImageView ivBack, iv_review1, iv_review2, iv_review3, iv_review4, iv_review5;

            public ViewHolder(final View itemView) {
                super(itemView);
                cuisinename = (TextView) itemView.findViewById(R.id.featuredname);
                cuisineimage = (ImageView) itemView.findViewById(R.id.featuredimage);
                featuredaddress = (TextView) itemView.findViewById(R.id.featuredaddress);
                iv_review1 = itemView.findViewById(R.id.review_1);
                iv_review2 = itemView.findViewById(R.id.review_2);
                iv_review3 = itemView.findViewById(R.id.review_3);
                iv_review4 = itemView.findViewById(R.id.review_4);
                iv_review5 = itemView.findViewById(R.id.review_5);
                miles = itemView.findViewById(R.id.miles);
                timee = itemView.findViewById(R.id.timee);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();


                try {
                    if (popularnearList.get(position).getRestaurantTimeStatus1().equals("Closed") || popularnearList.get(position).getRestaurantTimeStatus1().equals("Busy")) {
                        showCustomDialog1decline(popularnearList.get(position).getRestaurantTimeStatusText());
                    } else {

                        restaurant_distance_check = popularnearList.get(position).getrestaurant_distance_check();
                        RestaurantsListActivity.res_lat = popularnearList.get(position).getRestaurant_LatitudePoint();
                        RestaurantsListActivity.res_lng = popularnearList.get(position).getRestaurant_LongitudePoint();
                        RestaurantsListActivity.cash_avaible = popularnearList.get(position).getRestaurant_onlycashonAvailable();
                        RestaurantsListActivity.card_avaible = popularnearList.get(position).getRestaurant_cardacceptAvailable();


                        Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                        String id = popularnearList.get(position).getId();
                        RestaurantModel restaurantModel = popularnearList.get(position);
                        authPreference.setRestraurantCity("" + popularnearList.get(position).getRestaurantCity());

                        authPreference.setMinprice(popularnearList.get(position).getRestaurant_minimumorder());
                        intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                        intent.putExtra("restaurantModelArrayListReceived", popularnearList);
                        intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                        intent.putExtra("cityname", CITY);
                        intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                        intent.putExtra("Booktable", restaurantModel.getBookaTable());
                        startActivity(intent);
                    }


                } catch (Exception e) {

                }


            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void getData() {
        XMENArray.clear();

        XMENArray = new ArrayList<>();
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_AppFlashBannerList> userLoginCall = interfaceRetrofit.flashbanner(myPref.getState(), myPref.getCity(), myPref.getLatitude(),
                myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_AppFlashBannerList>() {
            @Override
            public void onResponse(Call<Model_AppFlashBannerList> call, retrofit2.Response<Model_AppFlashBannerList> response) {
                if (response.isSuccessful()) {
                    try {
                        for (int i = 0; i < response.body().getFrontBannersList().size(); i++) {
                            XMENArray.add(response.body().getFrontBannersList().get(i).getApp_banner_img());
                        }
                        MyAdapter myAdapter = new MyAdapter(getActivity(), XMENArray);
                        viewPagerslider.setAdapter(myAdapter);
                        indicator.setViewPager(viewPagerslider);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Model_AppFlashBannerList> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.phpexpert_app_flash_banner, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("FrontBannersList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jj = jsonArray.getJSONObject(i);
                        String error = jj.getString("error");
                        if (error.equals("1")) {
                            llbanner.setVisibility(View.GONE);
                        } else {
                            String app_banner_img = jj.getString("app_banner_img");
                            XMENArray.add(app_banner_img);
                        }
                    }
                    MyAdapter myAdapter = new MyAdapter(getActivity(), XMENArray);
                    viewPagerslider.setAdapter(myAdapter);
                    indicator.setViewPager(viewPagerslider);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_country", SplashScreenActivity.customer_country);
                params.put("customer_city", SplashScreenActivity.customer_city);
                params.put("customer_lat", SplashScreenActivity.customer_lat);
                params.put("customer_long", myPref.getLongitude());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(stringRequest);*/
    }


    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        urlString.append("&amp;location=");
//        urlString.append(latitude + "," + longitude);
        urlString.append("&amp;radius=500&amp;language=en");

//        urlString.append("&types=(cities)");
        urlString.append("&components=country:" + SplashScreenActivity.customer_country_code);//country code

        urlString.append("&key=" + "AIzaSyDNyevW_K3I8Nk_6Rg6jntytgi0W0rNu58");

        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }

    public class AutoCompleteAdapter extends ArrayAdapter<PlaceAutoComplete> {
        ViewHolder holder;
        Context context;
        List<PlaceAutoComplete> Places;
        private Activity mActivity;

        public AutoCompleteAdapter(Context context, List<PlaceAutoComplete> modelsArrayList, Activity activity) {
            super(context, R.layout.autocomplete_row, modelsArrayList);
            this.context = context;
            this.Places = modelsArrayList;
            this.mActivity = activity;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.autocomplete_row, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) rowView.findViewById(R.id.place_name);
                holder.location = (TextView) rowView.findViewById(R.id.place_detail);

                rowView.setTag(holder);

            } else
                holder = (ViewHolder) rowView.getTag();
            /***** Get each Model object from ArrayList ********/
            holder.Place = Places.get(position);
            StringTokenizer st = new StringTokenizer(holder.Place.getPlaceDesc(), ",");
            /************  Set Model values in Holder elements ***********/

            holder.name.setText(st.nextToken());
            String desc_detail = "";
            for (int i = 1; i < st.countTokens(); i++) {
                if (i == st.countTokens() - 1) {
                    desc_detail = desc_detail + st.nextToken();
                } else {
                    desc_detail = desc_detail + st.nextToken() + ",";
                }
            }
            holder.location.setText(desc_detail);
            return rowView;


        }


        class ViewHolder {
            PlaceAutoComplete Place;
            TextView name, location;

        }

        @Override
        public int getCount() {
            return Places.size();
        }
    }

}