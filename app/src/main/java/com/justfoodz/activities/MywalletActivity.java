package com.justfoodz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.gp.PaymentsUtil;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.Network;
import com.justfoodz.utils.UrlConstants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;

import static com.justfoodz.activities.PaymentActivity.PAYPAL_REQUEST_CODE;

public class MywalletActivity extends AppCompatActivity implements View.OnClickListener {
    TextView loyalitypoint, walletamount;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    String customId;
    RadioButton paypal, googlepay, stripe;
    ImageView iv_back;
    EditText amount;
    Button recpwdBtn, btnearnmore;
    public static String toopen_payment = "";
    public String transactionid = "";
    private static final int PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    private String paymentAmount;
    public static final int PAYPAL_REQUEST_CODE = 123;


    private PaymentsClient mPaymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        mPaymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
        loyalitypoint = (TextView) findViewById(R.id.loyalitypoint);
        walletamount = (TextView) findViewById(R.id.walletamount);
        paypal = (RadioButton) findViewById(R.id.paypal);
        googlepay = (RadioButton) findViewById(R.id.googlepay);
        stripe = (RadioButton) findViewById(R.id.stripe);
        recpwdBtn = (Button) findViewById(R.id.recpwdBtn);
        btnearnmore = (Button) findViewById(R.id.btnearnmore);
        amount = (EditText) findViewById(R.id.amount);
        iv_back = findViewById(R.id.iv_back);
        AuthPreference authPreference = new AuthPreference(this);
        customId = authPreference.getCustomerId();

        HitUrlforWalletAmount();

        paypal.setOnClickListener(this);
        googlepay.setOnClickListener(this);
        stripe.setOnClickListener(this);
        recpwdBtn.setOnClickListener(this);
        btnearnmore.setOnClickListener(this);

        iv_back.setOnClickListener(view -> {
            Intent intent = new Intent(MywalletActivity.this, MainWalletactivity.class);
            startActivity(intent);
        });

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    if (amount.getText().toString().equals("0")) {
                        amount.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnearnmore:
                Intent i = new Intent(MywalletActivity.this, Earnmore.class);
                startActivity(i);
                break;
            case R.id.paypal:
                toopen_payment = "Paypal";
                break;
            case R.id.googlepay:
                toopen_payment = "Credit-card";

                break;
            case R.id.stripe:
                toopen_payment = "Googlepay";
                break;
            case R.id.recpwdBtn:
                if (amount.getText().toString().equals("")) {
                    amount.setError("Enter Amount");
                    amount.requestFocus();
                } else if (toopen_payment.equals("")) {
                    Toast.makeText(this, "Please select one for further operations ", Toast.LENGTH_SHORT).show();

                } else if (toopen_payment.equals("Paypal")) {

//                    if (toopen_payment.equals("Paypal")) {
                    if (Network.isNetworkCheck(this)) {
                        getPayment();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
//
//                    } else {
//                        if (Network.isNetworkCheck(this)) {
//                            paymentSubmit();
//                        } else {
//                            Toast.makeText(this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
//                        }
//                    }
                } else if (toopen_payment.equals("Credit-card")) {
                    paymentAmount = String.valueOf("" + amount.getText().toString());
                    Intent intent = new Intent(this, StripePaymentActivity.class);
                    intent.putExtra("amount", paymentAmount);
                    intent.putExtra("paymentwallet", "1");
//
                    Log.e("payment", "jddj" + intent.putExtra("amount", paymentAmount));
                    startActivityForResult(intent, PAYMENT);
                } else if (toopen_payment.equals("Googlepay")) {
                    requestPayment(view);

                } else {

                }

                break;
            default:
                break;
        }
    }

