package com.justfoodz.activities;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.justfoodz.adapters.MyOrderAdapter;
import com.justfoodz.models.OrderModel;
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

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private RecyclerView rvOrder;
    private MyOrderAdapter myOrderAdapter;
    public static ArrayList<OrderModel> orderModelArrayList;
    public static OrderModel orderModel;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;

    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;
    public static String c_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        emptytxt = findViewById(R.id.emptytxt);
        imageblank = findViewById(R.id.imageblank);
        layout2 = findViewById(R.id.layout);
        statusBarColor();
        initialization();
        initializedListener();
        authPreference = new AuthPreference(this);
        c_id = authPreference.getCustomerId();
        if (Network.isNetworkCheck(this)) {
            displayMyOrder();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        rvOrder = findViewById(R.id.rv_order);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvOrder.setLayoutManager(mLayoutManager);
        rvOrder.setItemAnimator(new DefaultItemAnimator());

    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            default:

        }

    }

    private void displayMyOrder() {
        orderModelArrayList = new ArrayList<OrderModel>();
        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_OrderModelListing> userLoginCall = interfaceRetrofit.phpexpert_OrderDisplay(authPreference.getCustomerId());
        userLoginCall.enqueue(new Callback<Model_OrderModelListing>() {
            @Override
            public void onResponse(Call<Model_OrderModelListing> call, retrofit2.Response<Model_OrderModelListing> response) {
                if (response.isSuccessful()) {

                    if (!response.body().getSuccess().equalsIgnoreCase("1")) {

                        for (int i = 0; i < response.body().getOrders().getOrderViewResult().size(); i++) {
                            OrderModel orderModel = response.body().getOrders().getOrderViewResult().get(i);
                            orderModelArrayList.add(orderModel);
                        }
                        rvOrder.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);

                        if (orderModelArrayList.size() > 0) {
                            myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, orderModelArrayList);
                            rvOrder.setAdapter(myOrderAdapter);
                        }

                    } else {
                        rvOrder.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText(response.body().getSuccess_msg());

                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_OrderModelListing> call, Throwable t) {
                Toast.makeText(MyOrderActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


       /* pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        orderModelArrayList = new ArrayList<OrderModel>();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ORDER_LISTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("orderresponse", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String success_msg = jObj.getString("success_msg");
                        rvOrder.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText(success_msg);


                    } else {
                        rvOrder.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);
                        JSONObject orders = jObj.getJSONObject("orders");
                        JSONArray jsonArray = orders.getJSONArray("OrderViewResult");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String orderIdentifyno = jsonObject.getString("order_identifyno");
                            String order_time = jsonObject.getString("order_time");
                            String restaurant_id = jsonObject.getString("restaurant_id");
                            String ordPrice = jsonObject.getString("ordPrice");
                            String rider_otp = jsonObject.getString("rider_otp");
                            String order_type = jsonObject.getString("order_type");
                            String payment_mode = jsonObject.getString("payment_mode");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String restaurant_address = jsonObject.getString("restaurant_address");
                            String order_display_message = jsonObject.getString("order_display_message");
                            String order_date = jsonObject.getString("order_date");
                            String DriverID = jsonObject.getString("DriverID");
                            String rider_review = jsonObject.getString("rider_review");
                            String RiderRating = jsonObject.getString("RiderRating");
                            String rider_order_close = jsonObject.getString("rider_order_close");
                            String Favorites_display = jsonObject.getString("Favorites_display");

                            orderModel = new OrderModel();
                            orderModel.setRestaurant_name(restaurant_name);
                            orderModel.setOrder_identifyno(orderIdentifyno);
                            orderModel.setOrdPrice(ordPrice);
                            orderModel.setRestaurant_name(restaurant_name);
                            orderModel.setRestaurant_address(restaurant_address);
                            orderModel.setPayment_mode(payment_mode);
                            orderModel.setOrder_type(order_type);
                            orderModel.setOrder_date(order_date);
                            orderModel.setOrder_time(order_time);
                            orderModel.setRestaurant_id(restaurant_id);
                            orderModel.setOrder_display_message(order_display_message);
                            orderModel.setDriverID(DriverID);
                            orderModel.setRiderOtp(rider_otp);
                            orderModel.setRiderReview(rider_review);
                            orderModel.setRiderRating(RiderRating);
                            orderModel.setrider_order_close(rider_order_close);
                            orderModel.setFavorites_display(Favorites_display);
                            orderModelArrayList.add(orderModel);
                        }

                        myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, orderModelArrayList);
                        rvOrder.setAdapter(myOrderAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ordere", "" + e);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("CustomerId", customerId);
                Log.e("qw", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }


    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    @Override
    public void onBackPressed() {
        finish();
        // super.onBackPressed();
    }
}
