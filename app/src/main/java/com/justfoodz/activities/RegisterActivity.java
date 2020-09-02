package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.models.SubMenuModel;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvAlreadyAccount;
    Button tvSignUp;
    private ImageView ivBack;
    private MaterialEditText edtFirstName, edtLastName, edtEmail, edtPhoneNumber, edtPassword, edt_referalcode;
    private String firstName, lastName, email, phoneNumber, password;
    private ProgressDialog pDialog;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private AuthPreference authPreference;
    private Dialog alertDialog;
    public ArrayList<SubMenuModel> subMenuModelArrayListReceive = null;
    Button btnUpdate;
    private String date, time, itemId, id, disPrice = "", deliveryChargeValue = "", serviceFees = "", preOrderTime, updateTotalPrice, orderType = "", restaurantName;
    SharedPreferences sharedPreferences;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myPref = new MyPref(RegisterActivity.this);
        initialization();
        initializedListener();
        statusBarColor();
        authPreference = new AuthPreference(this);
    }

    private void initialization() {
        sharedPreferences = getSharedPreferences("noti", MODE_PRIVATE);
        tvAlreadyAccount = findViewById(R.id.tv_already_account);
        ivBack = findViewById(R.id.iv_back);
        tvSignUp = findViewById(R.id.tv_sign_up);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtPassword = findViewById(R.id.edt_password);
        edt_referalcode = findViewById(R.id.edt_referalcode);
        date = getIntent().getStringExtra("date");
        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");
        itemId = getIntent().getStringExtra("itemId");
        disPrice = getIntent().getStringExtra("disPrice");
        deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
        serviceFees = getIntent().getStringExtra("serviceFees");
        restaurantName = getIntent().getStringExtra("restaurantName");
        orderType = getIntent().getStringExtra("orderType");
        subMenuModelArrayListReceive = (ArrayList<SubMenuModel>) getIntent().getSerializableExtra("subMenuModelArrayLists");
        orderType = getIntent().getStringExtra("orderType");

    }


    private void initializedListener() {
        tvAlreadyAccount.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_already_account:
                Intent qw = new Intent(this, LoginActivity.class);
                qw.putExtra("id", "");
                qw.putExtra("restaurantName", "");
                qw.putExtra("restaurantAddress", "");
                qw.putExtra("date", "");
                qw.putExtra("time", "");
                qw.putExtra("itemId", "");
                qw.putExtra("updateTotalPrice", "");
                qw.putExtra("subTot", "");
                qw.putExtra("disPrice", "");
                qw.putExtra("deliveryChargeValue", "");
                qw.putExtra("serviceFees", "");
                qw.putExtra("packageFees", "");
                qw.putExtra("salesTaxAmount", "");
                qw.putExtra("vatTax", "");
                //  intent.putExtra("subMenuModelArrayLists", subMenuModelArrayLists);
                qw.putExtra("orderType", "");
//                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
//                    intent.putExtra("subMenuCartList",subMenuCartList);
                qw.putExtra("subMenuItemId", "");
                startActivity(qw);
                break;
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_sign_up:
                registerValidation();
                //   startActivity(new Intent(RegisterActivity.this, UpdateProfileActivity.class));
                break;
            default:
                break;
        }
    }

    public void registerValidation() {
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        phoneNumber = edtPhoneNumber.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (firstName.isEmpty()) {
            edtFirstName.setError("Enter First Name");
            edtFirstName.requestFocus();
        } else if (lastName.isEmpty()) {
            edtLastName.setError("Enter Last Name");
            edtLastName.requestFocus();
        } else if (!isValidEmail(email)) {
            edtEmail.setError("Enter Valid Email Address");
            edtEmail.requestFocus();
        } else if (phoneNumber.isEmpty()) {
            edtPhoneNumber.setError("Enter Phone Number");
            edtPhoneNumber.requestFocus();
        } else if (password.isEmpty()) {
            edtPassword.setError("Enter Password");
            edtPassword.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(RegisterActivity.this)) {
            registerUserApi();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void registerUserApi() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_AccountRegistration> userLoginCall = interfaceRetrofit.phpexpert_customer_account_register(firstName, lastName, email, password, phoneNumber, "" + edt_referalcode.getText().toString(),
                "" + sharedPreferences.getString("token", ""), "Android", myPref.getState(), myPref.getCity(),
                myPref.getLatitude(), myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_AccountRegistration>() {
            @Override
            public void onResponse(Call<Model_AccountRegistration> call, retrofit2.Response<Model_AccountRegistration> response) {
                if (response.isSuccessful()) {

                    try {

                        if (response.body().getSuccess().equalsIgnoreCase("1")) {
                            Model_AccountRegistration body = response.body();

                            authPreference.setCustomerId(body.getCustomerId());
                            authPreference.setDigitalWallet_CardNumber(body.getDigital_wallet_card_number());
                            authPreference.setDigitalWallet_QrCode(body.getDigital_wallet_qr_code());
                            authPreference.setDigitalWallet_PINNumber(body.getDigital_wallet_card_pin_number());
                            authPreference.setDigitalWallet_MemerSince(body.getDigital_wallet_member_since());
                            authPreference.setDigitalWalletMessage(body.getWEBSITE_DIGITAL_WALLET_TERMS());
                            authPreference.setGiftWalletMoney(body.getTotal_wallet_money_available());
                            authPreference.setFirstName(firstName);
                            authPreference.setLastName(lastName);
                            authPreference.setUserEmail(email);
                            authPreference.setUserPhone(phoneNumber);
                            authPreference.setCompanyStreet("" + myPref.getPickupAdd());
                            authPreference.setUserPostcode("" + myPref.getFirebaseTokenId());


                            Toast.makeText(getApplicationContext(), body.getSuccess_msg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, UpdateProfileActivity.class);
                            intent.putExtra("firstName", firstName);
                            intent.putExtra("lastName", lastName);
                            intent.putExtra("email", email);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                        } else {

                            showCustomDialog1decline(response.body().getError_msg());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_AccountRegistration> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


     /*   pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_ACCOUNT_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        int error = jObj.getInt("error");
                        String customerId = jObj.getString("CustomerId");
                        String digital_wallet_card_number = jObj.optString("digital_wallet_card_number");
                        String digital_wallet_qr_code = jObj.optString("digital_wallet_qr_code");
                        String digital_wallet_card_pin_number = jObj.optString("digital_wallet_card_pin_number");
                        String digital_wallet_member_since = jObj.optString("digital_wallet_member_since");
                        String WEBSITE_DIGITAL_WALLET_TERMS = jObj.optString("WEBSITE_DIGITAL_WALLET_TERMS");
                        String Total_wallet_money_available = jObj.optString("Total_wallet_money_available");
                        authPreference.setCustomerId(jObj.getString("CustomerId"));
                        authPreference.setDigitalWallet_CardNumber(digital_wallet_card_number);
                        authPreference.setDigitalWallet_QrCode(digital_wallet_qr_code);
                        authPreference.setDigitalWallet_PINNumber(digital_wallet_card_pin_number);
                        authPreference.setDigitalWallet_MemerSince(digital_wallet_member_since);
                        authPreference.setDigitalWalletMessage(WEBSITE_DIGITAL_WALLET_TERMS);
                        authPreference.setGiftWalletMoney(Total_wallet_money_available);
                        authPreference.setFirstName(firstName);
                        authPreference.setLastName(lastName);
                        authPreference.setUserEmail(email);
                        authPreference.setUserPhone(phoneNumber);
                        authPreference.setCompanyStreet("" + SplashScreenActivity.customer_full_address);
                        authPreference.setUserPostcode("" + SplashScreenActivity.customer_postcode);


                        String successMsg = jObj.getString("success_msg");
                        Toast.makeText(getApplicationContext(), successMsg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, UpdateProfileActivity.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("email", email);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);

                        Log.e("customerid", customerId);
                    } else {
                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline(error_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login

                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("user_email", email);
                params.put("user_pass", password);
                params.put("user_phone", phoneNumber);
                params.put("referral_code", "" + edt_referalcode.getText().toString());
                params.put("device_id", "" + sharedPreferences.getString("token", ""));
                params.put("device_platform", "Android");
                params.put("customer_country", SplashScreenActivity.customer_country);
                params.put("customer_city", SplashScreenActivity.customer_city);
                params.put("customer_lat", SplashScreenActivity.customer_lat);
                params.put("customer_long", myPref.getLongitude());
                Log.e("params", "" + params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);*/


    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}