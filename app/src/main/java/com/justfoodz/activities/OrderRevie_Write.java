package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.justfoodz.R;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRevie_Write extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvPublishReview;
    private TextView txtrating;
    private MaterialEditText edtWriteReview;
    private String  writeReview;
    private ProgressDialog pDialog;

    RatingBar ratingBar;
    ArrayList<String> name;
    ArrayList<String> id;
    String rating_review_tosend="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writereview);

        statusBarColor();
        initialization();
        initializedListener();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_review_tosend = String.valueOf(rating);
                txtrating.setText(""+rating_review_tosend+" Star");
            }
        });
    }

    private void initialization() {
        name = new ArrayList<>();
        id = new ArrayList<>();
        ivBack = findViewById(R.id.iv_back);
        txtrating = findViewById(R.id.txtrating);
        edtWriteReview = findViewById(R.id.edt_write_review);
        tvPublishReview = findViewById(R.id.tv_publish_review);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvPublishReview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_publish_review:
                writeReviewValidation();
                break;
            default:
        }
    }






    public void writeReviewValidation() {
        writeReview = edtWriteReview.getText().toString().trim();
        if (rating_review_tosend.equals("0")){
            showCustomDialog1decline ("Please select star rating","2");
        } else if (writeReview.isEmpty()) {
            edtWriteReview.setError("Write your comment");
            edtWriteReview.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(OrderRevie_Write.this)) {
            writeReview();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void writeReview() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.Order_Review, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("response",""+response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String success_msg = jObj.optString("success_msg");
                        String resId = jObj.optString("resid");
                        String customerId = jObj.optString("CustomerId");
                        String orderIdentifyNo = jObj.optString("order_identifyno");
                        String restaurantReviewRating = jObj.optString("RestaurantReviewRating");
                        String qualityRatingN = jObj.optString("Quality_ratingN");
                        String serviceRatingN = jObj.optString("Service_ratingN");
                        String timeRatingN = jObj.optString("Time_ratingN");
                        String restaurantReviewContent = jObj.optString("RestaurantReviewContent");
                        String restaurantReviewName = jObj.optString("RestaurantReviewName");
                        String ratingAvg = jObj.optString("ratingAvg");
                        showCustomDialog1decline (success_msg,"1");
                    } else if (success == 0) {
                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline (error_msg,"2");
                    } else if (success == 2) {
                        String success_msg = jObj.getString("success_msg");
                        showCustomDialog1decline (success_msg,"2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", ""+getIntent().getStringExtra("orderIdentifyNo"));
                params.put("RiderRating", rating_review_tosend);
                params.put("RiderRatingComment", writeReview);
                Log.e("param",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showCustomDialog1decline (String s, final String todo)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(OrderRevie_Write.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1"))
                {
//                    startActivity(new Intent(OrderRevie.this, HomeActivity.class));
                    finish();
                }
                else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

}