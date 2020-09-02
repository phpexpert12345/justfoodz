package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.models.Citymodel;
import com.justfoodz.models.Restaurantlistmodel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.ConnectionDetector;
import com.justfoodz.utils.DatabaseHelper;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class TableBookingActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    LinearLayout restaurantlayout, citylayout, tablelayout, datedialog, timedialog;
    Dialog restaurantdialog, citydialog, tabledialog;
    private ArrayList<Restaurantlistmodel> restaurantList;
    private RestaurantlistAdapter restaurantlistAdapter;
    ProgressDialog pDialog;
    RecyclerView restaurantrecycler, cityrecycler, tablerecycler;
    Citymodel citymodel;
    String CITY, Restaurantid, tableid, CUSTOMERid, RESTRARANTLIST, TABLENO;
    Button submit;
    EditText mobilenumber, noofguests, specialinstructions;
    String MOBILENUMBER, DATE, TIME, NOOFGUESTS, SPECAILINSTRUC;
    AuthPreference authPreference;
    private ArrayList<Citymodel> searchList;
    private ArrayList<Restaurantlistmodel> RestrasearchList;
    private ArrayList<Citymodel> cityList;
    private CITYAdapter cityAdapter;

    private ArrayList<Citymodel> tablenoList;
    private TableNumberAdapter tableNumberAdapter;
    TextView restaurantname, city, tablenumber, error, date, time;
    private int mYear, mMonth, mDay;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    TimePickerDialog timePickerDialog;
    EditText search_view, restra_search_view;
    Restaurantlistmodel restaurantlistmodel;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking);
        cityList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(TableBookingActivity.this);
        restaurantlayout = findViewById(R.id.restaurantlayout);
        citylayout = findViewById(R.id.citylayout);
        tablelayout = findViewById(R.id.tablelayout);
        restaurantname = findViewById(R.id.restaurantname);
        submit = findViewById(R.id.submit);
        city = findViewById(R.id.city);
        tablenumber = findViewById(R.id.tablenumber);
        error = findViewById(R.id.error);
        mobilenumber = findViewById(R.id.mobilenumber);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        iv_back = findViewById(R.id.iv_back);
        noofguests = findViewById(R.id.noofguests);
        datedialog = findViewById(R.id.datedialog);
        timedialog = findViewById(R.id.timedialog);
        specialinstructions = findViewById(R.id.specialinstructions);
        AuthPreference authPreference = new AuthPreference(this);
        CUSTOMERid = authPreference.getCustomerId();
        restaurantlayout.setEnabled(false);
        tablelayout.setEnabled(false);
        restaurantList = new ArrayList<>();
        searchList = new ArrayList<>();
        RestrasearchList = new ArrayList<>();
        restaurantlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRestaurantlistDialog();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        citylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCitylistDialog();
            }
        });
        tablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTablelistDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CITY = city.getText().toString();
                RESTRARANTLIST = restaurantname.getText().toString();
                MOBILENUMBER = mobilenumber.getText().toString();
                DATE = date.getText().toString();
                TIME = time.getText().toString();
                NOOFGUESTS = noofguests.getText().toString();
                SPECAILINSTRUC = specialinstructions.getText().toString();

                if (city.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Your City", Toast.LENGTH_LONG).show();
                } else if (restaurantname.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Your Restaurant Name", Toast.LENGTH_LONG).show();
                } else if (tablenumber.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Your Table Number", Toast.LENGTH_LONG).show();
                } else if (mobilenumber.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                } else if (date.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                } else if (time.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();
                } else if (noofguests.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter  No. of Guests", Toast.LENGTH_LONG).show();
                } else {
                    HitUrlForTableBookingData();
                }
            }
        });

        datedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TableBookingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }

        });
        timedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TableBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
    }

    public void HitUrlForTableBookingData() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.TableBookingAPi, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponsetablebook", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success == 0) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline1(success_msg, "2");
                    } else if (success == 1) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline1(success_msg, "1");

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
                params.put("restaurant_id", Restaurantid);
                params.put("table_number_assign", tableid);
                params.put("booking_mobile", MOBILENUMBER);
                params.put("booking_date", DATE);
                params.put("booking_time", TIME);
                params.put("noofgusts", NOOFGUESTS);
                params.put("booking_instruction", SPECAILINSTRUC);
                params.put("customer_id", CUSTOMERid);
                Log.e("params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void showCitylistDialog() {
        citydialog = new Dialog(this);
        citydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        citydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        citydialog.setContentView(R.layout.tablebookingcitylist);
        citydialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        citydialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        citydialog.setCanceledOnTouchOutside(true);
        cityrecycler = (RecyclerView) citydialog.findViewById(R.id.cityrecycler);
        search_view = (EditText) citydialog.findViewById(R.id.search_view);


        ArrayList<Citymodel> cityListing = databaseHelper.getCityListing();
        if (cityListing.size() > 0) {
            cityList.clear();
            for (int i = 0; i < cityListing.size(); i++) {
                cityList.add(cityListing.get(i));
            }
            if (cityList.size() > 0) {
                CITYAdapter cityAdapter = new CITYAdapter(getApplicationContext(), cityList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                cityrecycler.setLayoutManager(horizontalLayoutManager1);
                cityrecycler.setAdapter(cityAdapter);
            }
        } else {
            if (!Network.isNetworkCheck(this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();

            } else {
                HitURLforCityList();
            }
        }


        search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                for (int i = 0; i < cityList.size(); i++) {
                    String temp = cityList.get(i).getCity_name();
                    temp = temp.toLowerCase();
                    if (temp.contains(cs.toString())) {
                        searchList.clear();
                        String id = cityList.get(i).getId();
                        String name = cityList.get(i).getCity_name();


                        Citymodel citymodel = new Citymodel();
                        citymodel.setCity_name(name);
                        citymodel.setId(id);


                        searchList.add(citymodel);
                    } else {

                    }
                }

                cityAdapter = new CITYAdapter(TableBookingActivity.this, searchList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
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
        citydialog.show();
    }

    public void showRestaurantlistDialog() {
        restaurantdialog = new Dialog(this);
        restaurantdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        restaurantdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        restaurantdialog.setContentView(R.layout.tablebookingrestaurantlist);
        restaurantdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        restaurantdialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        restaurantdialog.setCanceledOnTouchOutside(true);
        restaurantrecycler = (RecyclerView) restaurantdialog.findViewById(R.id.restaurantrecycler);
        restra_search_view = (EditText) restaurantdialog.findViewById(R.id.restra_search_view);

        if (!Network.isNetworkCheck(this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();

        } else {
            HitURLforREstaurantList();
        }
        restra_search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                for (int i = 0; i < restaurantList.size(); i++) {
                    String temp = restaurantList.get(i).getRestaurant_name();
                    temp = temp.toLowerCase();
                    if (temp.contains(cs.toString())) {
                        RestrasearchList.clear();
                        String id = restaurantList.get(i).getId();
                        String name = restaurantList.get(i).getRestaurant_name();
                        RestrasearchList.add(new Restaurantlistmodel(id, name));
                    } else {

                    }
                }

                restaurantlistAdapter = new RestaurantlistAdapter(TableBookingActivity.this, RestrasearchList);
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                restaurantrecycler.setLayoutManager(horizontalLayoutManager1);
                restaurantrecycler.setAdapter(restaurantlistAdapter);
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
        restaurantdialog.show();
    }

    public void showTablelistDialog() {
        tabledialog = new Dialog(this);
        tabledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tabledialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tabledialog.setContentView(R.layout.tablebookingtablenolist);
        tabledialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        tabledialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        tabledialog.setCanceledOnTouchOutside(true);
        tablerecycler = (RecyclerView) tabledialog.findViewById(R.id.tablerecycler);
        ImageView imgcancle = (ImageView) tabledialog.findViewById(R.id.imgcancle);
        imgcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabledialog.dismiss();
            }
        });
        HitURLforTableNumberList();
        tabledialog.show();
    }

    public void HitURLforCityList() {

        cityList=new ArrayList<>();
        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CityListing> userLoginCall = interfaceRetrofit.citylising(UrlConstants.City);
        userLoginCall.enqueue(new Callback<Model_CityListing>() {
            @Override
            public void onResponse(Call<Model_CityListing> call, retrofit2.Response<Model_CityListing> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getCityList().size(); i++) {
                        Citymodel model_cityDetail = response.body().getCityList().get(i);
                        if (model_cityDetail.getError().equalsIgnoreCase("0")) {
                            cityList.add(model_cityDetail);
                            databaseHelper.saveCityDetails(model_cityDetail);
                        }
                    }
                    if (cityList.size() > 0) {
                        CITYAdapter cityAdapter = new CITYAdapter(TableBookingActivity.this, cityList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                        cityrecycler.setLayoutManager(horizontalLayoutManager1);
                        cityrecycler.setAdapter(cityAdapter);
                    }
                }
                MyProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Model_CityListing> call, Throwable t) {
                Toast.makeText(TableBookingActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


    }

    public void HitURLforREstaurantList() {
        CITY = city.getText().toString();

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_restaurant_list> userLoginCall = interfaceRetrofit.phpexpert_get_restaurant_list(CITY);
        userLoginCall.enqueue(new Callback<Model_restaurant_list>() {
            @Override
            public void onResponse(Call<Model_restaurant_list> call, retrofit2.Response<Model_restaurant_list> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getRestaurantList().size(); i++) {
                        Restaurantlistmodel restaurantlistmodel = response.body().getRestaurantList().get(i);

                        if (!restaurantlistmodel.getError().equalsIgnoreCase("1")) {

                            restaurantList.add(new Restaurantlistmodel(restaurantlistmodel.getId(), restaurantlistmodel.getRestaurant_name()));

                        } else {
                            showCustomDialog1decline(restaurantlistmodel.getError_msg());
                            restaurantdialog.dismiss();
                        }

                    }

                    if (restaurantList.size() > 0) {
                        RestaurantlistAdapter restaurantlistAdapter = new RestaurantlistAdapter(TableBookingActivity.this, restaurantList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                        restaurantrecycler.setLayoutManager(horizontalLayoutManager1);
                        restaurantrecycler.setAdapter(restaurantlistAdapter);
                    }
                }

                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_restaurant_list> call, Throwable t) {
                MyProgressDialog.dismiss();
                restaurantdialog.dismiss();
                Toast.makeText(TableBookingActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



/*
        CITY = city.getText().toString();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);
        restaurantList = new ArrayList<Restaurantlistmodel>();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GetRestaurantList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray citylist = jsonObj.getJSONArray("RestaurantList");
                    for (int i = 0; i < citylist.length(); i++) {
                        JSONObject c = citylist.getJSONObject(i);
                        int error = c.getInt("error");
                        if (error == 1) {
                            String error_msg = c.getString("error_msg");
                            showCustomDialog1decline(error_msg);
                            restaurantdialog.dismiss();
                            pDialog.dismiss();
                        } else {
                            String id = c.getString("id");
                            String rrr = c.getString("restaurant_id");
                            String restaurant_name = c.getString("restaurant_name");
                            String restaurantCity = c.getString("restaurantCity");
//                            authPreference=new AuthPreference(TableBookingActivity.this);
//                            authPreference.setRestrarantid(Restaurantid);
                            restaurantList.add(new Restaurantlistmodel(id, restaurant_name));
                            RestaurantlistAdapter restaurantlistAdapter = new RestaurantlistAdapter(TableBookingActivity.this, restaurantList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                            restaurantrecycler.setLayoutManager(horizontalLayoutManager1);
                            restaurantrecycler.setAdapter(restaurantlistAdapter);
                            pDialog.dismiss();
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
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurantCity", CITY);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/


    }

    public void HitURLforTableNumberList() {
        tablenoList=new ArrayList<>();

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_RestaurantTableList> userLoginCall = interfaceRetrofit.phpexpert_get_restaurant_table_list(Restaurantid);
        userLoginCall.enqueue(new Callback<Model_RestaurantTableList>() {
            @Override
            public void onResponse(Call<Model_RestaurantTableList> call, retrofit2.Response<Model_RestaurantTableList> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getRestaurantTableList().size(); i++) {
                        Citymodel citymodel = response.body().getRestaurantTableList().get(i);
                        if (!citymodel.getError().equalsIgnoreCase("1")) {
                            tablenoList.add(citymodel);
                            TableNumberAdapter tableNumberAdapter = new TableNumberAdapter(TableBookingActivity.this, tablenoList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                            tablerecycler.setLayoutManager(horizontalLayoutManager1);
                            tablerecycler.setAdapter(tableNumberAdapter);


                        } else {
                            showCustomDialog1decline(citymodel.getError_msg());
                            tabledialog.dismiss();

                        }
                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_RestaurantTableList> call, Throwable t) {
                MyProgressDialog.dismiss();
                Toast.makeText(TableBookingActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

/*
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);
        tablenoList = new ArrayList<Citymodel>();
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.GetRestaurantTableNumberList, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray citylist = jsonObj.getJSONArray("RestaurantTableList");
                    for (int i = 0; i < citylist.length(); i++) {
                        JSONObject c = citylist.getJSONObject(i);
                        int error = c.getInt("error");
                        if (error == 1) {
                            String error_msg = c.getString("error_msg");
                            showCustomDialog1decline(error_msg);
                            tabledialog.dismiss();
                            pDialog.dismiss();
                        } else {
                            String ttableid = c.getString("id");
                            String restaurant_id = c.getString("restaurant_id");
                            String table_caption = c.getString("table_caption");
                            String restaurantCity = c.getString("table_number");
                            String table_no_guest_available = c.getString("table_no_guest_available");
                            String tabledisvplay = c.getString("Table_display_view");


                            Citymodel citymodel = new Citymodel();
                            citymodel.setCity_name(tabledisvplay);
                            citymodel.setId(ttableid);
                            tablenoList.add(citymodel);
                            TableNumberAdapter tableNumberAdapter = new TableNumberAdapter(TableBookingActivity.this, tablenoList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(TableBookingActivity.this, LinearLayoutManager.VERTICAL, false);
                            tablerecycler.setLayoutManager(horizontalLayoutManager1);
                            tablerecycler.setAdapter(tableNumberAdapter);
                            pDialog.dismiss();
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
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", Restaurantid);
                Log.e("paramtable", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);*/
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(TableBookingActivity.this).create();
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

    private void showCustomDialog1decline1(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(TableBookingActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    Intent intent = new Intent(TableBookingActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public class CITYAdapter extends RecyclerView.Adapter<CITYAdapter.ViewHolder> implements Filterable {

        Context context;
        private ArrayList<Citymodel> cityList;
        private int lastSelectedPosition = -1;
        private ArrayList<Citymodel> filterList;


        public CITYAdapter(Context c, ArrayList<Citymodel> cityList) {
            this.context = c;
            this.cityList = cityList;
            this.filterList = cityList;
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

            citymodel = cityList.get(position);
            holder.tv_city_name.setText(cityList.get(position).getCity_name());

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
                            restaurantlayout.setEnabled(true);
                            citydialog.dismiss();
//                            pDialog.dismiss();
                        }
                    }, 1000);


                }
            });
        }

        @Override
        public int getItemCount() {
            return cityList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterList = cityList;
                    } else {
                        ArrayList<Citymodel> myList = new ArrayList<>();
                        for (Citymodel temp : cityList) {
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

    public class RestaurantlistAdapter extends RecyclerView.Adapter<RestaurantlistAdapter.ViewHolder> implements Filterable {

        Context context;
        private ArrayList<Restaurantlistmodel> restaurantList;
        private int lastSelectedPosition = -1;
        private ArrayList<Restaurantlistmodel> filterListR;


        public RestaurantlistAdapter(Context c, ArrayList<Restaurantlistmodel> restaurantList) {
            this.context = c;
            this.restaurantList = restaurantList;
            this.filterListR = restaurantList;

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


            restaurantlistmodel = restaurantList.get(position);
            holder.tv_city_name.setText(restaurantList.get(position).getRestaurant_name());


            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.citychecked.setChecked(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Restaurantid = restaurantList.get(position).getId();
                            lastSelectedPosition = position;
                            notifyDataSetChanged();
                            restaurantname.setText(holder.tv_city_name.getText());
                            tablelayout.setEnabled(true);
                            restaurantdialog.dismiss();
                        }
                    }, 1000);


                }
            });
        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterListR = restaurantList;
                    } else {
                        ArrayList<Restaurantlistmodel> myList = new ArrayList<>();
                        for (Restaurantlistmodel temp : restaurantList) {
                            if (temp.getRestaurant_name().toLowerCase().contains(charString)) ;
                            filterListR.add(temp);
                        }
                        filterListR = myList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterListR;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filterListR = (ArrayList<Restaurantlistmodel>) results.values;
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

    public class TableNumberAdapter extends RecyclerView.Adapter<TableNumberAdapter.ViewHolder> {

        Context context;
        private ArrayList<Citymodel> tablenoList;
        private int lastSelectedPosition = -1;

        public TableNumberAdapter(Context c, ArrayList<Citymodel> tablenoList) {
            this.context = c;
            this.tablenoList = tablenoList;
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


            citymodel = tablenoList.get(position);
            holder.tv_city_name.setText(tablenoList.get(position).getCity_name());


            holder.citychecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.citychecked.setChecked(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tableid = tablenoList.get(position).getId();
                            lastSelectedPosition = position;
                            notifyDataSetChanged();
                            tablenumber.setText(holder.tv_city_name.getText());
                            tabledialog.dismiss();
                            pDialog.dismiss();
                        }
                    }, 1000);


                }
            });
        }

        @Override
        public int getItemCount() {
            return tablenoList.size();
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
}
