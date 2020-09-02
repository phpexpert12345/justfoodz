package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.justfoodz.R;

import com.justfoodz.models.SubMenuModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.ServiceGenerator;
import com.justfoodz.retrofit.StripeResponse;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StripePaymentActivity extends AppCompatActivity {
    ImageView iv_back;
    Stripe stripe;
    private String key = "pk_test_qcq55VODkhv0BUHPfCiCNAQO";
    CardInputWidget cardInputWidget;
    Button buttonSubmit;
    ProgressDialog pDialog;
    String amount,paymentwallet;
    TextView tvAmount;
    double aDouble;
    String pound = SplashScreenActivity.currency_symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        initViews();
    }

    private void initViews() {
        authPreference = new AuthPreference(this);
        iv_back = findViewById(R.id.iv_back);
        tvAmount = findViewById(R.id.tvAmount);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        cardInputWidget = findViewById(R.id.card_input_widget);
        iv_back.setOnClickListener(view -> {
            finish();
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            amount = intent.getStringExtra("amount");
            paymentwallet = intent.getStringExtra("paymentwallet");

            aDouble= Double.parseDouble(amount);
            try {
                tvAmount.setText(SplashScreenActivity.currency_symbol+String.format("%.2f", aDouble));
                Log.e("p",""+amount);

            } catch (Exception e) { } }
        stripe = new Stripe(this, key);
        buttonSubmit.setOnClickListener(view -> {
            if (cardInputWidget.getCard() == null) {
                showCustomDialog1decline1(getString(R.string.invalid_card));
            } else {
                stripe.createToken(
                        cardInputWidget.getCard(),
                        new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(@NonNull Token result) {
                                //    Toast.makeText(StripePaymentActivity.this, "" + result, Toast.LENGTH_SHORT).show();
                                Log.e("cardsave", "ddddd" + result);
                                if (Network.isNetworkCheck(StripePaymentActivity.this)) {
                                    if(paymentwallet.equals("1")) makeWalletResposne(result.getId());
                                    else makeServerRequest(result.getId());
                                }
                                else Toast.makeText(StripePaymentActivity.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(@NonNull Exception e) {
                                Toast.makeText(StripePaymentActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                Log.e("card to save", "sssss" + e);
                            }
                        }
                );
            }


        });


    }
    private AuthPreference authPreference;


    private void makeServerRequest(String token) {
        pDialog = new ProgressDialog(StripePaymentActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "charge");
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("source", token);
        params.put("description", "Payment by credit card");
        Log.e("params", "" + params);
        InterUserdata interUserdata = ServiceGenerator.createService(InterUserdata.class);
        interUserdata.getStripePaymentResponse(params).enqueue(new Callback<StripeResponse>() {
            @Override
            public void onResponse(Call<StripeResponse> call, Response<StripeResponse> response) {

                if (response.isSuccessful()) {
                    pDialog.dismiss();
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        showCustomDialog1decline(response.body().getMessage());

                    } else {
                        Toast.makeText(StripePaymentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<StripeResponse> call, Throwable t) {
                pDialog.dismiss();

            }
        });
//
    }

    private void showCustomDialog1decline1(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(StripePaymentActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", (dialog, which) -> {

            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(StripePaymentActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", (dialog, which) -> {
            Intent intent = new Intent();
            intent.putExtra("result", s);
            setResult(RESULT_OK, intent);
            finish();
        });
        alertDialog.show();
    }

    private void makeWalletResposne(String token) {
        String customerId = authPreference.getCustomerId();
        pDialog = new ProgressDialog(StripePaymentActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "charge");
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("source", token);
        params.put("description", "Payment by credit card");
        params.put("CustomerId", customerId);
        Log.e("params", "" + params);
        InterUserdata interUserdata = ServiceGenerator.createService(InterUserdata.class);
        interUserdata.getWalletReponse(params).enqueue(new Callback<StripeResponse>() {
            @Override
            public void onResponse(Call<StripeResponse> call, Response<StripeResponse> response) {

                if (response.isSuccessful()) {
                    pDialog.dismiss();
                    if(response.body().getResponse().equalsIgnoreCase("Success"))
                        showCustomDialog1decline(response.body().getMessage());
                    else Toast.makeText(StripePaymentActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                else pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StripeResponse> call, Throwable t) {
                pDialog.dismiss();

            }
        });
//
    }
}