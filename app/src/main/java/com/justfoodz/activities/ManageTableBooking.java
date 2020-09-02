package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.justfoodz.R;
import com.justfoodz.models.ManageTabelModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageTableBooking extends AppCompatActivity {
    RecyclerView managetablerecycler;
    private ArrayList<ManageTabelModel> managetableList;
    private ManageTableBookingAdapter manageTableBookingAdapter;
    ProgressDialog pDialog;
    String CustomerId;
    AuthPreference authPreference;
    ImageView iv_back;
    Dialog activedialog;
    ManageTabelModel manageTabelModel;
    int deleteindexpos;
    String TABLEID;
    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table_booking);
        managetablerecycler = findViewById(R.id.managetablerecycler);
        emptytxt=findViewById(R.id.emptytxt);
        imageblank=findViewById(R.id.imageblank);
        layout2=findViewById(R.id.layout);
        authPreference=new AuthPreference(this);
        CustomerId=authPreference.getCustomerId();
        iv_back=findViewById(R.id.iv_back);
        managetableList = new ArrayList<>();

        HitURLforManageTableData();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
        public void HitURLforManageTableData () {
            pDialog = new ProgressDialog(ManageTableBooking.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ManageTableBooking, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ressponse", response);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray buylist = jsonObj.getJSONArray("TableBookingList");
                        managetableList.clear();
                        for (int i = 0; i < buylist.length(); i++) {
                            JSONObject c = buylist.getJSONObject(i);
                            int error= c.getInt("error");
                            if (error==1){
                                String error_msg= c.getString("error_msg");
                                managetablerecycler.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
                                imageblank.setVisibility(View.VISIBLE);
                                emptytxt.setVisibility(View.VISIBLE);
                                emptytxt.setText(error_msg);


                            }else {
                                managetablerecycler.setVisibility(View.VISIBLE);
                                layout2.setVisibility(View.GONE);
                                imageblank.setVisibility(View.GONE);
                                emptytxt.setVisibility(View.GONE);
                                String id = c.getString("id");
                                String table_id = c.getString("table_id");
                                String status = c.getString("status");
                                String booking_date = c.getString("booking_date");
                                String booking_time = c.getString("booking_time");
                                String booking_id = c.getString("booking_id");
                                String noofgusts = c.getString("noofgusts");
                                String table_booking_number = c.getString("table_booking_number");
                                String customer_booking_name = c.getString("customer_booking_name");
                                String customer_booking_mobile = c.getString("customer_booking_mobile");
                                String customer_booking_email = c.getString("customer_booking_email");
                                String booking_instruction = c.getString("booking_instruction");
                                String restaurant_name = c.getString("restaurant_name");
                                String restaurant_address = c.getString("restaurant_address");
                                String restaurant_phone = c.getString("restaurant_phone");
                                String restaurant_mobile = c.getString("restaurant_mobile");
                                String table_booking_status_message = c.getString("table_booking_status_message");


                                managetableList.add(new ManageTabelModel(id, table_id, status, booking_date, booking_time, booking_id, noofgusts
                                        ,table_booking_number,customer_booking_name,customer_booking_mobile,customer_booking_email,booking_instruction,restaurant_name,
                                        restaurant_address,restaurant_phone,restaurant_mobile,table_booking_status_message));


                                ManageTableBookingAdapter manageTableBookingAdapter = new ManageTableBookingAdapter(ManageTableBooking.this, managetableList);
                                LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ManageTableBooking.this, LinearLayoutManager.VERTICAL, false);
                                managetablerecycler.setLayoutManager(horizontalLayoutManager2);
                                managetablerecycler.setAdapter(manageTableBookingAdapter);
                                }
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
                    params.put("CustomerId",CustomerId);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(strReq);
        }
    public void HitUrlforDeletegroup() {
        pDialog = new ProgressDialog(ManageTableBooking.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ManageTableCancel, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success==1) {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg);
                        managetableList.clear();
                        HitURLforManageTableData();
                        activedialog.dismiss();


                    } else {
                        String success_msg = jsonObj.getString("success_msg");
                        showCustomDialog1decline(success_msg);
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
                params.put("table_id", managetableList.get(deleteindexpos).getTable_id());
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
        private void showCustomDialog1decline (String s){
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

        public class ManageTableBookingAdapter extends RecyclerView.Adapter<ManageTableBookingAdapter.ViewHolder> {

            Context context;

            ArrayList<ManageTabelModel> managetableList;

            public ManageTableBookingAdapter(Context c, ArrayList<ManageTabelModel> managetableList) {
                this.context = c;
                this.managetableList = managetableList;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater;
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = layoutInflater.inflate(R.layout.managetablelist, parent, false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {

                holder.restaurant_name.setText(managetableList.get(position).getRestaurant_name());
                holder.bookingno.setText(managetableList.get(position).getBooking_id());
                holder.tableno.setText(managetableList.get(position).getTable_booking_number());
                holder.noofguests.setText(managetableList.get(position).getNoofgusts());
                holder.address.setText(managetableList.get(position).getRestaurant_address());

                holder.pending.setText(managetableList.get(position).getTable_booking_status_message());

                String ststus=managetableList.get(position).getStatus();

                if (ststus.equalsIgnoreCase("0")){
                    holder.cancel.setVisibility(View.VISIBLE);
                }else{
                    holder.cancel.setVisibility(View.GONE);
                }

              holder.cancel.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      manageTabelModel = managetableList.get(position);
                      deleteindexpos = position;
                      TABLEID = manageTabelModel.getTable_id();
                      activedialog = new Dialog(context);
                      activedialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
                      activedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                      activedialog.setContentView(R.layout.catdelete_dialog_layout);
                      activedialog.show();
                      Button cancelBtn = (Button) activedialog.findViewById(R.id.cancel);
                      Button okBtn = (Button) activedialog.findViewById(R.id.ok);
                      TextView txtdialogTv = (TextView) activedialog.findViewById(R.id.txtdialog);
                      txtdialogTv.setText("Are You Sure to Cancel Table?");
                      cancelBtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              activedialog.dismiss();
                          }
                      });
                      okBtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              if (Network.isNetworkCheck(context)) {
                                  HitUrlforDeletegroup();
                              } else {
                                  Toast.makeText(context, context.getResources().getString(R.string.internet), Toast.LENGTH_LONG).show();
                              }
                          }
                      });
                  }
              });

            }


            @Override
            public int getItemCount() {
                return managetableList.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {

                TextView restaurant_name, bookingno,tableno,noofguests,address,pending,cancel,viewdetails;


                public ViewHolder(View itemView) {
                    super(itemView);
                    restaurant_name = (TextView) itemView.findViewById(R.id.restaurant_name);
                    bookingno = (TextView) itemView.findViewById(R.id.bookingno);
                    tableno = (TextView) itemView.findViewById(R.id.tableno);
                    noofguests = (TextView) itemView.findViewById(R.id.noofguests);
                    address = (TextView) itemView.findViewById(R.id.address);
                    cancel = (TextView) itemView.findViewById(R.id.cancel);
                    pending = (TextView) itemView.findViewById(R.id.pending);
                    viewdetails = (TextView) itemView.findViewById(R.id.viewdetails);



                    viewdetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position=getAdapterPosition();
                            Intent intent = new Intent(ManageTableBooking.this, ManageTableAllDetails.class);
                            intent.putExtra("tableid", managetableList.get(position).getTable_id());
                            startActivity(intent);
                            finish();


                        }
                    });
                }
            }
        }


    }
