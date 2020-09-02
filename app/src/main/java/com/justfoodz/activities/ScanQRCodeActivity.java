package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanQRCodeActivity extends AppCompatActivity {

    ImageView buttonScan,back;
    private TextView txtsubmit, txtname;
    private IntentIntegrator qrScan;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    EditText edt_amount,edt_pin;
    String customer_wallet_number;
    SharedPreferences sharedPreferences;
    AuthPreference authPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        authPreference = new AuthPreference(this);
        buttonScan = (ImageView) findViewById(R.id.buttonScan);
        back = (ImageView) findViewById(R.id.back);
        txtsubmit = (TextView) findViewById(R.id.txtsubmit);
        txtname = (TextView) findViewById(R.id.txtname);
        edt_amount = (EditText) findViewById(R.id.edt_amount);
        edt_pin = (EditText) findViewById(R.id.edt_pin);


        buttonScan.setOnClickListener(view -> qrScan.initiateScan());
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
        back.setOnClickListener(v -> finish());
        txtsubmit.setOnClickListener(v -> {
            if (edt_amount.getText().toString().equals("")) {
                edt_amount.setError("Enter Amount");
                edt_amount.requestFocus();
            } else if (edt_pin.getText().toString().equals("")) {
                edt_pin.setError("Enter your pin number");
                edt_pin.requestFocus();
            }else {
                getSubmit();
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                showAlert("Result Not Found","1");
            } else {
                //if qr contains data
//                try {
                Log.e("rerere",""+result.getContents());

                String rr = result.getContents();
                String[] separated = rr.split("_");
                txtname.setText(""+separated[1]);
                customer_wallet_number = separated[0];

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
//money_from_customer

    public void getSubmit()
    {
        progressDialog = progressDialog.show(ScanQRCodeActivity.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.WallettoWalletTransfer, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("qwerty",""+s);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    String success_msg = jsonObject.getString("success_msg");
                    if (success==0) {
                        showAlert(success_msg,"1");
                    }else {
                        showAlert(success_msg,"2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e("error",""+volleyError);
                Toast.makeText(ScanQRCodeActivity.this, "Please Check your network connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipient_wallet_number", customer_wallet_number);
                params.put("transfer_wallet_money", ""+edt_amount.getText().toString());
                params.put("CustomerWalletPin", ""+edt_pin.getText().toString());
                params.put("CustomerId", ""+authPreference.getCustomerId());
                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showAlert (String a, final String b)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(ScanQRCodeActivity.this).create();
        // alertDialog.setTitle("Alert");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(a);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    Intent i = new Intent(ScanQRCodeActivity.this,MainWalletactivity.class);
                    startActivity(i);
                    finish();
                }else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
}