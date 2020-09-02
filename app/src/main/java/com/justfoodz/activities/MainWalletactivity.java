package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainWalletactivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout transactionreport, digitalwallet, payviawalletid, scanqrcode, mywallet, llchangepin;
    ImageView iv_back;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    AuthPreference authPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_walletactivity);
        requestQueue = Volley.newRequestQueue(this);
        authPreference = new AuthPreference(this);
        digitalwallet = findViewById(R.id.digitalwallet);
        payviawalletid = findViewById(R.id.payviawalletid);
        scanqrcode = findViewById(R.id.scanqrcode);
        mywallet = findViewById(R.id.mywallet);
        transactionreport = findViewById(R.id.transactionreport);
        llchangepin = findViewById(R.id.llchangepin);
        iv_back = findViewById(R.id.iv_back);

        digitalwallet.setOnClickListener(this);
        payviawalletid.setOnClickListener(this);
        scanqrcode.setOnClickListener(this);
        mywallet.setOnClickListener(this);
        transactionreport.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        llchangepin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.digitalwallet:
                startActivity(new Intent(this, DigitalWallet.class));
                break;
            case R.id.payviawalletid:
                startActivity(new Intent(this, PayVIAWALLETIDActivity.class));
                break;
            case R.id.scanqrcode:
                startActivity(new Intent(this, ScanQRCodeActivity.class));
                break;
            case R.id.mywallet:
                startActivity(new Intent(this, MywalletActivity.class));
                break;
            case R.id.transactionreport:
                startActivity(new Intent(this, ViewAllTransactionDigital.class));
                break;
            case R.id.iv_back:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.llchangepin:
                new AlertDialog.Builder(MainWalletactivity.this)
                        .setTitle("Justfoodz")
                        .setMessage("Are you sure you want to re-set your wallet pin number ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getResetpin();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).show();
                break;
        }
    }

    public void getResetpin() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_wallet_password_reset> userLoginCall = interfaceRetrofit.phpexpert_customer_wallet_password_reset("" + authPreference.getDigitalWallet_CardNumber(),
                "" + authPreference.getCustomerId());
        userLoginCall.enqueue(new Callback<Model_wallet_password_reset>() {
            @Override
            public void onResponse(Call<Model_wallet_password_reset> call, retrofit2.Response<Model_wallet_password_reset> response) {
                if (response.isSuccessful()) {
                    showCustomDialog1decline("" + response.body().getSuccess_msg());
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_wallet_password_reset> call, Throwable t) {
                MyProgressDialog.dismiss();
                showCustomDialog1decline("" + t.toString());

            }
        });

/*
        progressDialog = progressDialog.show(MainWalletactivity.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.customer_wallet_password_reset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("changwalletpinresponse", "" + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String success_msg = jsonObject.getString("success_msg");
                    showCustomDialog1decline("" + success_msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                showCustomDialog1decline("" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("digital_wallet_card_number", "" + authPreference.getDigitalWallet_CardNumber());
                params.put("CustomerId", "" + authPreference.getCustomerId());
                Log.e("changwalletpinparams", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);*/
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainWalletactivity.this).create();
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