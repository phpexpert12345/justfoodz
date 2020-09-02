package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodz.R;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayVIAWALLETIDActivity extends AppCompatActivity {
    ImageView iv_back;
    EditText walletno,amount,pinnumber;
    Button submit;
    ProgressDialog pDialog;
    AuthPreference authPreference;

    String WALLETNO,AMOUNT,PINNUMBER,CustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_viawalletid);
        iv_back=findViewById(R.id.iv_back);
        submit=findViewById(R.id.submit);
        walletno=findViewById(R.id.walletno);
        amount=findViewById(R.id.amount);
        pinnumber=findViewById(R.id.pinnumber);
        submit=findViewById(R.id.submit);
        authPreference=new AuthPreference(this);
        CustomerId=authPreference.getCustomerId();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (walletno.getText().toString().length() == 0) {
                    walletno.setError("Enter Wallet Number");
                    walletno.requestFocus();
                }
                else if (amount.getText().toString().length() == 0) {
                    amount.setError("Enter Amount");
                    amount.requestFocus();
                }
                else if (pinnumber.getText().toString().length() == 0) {
                    pinnumber.setError("Enter PIN");
                    pinnumber.requestFocus();
                }
                else {
                    HitURLForPayWalletData();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PayVIAWALLETIDActivity.this,MainWalletactivity.class);
                startActivity(intent);
            }
        });
    }
    public void HitURLForPayWalletData() {

        WALLETNO= walletno.getText().toString();
        AMOUNT= amount.getText().toString();
        PINNUMBER= pinnumber.getText().toString();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.WallettoWalletTransfer, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ressponse", response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    int success = jsonObj.getInt("success");
                    if (success ==0) {

                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg,"1");



                    } else if(success==1) {
                        String success_msg = jsonObj.optString("success_msg");
                        showCustomDialog1decline(success_msg,"2");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showCustomDialog1decline(error.getMessage(),"2");

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipient_wallet_number", WALLETNO);
                params.put("transfer_wallet_money", AMOUNT);
                params.put("CustomerWalletPin", PINNUMBER);
                params.put("CustomerId", CustomerId);
                Log.e("papapa",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    private void showCustomDialog1decline(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    Intent intent=new Intent(PayVIAWALLETIDActivity.this,MainWalletactivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
}
