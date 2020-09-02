package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageTableAllDetails extends AppCompatActivity {
    TextView restaurantname, restaurantaddress, restaurantphoneno, restaurantmobile, restaurantdate, restauranttime, bookingno,
            noofguests, tablebookingbumber, bookingname, bookingcontact, bookingemail, specialinstruct,bookingno1;
    String tableid;
    ProgressDialog pDialog;
    ImageView iv_back;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ManageTableAllDetails.this,ManageTableBooking.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table_all_details);
        tableid = getIntent().getStringExtra("tableid");


        restaurantname = (TextView) findViewById(R.id.restaurantname);
        restaurantaddress = (TextView) findViewById(R.id.restaurantaddress);
        restaurantphoneno = (TextView) findViewById(R.id.restaurantphoneno);
        restaurantmobile = (TextView) findViewById(R.id.restaurantmobile);
        restaurantdate = (TextView) findViewById(R.id.restaurantdate);
        restauranttime = (TextView) findViewById(R.id.restauranttime);
        bookingno = (TextView) findViewById(R.id.bookingno);
        noofguests = (TextView) findViewById(R.id.noofguests);
        tablebookingbumber = (TextView) findViewById(R.id.tablebookingbumber);
        bookingname = (TextView) findViewById(R.id.bookingname);
        bookingcontact = (TextView) findViewById(R.id.bookingcontact);
        bookingemail = (TextView) findViewById(R.id.bookingemail);
        specialinstruct = (TextView) findViewById(R.id.specialinstruct);
        bookingno1=(TextView)findViewById(R.id.bookingno1);
        iv_back=findViewById(R.id.iv_back);
        HitUrlforShowTAbledata();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ManageTableAllDetails.this,ManageTableBooking.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void HitUrlforShowTAbledata() {
        pDialog = new ProgressDialog(ManageTableAllDetails.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ManageTableShow, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray buylist = jsonObj.getJSONArray("TableBookingDetailList");
                    for (int i = 0; i < buylist.length(); i++) {
                        JSONObject c = buylist.getJSONObject(i);

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
                        String error = c.getString("error");

                        restaurantname.setText(restaurant_name);
                        restaurantaddress.setText(restaurant_address);
                        restaurantphoneno.setText(restaurant_phone);
                        restaurantmobile.setText(restaurant_mobile);
                        restaurantdate.setText(booking_date);
                        restauranttime.setText(booking_time);
                        bookingno.setText(booking_id);
                        noofguests.setText(noofgusts);
                        tablebookingbumber.setText(table_booking_number);
                        bookingname.setText(customer_booking_name);
                        bookingcontact.setText(customer_booking_mobile);
                        bookingemail.setText(customer_booking_email);
                        specialinstruct.setText(booking_instruction);
                        bookingno1.setText("#"+booking_id);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline("Something went wrong please try again");

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("table_id", tableid);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
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
