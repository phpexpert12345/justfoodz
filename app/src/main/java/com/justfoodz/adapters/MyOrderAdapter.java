package com.justfoodz.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.activities.MyOrderActivity;
import com.justfoodz.activities.OrderDetailsDisplayActivity;
import com.justfoodz.activities.OrderRevie_Write;
import com.justfoodz.activities.OrderTrackDetailActivity;
;
import com.justfoodz.R;
import com.justfoodz.activities.SplashScreenActivity;
import com.justfoodz.activities.TrackOrder_activity;
import com.justfoodz.models.OrderModel;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<OrderModel> mOrderModelArrayList;
    private OrderModel orderModel;
    private String pound = SplashScreenActivity.currency_symbol;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;


    public MyOrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.mOrderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyViewHolder holder, final int position) {
        orderModel = mOrderModelArrayList.get(position);

        holder.tvOrderTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderModel = mOrderModelArrayList.get(position);
                Intent intent = new Intent(context, OrderTrackDetailActivity.class);
                intent.putExtra("orderIdentifyNo", orderModel.getOrder_identifyno());
                context.startActivity(intent);

            }
        });
        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderModel = mOrderModelArrayList.get(position);
                Intent i = new Intent(context, OrderDetailsDisplayActivity.class);
                i.putExtra("orderIdentifyNo", orderModel.getOrder_identifyno());
                context.startActivity(i);
            }
        });
        holder.tv_order_track_driver.setOnClickListener(v -> {
            orderModel = mOrderModelArrayList.get(position);
            if (orderModel.getrider_order_close().equals("1")) {
                showCustomDialog1decline("Order already delivered");
            } else {
                if (orderModel.getDriverID().equals("0")) {
                    Intent i = new Intent(context, TrackOrder_activity.class);
                    i.putExtra("orderIdentifyNo", orderModel.getOrder_identifyno());
                    context.startActivity(i);
                } else {
                    showCustomDialog1decline("Rider not assigned");
                }
            }
        });

        if (mOrderModelArrayList.get(position).getFavorites_display().equals("Yes")) {
            holder.imgfavred.setVisibility(View.VISIBLE);
            holder.imgfavgrey.setVisibility(View.GONE);
        } else {
            holder.imgfavred.setVisibility(View.GONE);
            holder.imgfavgrey.setVisibility(View.VISIBLE);
        }

        holder.tvRestaurantName.setText(orderModel.getRestaurant_name());
        holder.tvOrderNo.setText(orderModel.getOrder_identifyno());
        holder.tvOrderPrice.setText(pound.concat(orderModel.getOrdPrice()));
        holder.orderDisplayMessage.setText(orderModel.getOrder_display_message());
        if (orderModel.getOrder_type().contains("Collection")) {
            holder.writerider.setVisibility(View.GONE);
            holder.tv_order_track_driver.setVisibility(View.GONE);
        } else {
            holder.writerider.setVisibility(View.VISIBLE);
            holder.tv_order_track_driver.setVisibility(View.VISIBLE);
        }
//      if (orderModel.getRiderReview().equals("1")){
//          holder.writerider.setVisibility(View.GONE);
//      }else {
//          holder.writerider.setVisibility(View.VISIBLE);
//      }
        if (orderModel.getRiderOtp().equals("")) {
            holder.otp.setVisibility(View.GONE);
            holder.writerider.setVisibility(View.GONE);
        } else {
            holder.otp.setVisibility(View.VISIBLE);
            holder.writerider.setVisibility(View.VISIBLE);
            holder.riderotp.setText(orderModel.getRiderOtp());

            holder.writerider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderModel = mOrderModelArrayList.get(position);

                    if (orderModel.getDriverID().equals("0")) {
                        if (orderModel.getRiderRating().equals("")) {
                            Intent i = new Intent(context, OrderRevie_Write.class);
                            i.putExtra("orderIdentifyNo", orderModel.getOrder_identifyno());
                            context.startActivity(i);
                        } else {
                            showCustomDialog1decline("You have already given review for selected rider");
                        }
                    } else {
                        showCustomDialog1decline("Rider not assigned");
                    }
                }
            });
        }

        holder.imgfavgrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Justfoodz")
                        .setMessage("Are you sure you want to make it favourite ?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog, int which) {
                                        getFav("" + mOrderModelArrayList.get(position).getOrder_identifyno(), "" + mOrderModelArrayList.get(position).getRestaurant_id());
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRestaurantName, tvOrderNo, tvOrderPrice, orderDisplayMessage, riderotp;
        private LinearLayout tvOrderTrack, tvViewDetails, tv_order_track_driver, writerider, otp;
        ImageView imgfavred, imgfavgrey;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurant_name);
            tvOrderNo = itemView.findViewById(R.id.tv_order_identifyno);
            tvOrderPrice = itemView.findViewById(R.id.tv_orderPrice);
            orderDisplayMessage = itemView.findViewById(R.id.order_display_message);
            tvOrderTrack = itemView.findViewById(R.id.tv_order_track);
            tvViewDetails = itemView.findViewById(R.id.tv_view_details);
            tv_order_track_driver = itemView.findViewById(R.id.tv_order_track_driver);
            riderotp = itemView.findViewById(R.id.riderotp);
            writerider = itemView.findViewById(R.id.writerider);
            otp = itemView.findViewById(R.id.otp);
            imgfavred = itemView.findViewById(R.id.imgfavred);
            imgfavgrey = itemView.findViewById(R.id.imgfavgrey);
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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

    private void getFav(final String a, final String b) {
        progressDialog = progressDialog.show(context, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.add_remove_fav, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rere", "" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error = jsonObject.getInt("error");
                    if (error == 1) {
                        Intent i = new Intent(context, MyOrderActivity.class);
                        context.startActivity(i);
                        ((MyOrderActivity) context).finish();
                    } else {
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CustomerId", "" + MyOrderActivity.c_id);
                params.put("order_id", "" + a);
                params.put("restaurant_id", "" + b);
                params.put("favorite_action_type", "add");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}