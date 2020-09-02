package com.justfoodz.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.R;
import com.justfoodz.adapters.ReviewAdapter;
import com.justfoodz.models.NewReviewModel;
import com.justfoodz.models.ReviewModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReviewFragment extends Activity {
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    private RecyclerView rv_review;
    private ReviewAdapter reviewAdapter;


    private ArrayList<NewReviewModel> reviewModelArrayList;
    String id;
    ImageView iv_back;
    LinearLayout llwithoutdata;
    TextView txtmsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_review);
        iv_back = findViewById(R.id.iv_back);
        authPreference = new AuthPreference(this);
        rv_review = findViewById(R.id.rv_review);
        llwithoutdata = findViewById(R.id.llwithoutdata);
        txtmsg = findViewById(R.id.txtmsg);
        reviewModelArrayList = new ArrayList<>();

        final Intent intent = getIntent();
        id = getIntent().getStringExtra("id");

        iv_back.setOnClickListener(view -> onBackPressed());

        if (Network.isNetworkCheck(this)) {
            reviewList();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }
String kkk = "{\"reviewlistingList\":[\n" +
        "    {\n" +
        "        \"deliveryrating\": \"\",\n" +
        "        \"friendlinessrating\": \"\",\n" +
        "        \"foodqualityrating\": \"\",\n" +
        "        \"RestaurantReviewRating\": \"3.0\",\n" +
        "        \"restaurant_name\": \" Calamar Fish & Chips\",\n" +
        "        \"customerName\": \"Lamon Mangal\",\n" +
        "        \"success\": \"0\",\n" +
        "        \"success_msg\": \"success\",\n" +
        "        \"review_id\": 3,\n" +
        "        \"reviewpostedDate\": \"22-07-2020\",\n" +
        "        \"customerReviewComment\": \"fully enjoyable\",\n" +
        "        \"ReviewGalleryPhoto\": [\n" +
        "            {\n" +
        "                \"error\": \"0\",\n" +
        "                \"error_msg\": \"success\",\n" +
        "                \"review_img\": \"https:\\/\\/www.justfoodz.com\\/reviewimguploads\\/5f1848d41604920200612_145910.jpg\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    {\n" +
        "        \"deliveryrating\": \"\",\n" +
        "        \"friendlinessrating\": \"\",\n" +
        "        \"foodqualityrating\": \"\",\n" +
        "        \"RestaurantReviewRating\": \"5.0\",\n" +
        "        \"restaurant_name\": \" Calamar Fish & Chips\",\n" +
        "        \"customerName\": \"Lamon Mangal\",\n" +
        "        \"success\": \"0\",\n" +
        "        \"success_msg\": \"success\",\n" +
        "        \"review_id\": 2,\n" +
        "        \"reviewpostedDate\": \"22-07-2020\",\n" +
        "        \"customerReviewComment\": \"service was excellent\",\n" +
        "        \"ReviewGalleryPhoto\": [\n" +
        "            {\n" +
        "                \"error\": \"0\",\n" +
        "                \"error_msg\": \"success\",\n" +
        "                \"review_img\": \"https:\\/\\/www.justfoodz.com\\/reviewimguploads\\/5f18489db8e0020200630_153135.jpg\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    {\n" +
        "        \"deliveryrating\": \"\",\n" +
        "        \"friendlinessrating\": \"\",\n" +
        "        \"foodqualityrating\": \"\",\n" +
        "        \"RestaurantReviewRating\": \"4.0\",\n" +
        "        \"restaurant_name\": \" Calamar Fish & Chips\",\n" +
        "        \"customerName\": \"Lamon Mangal\",\n" +
        "        \"success\": \"0\",\n" +
        "        \"success_msg\": \"success\",\n" +
        "        \"review_id\": 1,\n" +
        "        \"reviewpostedDate\": \"22-07-2020\",\n" +
        "        \"customerReviewComment\": \"good food\",\n" +
        "        \"ReviewGalleryPhoto\": [\n" +
        "            {\n" +
        "                \"error\": \"0\",\n" +
        "                \"error_msg\": \"success\",\n" +
        "                \"review_img\": \"https:\\/\\/www.justfoodz.com\\/reviewimguploads\\/5f18485e4684520200708_152258.jpg\"\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "]}";
    @Override
    public void onBackPressed() {
        finish();
    }
    String review_img="";
    public void reviewList() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        reviewModelArrayList.clear();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_REVIEW_RESTAURANT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("reviewresponse", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (!jObj.has("success_msg")) {
                       // JSONObject review = jObj.getJSONObject("review");
                        JSONArray jsonArray = jObj.getJSONArray("reviewlistingList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NewReviewModel reviewModel = new NewReviewModel();
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.optInt("id");
                                int review_id = jsonObject.optInt("review_id");
                                String deliveryrating = jsonObject.optString("deliveryrating");

                                JSONArray jsonArray1 = jsonObject.getJSONArray("ReviewGalleryPhoto");
                                for (int j = 0; j<jsonArray1.length(); j++)
                                {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    review_img =  jsonObject1.optString("review_img");
                                    reviewModel.setReview_img(review_img);
                                }

                                // String review_img = jsonObject.optString("review_img");
                                String friendlinessrating = jsonObject.optString("friendlinessrating");
                                String foodqualityrating = jsonObject.optString("foodqualityrating");
                                String restaurant_name = jsonObject.optString("restaurant_name");
                                String customerName = jsonObject.optString("customerName");
                                String RestaurantReviewRating = jsonObject.optString("RestaurantReviewRating");
                                String reviewpostedDate = jsonObject.optString("reviewpostedDate");
                                String customerReviewComment = jsonObject.optString("customerReviewComment");

                                reviewModel.setId(id);
                                reviewModel.setReview_id(review_id);
                                reviewModel.setDeliveryrating(deliveryrating);
                                reviewModel.setFriendlinessrating(friendlinessrating);
                                reviewModel.setFoodqualityrating(foodqualityrating);
                                reviewModel.setRestaurant_name(restaurant_name);
                                reviewModel.setCustomerName(customerName);
                                reviewModel.setRestaurantReviewRating(RestaurantReviewRating);
                                reviewModel.setReviewpostedDate(reviewpostedDate);
                                reviewModel.setCustomerReviewComment(customerReviewComment);
                                reviewModelArrayList.add(reviewModel);
                            }
                            catch (JSONException  ex)
                            {
                                ex.printStackTrace();
                            }


                        //    reviewModelArrayList.add(new ReviewModel(id,review_id,deliveryrating,review_img,friendlinessrating,
                            //    foodqualityrating,restaurant_name,customerName,RestaurantReviewRating,reviewpostedDate,customerReviewComment));
                        }
                        ReviewAdapter reviewAdapter = new ReviewAdapter(ReviewFragment.this, reviewModelArrayList);
                        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(ReviewFragment.this, LinearLayoutManager.VERTICAL, false);
                        rv_review.setLayoutManager(horizontalLayoutManager1);
                        rv_review.setAdapter(reviewAdapter);


                    } else {
                        String success_msg = jObj.optString("success_msg");
                        rv_review.setVisibility(View.GONE);
                        llwithoutdata.setVisibility(View.VISIBLE);
                        txtmsg.setText(""+success_msg);
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
            protected Map<String, String> getParams() {
                String restaurantId = (authPreference.getRestaurantId());
                // Posting params to Search
                Map<String, String> params = new HashMap<String, String>();
                params.put("resid", id);
                Log.e("idddd", id);

                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


}