    private void getPayment() {

        paymentAmount = String.valueOf("" + amount.getText().toString());
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Add money to wallet",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void paymentSubmit() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest strReq1 = new StringRequest(Request.Method.POST, UrlConstants.PaymentSubmitWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("payresponse", "" + response);
                try {
                    pDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");
                    if (success == 0) {
                        String success_msg = obj.optString("success_msg");
                        showCustomDialog1decline(success_msg, "1");

                    } else {
                        String success_msg = obj.optString("success_msg");
                        showCustomDialog1decline(success_msg, "2");
                    }
                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customId);
                params.put("transaction_id", transactionid);
                params.put("customer_wallet_money", paymentAmount);
                params.put("customer_wallet_payment_mode", toopen_payment);
                Log.e("qwerty", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(strReq1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);

                        //     Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", paymentAmount));

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jj = jsonDetails.getJSONObject("response");
                            transactionid = jj.getString("id");
                            paymentSubmit();
                            //showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
                        } catch (JSONException e) {
                            Log.e("eqw", "" + e);
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                    break;
                case Activity.RESULT_CANCELED:
                    // Nothing to here normally - the user simply cancelled without selecting a
                    // payment method.
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    handleError(status.getStatusCode());
                    break;
            }
        }

        if (requestCode == PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                HitUrlforWalletAmount();
                amount.setText("");
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        String price = PaymentsUtil.microsToString(Long.parseLong(amount.getText().toString()));

        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        if (request == null) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            setGooglePayAvailable(result);
                        } catch (ApiException exception) {
                            // Process error
                            Log.w("isReadyToPay failed", exception);
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {

        } else {
            Toast.makeText(this, "Unfortunately, Google Pay is not available on this phone", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInformation = paymentData.toJson();

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        if (paymentInformation == null) {
            return;
        }
        JSONObject paymentMethodData;

        try {
            paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type")
                    .equals("PAYMENT_GATEWAY")
                    && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
                    .equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog =
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage(
                                        "Gateway name set to \"example\" - please modify "
                                                + "Constants.java and replace it with your own gateway.")
                                .setPositiveButton("OK", null)
                                .create();
                alertDialog.show();
            }

            String billingName =
                    paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name");
            Log.d("BillingName", billingName);
//            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG)
//                    .show();

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"));
        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString());
            return;
        }
    }


    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    private void HitUrlforWalletAmount() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_walletMoney> userLoginCall = interfaceRetrofit.phpexpert_customer_wallet_moeny(customId);
        userLoginCall.enqueue(new Callback<Model_walletMoney>() {
            @Override
            public void onResponse(Call<Model_walletMoney> call, retrofit2.Response<Model_walletMoney> response) {
                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        String total_wallet_money_available = response.body().getTotal_wallet_money_available();
                        walletamount.setText(SplashScreenActivity.currency_symbol + total_wallet_money_available);
                        authPreference = new AuthPreference(MywalletActivity.this);
                        authPreference.setGiftWalletMoney(total_wallet_money_available);

                        HitURLforLoyalitypoint();
                    } else {

                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_walletMoney> call, Throwable t) {
                Toast.makeText(MywalletActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


        /*

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.WalletMoneyAmount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ressponsee", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 0) {
                        String loyality = jObj.optString("Total_wallet_money_available");

                        walletamount.setText(SplashScreenActivity.currency_symbol + loyality);
                        authPreference = new AuthPreference(MywalletActivity.this);
                        authPreference.setGiftWalletMoney(loyality);

                        HitURLforLoyalitypoint();
                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);

                    } else {
                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }

    private void HitURLforLoyalitypoint() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_loyalty_point> userLoginCall = interfaceRetrofit.phpexpert_customer_loyalty_point(customId);
        userLoginCall.enqueue(new Callback<Model_loyalty_point>() {
            @Override
            public void onResponse(Call<Model_loyalty_point> call, retrofit2.Response<Model_loyalty_point> response) {
                if (response.isSuccessful()) {
                    try {

                        if (response.body().getSuccess().equalsIgnoreCase("0")) {

                            Model_loyalty_point body = response.body();

                            loyalitypoint.setText(body.getTotal_Loyalty_points());
                        } else {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                    }
                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_loyalty_point> call, Throwable t) {
                Toast.makeText(MywalletActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });

/*
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.Loyalitypoints, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rel", "" + response);

                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 0) {
                        String loyality = jObj.optString("Total_Loyalty_points");

                        loyalitypoint.setText(loyality);

                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);

                    } else {
                        String success_msg = jObj.getString("success_msg");
//                        showCustomDialog1decline (success_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
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
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customId);
                return params;
            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/
    }

    private void showCustomDialog1decline(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MywalletActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", (dialo, which) -> {
            if (b.equals("1")) {
                alertDialog.dismiss();
                HitUrlforWalletAmount();
            } else {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}