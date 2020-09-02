package com.justfoodz.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.models.BuyGiftModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.ConnectionDetector;
import com.justfoodz.utils.UrlConstants;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class BuyGiftDetailsActivity extends AppCompatActivity {
    TextView giftname,giftdescription,giftprice,giftredemption,gifttermscondition;
    ImageView giftimage,iv_back;
   AuthPreference authPreference;
   String image,referedname,referedemail,transactionid,customId;
   Button btnbuy;
    Dialog Addgroupdialog;
    ProgressDialog pDialog;


    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    private String paymentAmount;
    public static final int PAYPAL_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_gift_details);
        giftname=findViewById(R.id.giftname);
        giftprice=findViewById(R.id.giftprice);
        giftredemption=findViewById(R.id.giftredemption);
        gifttermscondition=findViewById(R.id.gifttermscondition);
        giftdescription=findViewById(R.id.giftdescription);
        giftimage=findViewById(R.id.giftimage);
        iv_back=findViewById(R.id.iv_back);
        btnbuy=findViewById(R.id.btnbuy);

        Intent intent = getIntent();
        String gift_name = intent.getStringExtra("giftname");
        String gift_price = intent.getStringExtra("giftprice");
        String gift_description = intent.getStringExtra("giftdescription");
        String gift_termscondition = intent.getStringExtra("gifttermscondition");
        String gift_redemption = intent.getStringExtra("giftredemption");

        giftname.setText(gift_name);
        giftprice.setText(SplashScreenActivity.currency_symbol+gift_price);
        giftdescription.setText(gift_description);
        gifttermscondition.setText(gift_termscondition);
        giftredemption.setText(gift_redemption);

        authPreference=new AuthPreference(this);
        image= authPreference.getBuyGiftImage();
        customId = authPreference.getCustomerId();
        Picasso.get().load(authPreference.getBuyGiftImage()).into(giftimage);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddGroupDialog();
            }
        });

    }

    public void showAddGroupDialog() {
        Addgroupdialog = new Dialog(this);
        Addgroupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Addgroupdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Addgroupdialog.setContentView(R.layout.buy_customer_detail);
        Addgroupdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Addgroupdialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        Addgroupdialog.setCanceledOnTouchOutside(true);
        Button submit = (Button) Addgroupdialog.findViewById(R.id.submit);
        final EditText edtname = (EditText) Addgroupdialog.findViewById(R.id.edtname);
        final EditText edtemailid = (EditText) Addgroupdialog.findViewById(R.id.edtemailid);
        Button cancel = (Button) Addgroupdialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addgroupdialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referedname = edtname.getText().toString().trim();
                referedemail = edtemailid.getText().toString().trim();
                if (referedname.isEmpty()) {
                    edtname.setError("Enter receiver name");
                    edtname.requestFocus();
                } else if (!isValidEmail(referedemail)) {
                    edtemailid.setError("Enter receiver valid email id");
                    edtemailid.requestFocus();
                } else if (com.justfoodz.utils.Network.isNetworkCheck(BuyGiftDetailsActivity.this)) {
                    getPayment();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Addgroupdialog.show();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void getPayment() {

        paymentAmount = getIntent().getStringExtra("giftprice");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
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
                            Log.e("eqw",""+e);
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
    }

    public void paymentSubmit() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_BuyGiftCard> userLoginCall = interfaceRetrofit.phpexpert_buy_gift_card_payments(customId, transactionid,paymentAmount, ""+getIntent().getStringExtra("giftcardID"),
                referedemail,referedname);
        userLoginCall.enqueue(new Callback<Model_BuyGiftCard>() {
            @Override
            public void onResponse(Call<Model_BuyGiftCard> call, retrofit2.Response<Model_BuyGiftCard> response) {
                if (response.isSuccessful()) {
                    Model_BuyGiftCard body = response.body();
                    if (body.getSuccess().equalsIgnoreCase("0")) {
                        showCustomDialog1decline(body.getSuccess_msg(),"1");
                    }else
                    {
                        showCustomDialog1decline(body.getSuccess_msg(),"2");
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_BuyGiftCard> call, Throwable t) {
                Toast.makeText(BuyGiftDetailsActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });



        /*pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest strReq1 = new StringRequest(Request.Method.POST, UrlConstants.buygift, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("payresponse", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String success = obj.getString("success");
                    String success_msg = obj.optString("success_msg");
                    if (success.equals("0")) {
                        showCustomDialog1decline(success_msg,"1");
                    } else {
                        showCustomDialog1decline(success_msg,"2");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerId", customId);
                params.put("transaction_id", transactionid);
                params.put("giftcardAmount", paymentAmount);
                params.put("giftcardID", ""+getIntent().getStringExtra("giftcardID"));
                params.put("giftcard_user_email", referedemail);
                params.put("giftcard_user_name", referedname);
                Log.e("qwerty",""+params);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(strReq1);*/
    }

    private void showCustomDialog1decline(String s, final String b) {
        final AlertDialog alertDialog = new AlertDialog.Builder(BuyGiftDetailsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (b.equals("1")) {
                    Intent i = new Intent(BuyGiftDetailsActivity.this,BuyGiftActivity.class);
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