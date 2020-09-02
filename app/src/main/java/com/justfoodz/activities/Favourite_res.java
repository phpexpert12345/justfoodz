package com.justfoodz.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.fragments.HomeFragment;
import com.justfoodz.models.RestaurantModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
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

public class Favourite_res extends AppCompatActivity {

    private RecyclerView rcfav;
    private ArrayList<RestaurantModel> restaurantModelArrayList;
    ImageView iv_back;
    private AuthPreference authPreference;
    public   static RestaurantModel restaurantModel;
    ProgressDialog pDialog,progressDialog;
    RequestQueue requestQueue;
    LinearLayout llrc,llnodata;
    TextView txtnodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_res);
        authPreference = new AuthPreference(this);
        requestQueue = Volley.newRequestQueue(this);
        rcfav = findViewById(R.id.rcfav);
        iv_back=findViewById(R.id.iv_back);
        llrc=findViewById(R.id.llrc);
        llnodata=findViewById(R.id.llnodata);
        txtnodata=findViewById(R.id.txtnodata);
        restaurantModelArrayList = new ArrayList<>();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (Network.isNetworkCheck(this)) {
            favList();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void favList() {
        restaurantModelArrayList.clear();
        pDialog = new ProgressDialog(Favourite_res.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
        restaurantModelArrayList = new ArrayList<RestaurantModel>();
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.fav_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("fav",""+response);
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
                            String SocialSharingMessage= jsonObject.optString("SocialSharingMessage");
                            String restaurant_distance_check=jsonObject.getString("restaurant_distance_check");
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
                            restaurantModel.setRestaurantTimeStatusText(RestaurantTimeStatusText);
                            //////////
                            restaurantModelArrayList.add(restaurantModel);
                            RestaurantListAdapter restaurantListAdapter = new  RestaurantListAdapter(Favourite_res.this,restaurantModelArrayList);
                            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(Favourite_res.this, LinearLayoutManager.VERTICAL, false);
                            rcfav.setLayoutManager(horizontalLayoutManager1);
                            rcfav.setAdapter(restaurantListAdapter);

                        } else {
                            llrc.setVisibility(View.GONE);
                            llnodata.setVisibility(View.VISIBLE);
                            String errorMsg = jsonObject.optString("error_msg");
                            txtnodata.setText(errorMsg);
                            }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("eeee",""+e);
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
                params.put("CustomerID", ""+ authPreference.getCustomerId());
                Log.e("params",""+params);
                return params;
            }

        };
        requestQueue.add(strReq);
    }

    public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {
        private Context context;
        private ArrayList<RestaurantModel> restaurantModelArrayList;//original list
        private RestaurantModel restaurantModel;
        //    String pound = "\u00a3";
        String pound=SplashScreenActivity.currency_symbol;
        private ArrayList<RestaurantModel> restaurantArrayListFiltered;

        SharedPreferences sharedPreferences;

        public RestaurantListAdapter(Context context, ArrayList<RestaurantModel> restaurantModelArrayList) {
            this.context = context;
            this.restaurantModelArrayList = restaurantModelArrayList;
            this.restaurantArrayListFiltered = restaurantModelArrayList;

        }

        @NonNull
        @Override
        public RestaurantListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fav_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            sharedPreferences = context.getSharedPreferences("HotelName",MODE_PRIVATE);
            holder.rlRestaurantList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestaurantsListActivity.res_lat = restaurantModelArrayList.get(position).getRestaurant_LatitudePoint();
                    RestaurantsListActivity.res_lng = restaurantModelArrayList.get(position).getRestaurant_LongitudePoint();
                    RestaurantsListActivity.cash_avaible = restaurantModelArrayList.get(position).getRestaurant_onlycashonAvailable();
                    RestaurantsListActivity.card_avaible = restaurantModelArrayList.get(position).getRestaurant_cardacceptAvailable();
                    HomeFragment.restaurant_distance_check = restaurantModelArrayList.get(position).getrestaurant_distance_check();
                    Intent intent = new Intent(context, MainMenuActivity.class);
                    String id = restaurantModelArrayList.get(position).getId();
                    RestaurantModel restaurantModel = restaurantModelArrayList.get(position);
                    authPreference.setRestraurantCity(""+restaurantModelArrayList.get(position).getRestaurantCity());
                    authPreference.setMinprice(restaurantModelArrayList.get(position).getRestaurant_minimumorder());
                    intent.putExtra("restaurantModel", (Serializable) restaurantModel);
                    intent.putExtra("restaurantModelArrayListReceived", restaurantModelArrayList);
                    intent.putExtra("ratingValue", restaurantModel.getRatingValue());
                    intent.putExtra("TotalRestaurantReview", restaurantModel.getTotalRestaurantReview());
                    startActivity(intent);


                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    int pos = holder.getAdapterPosition();
                    String tempName = restaurantArrayListFiltered.get(pos).getSocialSharingMessage();
//                String tempAddress = restaurantArrayListFiltered.get(pos).getRestaurant_address();
//                String tempImage = restaurantArrayListFiltered.get(pos).getRestaurant_Logo();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order Food");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tempName);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
            });

            holder.imgfav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFav(restaurantModelArrayList.get(position).getId());
                }
            });

            try {
                restaurantModel = restaurantModelArrayList.get(position);
                holder.tvRestaurantName.setText(restaurantArrayListFiltered.get(position).getRestaurant_name());
                holder.tvRestaurantAddress.setText(restaurantModel.getRestaurant_address());
                holder.tvRestaurantMinOrderPrice.setText(pound.concat(restaurantModel.getRestaurant_minimumorder()));
                holder.tvItemDiscountCost.setText((restaurantModel.getRestaurantOfferPrice()));
//

                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Open")) {
                    holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.openred));
                }
                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Preorder")) {
                    holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.preordergreen));

                }
                if (restaurantModel.getRestaurantTimeStatus1().equalsIgnoreCase("Closed")) {
                    holder.tvItemOrderStatus.setImageDrawable(context.getDrawable(R.drawable.closedred));

                }
                String image = restaurantModel.getRestaurant_Logo();
                Picasso.get().load(restaurantModel.getRestaurant_Logo()).into(holder.ivRestaurantLogo);
                String ratingCount = (restaurantModel.getRatingValue());
                if (ratingCount.equalsIgnoreCase("0")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_star);
                    holder.review_2.setBackgroundResource(R.drawable.review_star);
                    holder.review_3.setBackgroundResource(R.drawable.review_star);
                    holder.review_4.setBackgroundResource(R.drawable.review_star);
                    holder.review_5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingCount.equalsIgnoreCase("1")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_2.setBackgroundResource(R.drawable.review_star);
                    holder.review_3.setBackgroundResource(R.drawable.review_star);
                    holder.review_4.setBackgroundResource(R.drawable.review_star);
                    holder.review_5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingCount.equalsIgnoreCase("2")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_3.setBackgroundResource(R.drawable.review_star);
                    holder.review_4.setBackgroundResource(R.drawable.review_star);
                    holder.review_5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingCount.equalsIgnoreCase("3")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_4.setBackgroundResource(R.drawable.review_star);
                    holder.review_5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingCount.equalsIgnoreCase("4")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_4.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_5.setBackgroundResource(R.drawable.review_star);

                } else if (ratingCount.equalsIgnoreCase("5")) {
                    holder.review_1.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_2.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_3.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_4.setBackgroundResource(R.drawable.review_yellow);
                    holder.review_5.setBackgroundResource(R.drawable.review_yellow);
                }

                //////icon/////
                if (restaurantModel.getKidFriendly().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.kidgreen.setVisibility(View.VISIBLE);
                    holder.kidred.setVisibility(View.GONE);

                }else {
                    holder.kidred.setVisibility(View.VISIBLE);
                    holder.kidgreen.setVisibility(View.GONE);
                }

                if (restaurantModel.getPetFriendly().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.petgreen.setVisibility(View.VISIBLE);
                    holder.petred.setVisibility(View.GONE);

                }else {
                    holder.petred.setVisibility(View.VISIBLE);
                    holder.petgreen.setVisibility(View.GONE);
                }

                if (restaurantModel.getDelivery().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.deliverycheck.setVisibility(View.VISIBLE);
                    holder.deliverynotcheck.setVisibility(View.GONE);

                }else {
                    holder.deliverynotcheck.setVisibility(View.VISIBLE);
                    holder.deliverycheck.setVisibility(View.GONE);
                }

                if (restaurantModel.getCollection().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.collectioncheck.setVisibility(View.VISIBLE);
                    holder.collectionnotcheck.setVisibility(View.GONE);

                }else {
                    holder.collectionnotcheck.setVisibility(View.VISIBLE);
                    holder.collectioncheck.setVisibility(View.GONE);
                }
                if (restaurantModel.getDINEin().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.dinincheck.setVisibility(View.VISIBLE);
                    holder.dininnotcheck.setVisibility(View.GONE);

                }else {
                    holder.dininnotcheck.setVisibility(View.VISIBLE);
                    holder.dinincheck.setVisibility(View.GONE);
                }

                if (restaurantModel.getBookaTable().equalsIgnoreCase("Yes")) {
//                holder.tvItemOrderStatus.setTextColor(Color.parseColor("#147708"));
                    holder.booktablecheck.setVisibility(View.VISIBLE);
                    holder.booktablenotcheck.setVisibility(View.GONE);

                }else {
                    holder.booktablenotcheck.setVisibility(View.VISIBLE);
                    holder.booktablecheck.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            if (restaurantModelArrayList != null) {
                return restaurantArrayListFiltered.size();
            } else {
                return 0;
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout rlRestaurantList;

            private TextView tvRestaurantName, tvRestaurantMinOrderPrice,
                    tvRestaurantAddress, tvItemDiscount, tvItemDiscountCost;
            private ImageView ivRestaurantLogo, review_1, review_2, review_3, review_4, review_5;
            private  ImageView kidred,kidgreen,petred,petgreen,deliverynotcheck,deliverycheck,collectioncheck
                    ,collectionnotcheck,dininnotcheck,dinincheck,booktablenotcheck,booktablecheck;
            public  ImageView tvItemOrderStatus,share,imgfav;

            public MyViewHolder(View itemView) {
                super(itemView);
                rlRestaurantList = itemView.findViewById(R.id.rl_restaurant_list);
                tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
                tvRestaurantMinOrderPrice = itemView.findViewById(R.id.tv_restaurant_min_order_price);
                tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurant_address);
                tvItemOrderStatus = itemView.findViewById(R.id.tv_item_order_status);
                tvItemDiscount = itemView.findViewById(R.id.tv_item_discount);
                tvItemDiscountCost = itemView.findViewById(R.id.tv_item_discount_cost);
                ivRestaurantLogo = itemView.findViewById(R.id.iv_restaurant_logo);
                review_1 = itemView.findViewById(R.id.review_1);
                review_2 = itemView.findViewById(R.id.review_2);
                review_3 = itemView.findViewById(R.id.review_3);
                review_4 = itemView.findViewById(R.id.review_4);
                review_5 = itemView.findViewById(R.id.review_5);
                kidred=itemView.findViewById(R.id.kidred);
                kidgreen=itemView.findViewById(R.id.kidgreen);
                petred=itemView.findViewById(R.id.petred);
                petgreen=itemView.findViewById(R.id.petgreen);
                deliverynotcheck=itemView.findViewById(R.id.deliverynotcheck);
                deliverycheck=itemView.findViewById(R.id.deliverycheck);
                collectioncheck=itemView.findViewById(R.id.collectioncheck);
                collectionnotcheck=itemView.findViewById(R.id.collectionnotcheck);
                dininnotcheck=itemView.findViewById(R.id.dininnotcheck);
                dinincheck=itemView.findViewById(R.id.dinincheck);
                booktablenotcheck=itemView.findViewById(R.id.booktablenotcheck);
                booktablecheck=itemView.findViewById(R.id.booktablecheck);
                share=itemView.findViewById(R.id.share);
                imgfav=itemView.findViewById(R.id.imgfav);
            }
        }
    }

    private void getFav(final String a){
        progressDialog = progressDialog.show(Favourite_res.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.add_remove_fav, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rere",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error = jsonObject.getInt("error");
                    if (error==1){
//                        favList();
                        Intent i = new Intent(Favourite_res.this,Favourite_res.class);
                        startActivity(i);
                        finish();
                    }else {
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(Favourite_res.this, ""+error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("CustomerId",""+authPreference.getCustomerId());
                params.put("restaurant_id",""+a);
                params.put("favorite_action_type","delete");
                Log.e("qwerty",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}