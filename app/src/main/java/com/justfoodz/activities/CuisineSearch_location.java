package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.justfoodz.fragments.Model_Reasurant;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.DatabaseHelper;
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

public class CuisineSearch_location extends AppCompatActivity {
    LinearLayout citydialog;
    Dialog dialog;
    private RecyclerView cityrecycler;
    private ArrayList<Citymodel> CityList;
    private CITYAdapter cityAdapter;
    EditText search_view;
    ProgressDialog progressDialog;
    private ArrayList<Citymodel> searchList;
    private Citymodel citymodel;
    private TextView city, servicename;
    private EditText edtPassCode;
    private Button tv_search_txt;
    String CITY;
    public static String passCode;
    private ProgressDialog pDialog;
    public ArrayList<RestaurantModel> restaurantModelArrayList;
    private AuthPreference authPreference;
    public static RestaurantModel restaurantModel;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_search_location);

        intializeUI();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String service = bundle.getString("serviceName");
        servicename.setText("'" + service + "'");

        citydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingPoup();
            }
        });
        tv_search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CITY = city.getText().toString();
                if (CITY.equals("")) {
                    city.setError("Please Select Your City");
                    city.requestFocus();
                } else if (Network.isNetworkCheck(CuisineSearch_location.this)) {
                    searchFromPassCode();

                } else {
                    Toast.makeText(CuisineSearch_location.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void intializeUI() {
        databaseHelper = new DatabaseHelper(CuisineSearch_location.this);
        citydialog = findViewById(R.id.citydialog);
        city = findViewById(R.id.city);
        servicename = findViewById(R.id.servicename);
        edtPassCode = findViewById(R.id.edt_passCode);
        tv_search_txt = findViewById(R.id.tv_search_txt);
        authPreference = new AuthPreference(this);
        searchList = new ArrayList<>();
    }

    public void showSettingPoup() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.searchcitydailog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);

        cityrecycler = (RecyclerView) dialog.findViewById(R.id.cityrecycler);
        search_view = (EditText) dialog.findViewById(R.id.search_view);


        ArrayList<Citymodel> cityListing = databaseHelper.getCityListing();
        if (cityListing.size() > 0) {
            CityList.clear();
            for (int i = 0; i < cityListing.size(); i++) {
                CityList.add(cityListing.get(i));
            }
            if (CityList.size() > 0) {
                CITYAdapter cityAdapter = new CITYAdapter(CuisineSearch_location.this, CityList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CuisineSearch_location.this, LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }
        } else {
            if (!Network.isNetworkCheck(this)) {
                Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
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
                cityAdapter = new CITYAdapter(CuisineSearch_location.this, searchList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CuisineSearch_location.this, LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
//                Log.e("searchstring", (String) arg0);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        dialog.show();

    }

    public void HitURLforCityLIst() {

        MyProgressDialog.show(this, R.string.please_wait);
        CityList=new ArrayList<>();
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
                        CITYAdapter cityAdapter = new CITYAdapter(CuisineSearch_location.this, CityList);

                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CuisineSearch_location.this, LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_CityListing> call, Throwable t) {
                Toast.makeText(CuisineSearch_location.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        CityList = new ArrayList<Citymodel>();
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.City, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
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


                        Citymodel citymodel = new Citymodel();
                        citymodel.setCity_name(cityname);
                        citymodel.setId(id);


                        CityList.add(citymodel);
                        CITYAdapter cityAdapter = new CITYAdapter(CuisineSearch_location.this, CityList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CuisineSearch_location.this, LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                        progressDialog.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
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
                            lastSelectedPosition = position;
                            notifyDataSetChanged();
                            city.setText(holder.tv_city_name.getText());
                            dialog.dismiss();
                            progressDialog.dismiss();
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

    private void searchFromPassCode() {
        passCode = edtPassCode.getText().toString();
        restaurantModelArrayList = new ArrayList<RestaurantModel>();


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_Reasurant> userLoginCall = interfaceRetrofit.phpexpert_search_where(UrlConstants.City);
        userLoginCall.enqueue(new Callback<Model_Reasurant>() {
            @Override
            public void onResponse(Call<Model_Reasurant> call, retrofit2.Response<Model_Reasurant> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getSearchResult().size(); i++) {
                        RestaurantModel restaurantModel = response.body().getSearchResult().get(i);
                        if (restaurantModel.getError().equalsIgnoreCase("0")) {
                            restaurantModelArrayList.add(restaurantModel);

                        }
                    }
                    if (restaurantModelArrayList.size() > 0) {
                        Intent intent = (new Intent(CuisineSearch_location.this, RestaurantsListActivity.class));
                        intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                        intent.putExtra("passCode", passCode);
                        CuisineSearch_location.this.startActivity(intent);
                        CuisineSearch_location.this.finish();
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_Reasurant> call, Throwable t) {
                Toast.makeText(CuisineSearch_location.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });





       /* Log.e("params", "" + passCode);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);

        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("homere", "" + response);
                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONArray jsonArray = jObj.getJSONArray("searchResult");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.has("error_msg")) {

                            String id = jsonObject.optString("id");
                            String restaurant_name = jsonObject.optString("restaurant_name");
                            String restaurantsupervisory = jsonObject.optString("restaurantsupervisory");
                            String restaurant_address = jsonObject.optString("restaurant_address");
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
                            String mealDealAvailable = jsonObject.optString("mealDealAvailable");
                            String RestaurantTimeStatusText = jsonObject.optString("RestaurantTimeStatusText");
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

                            /////////////icon show/////
                            restaurantModel.setBookaTable(bookatable);
                            restaurantModel.setKidFriendly(kidfriendly);
                            restaurantModel.setPetFriendly(petfriendly);
                            restaurantModel.setDINEin(dinein);
                            restaurantModel.setCollection(collection);
                            restaurantModel.setDelivery(delievery);
                            restaurantModel.setmealDealAvailable(mealDealAvailable);
                            restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                            //////////

                            restaurantModelArrayList.add(restaurantModel);
                            //  pDialog.dismiss();
                            Intent intent = (new Intent(CuisineSearch_location.this, RestaurantsListActivity.class));
                            intent.putExtra("restaurantModelArrayList", restaurantModelArrayList);
                            intent.putExtra("passCode", passCode);
                            CuisineSearch_location.this.startActivity(intent);
                            CuisineSearch_location.this.finish();

                        } else {

                            String error = jsonObject.optString("error");
                            String errorMsg = jsonObject.optString("error_msg");
                            showCustomDialog1decline(error);
                            //  Toast.makeText(getActivity(), "" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to Search

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", CITY);
                Log.e("params", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
