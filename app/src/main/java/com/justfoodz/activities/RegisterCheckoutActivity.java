package com.justfoodz.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

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
import androidx.core.content.ContextCompat;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterCheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvSignUp, tvAlreadyAccount;
    private ImageView ivBack;
    private EditText edtFirstName, edtLastName, edtEmail, edtPhoneNumber, edtPassword,edt_referalcode;
    private String firstName, lastName, email, phoneNumber, password;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;

    private String date, time, itemId, customerId, id, disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", preOrderTime, updateTotalPrice, orderType = "", restaurantName, restaurantAddress,subTot;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_checkout);
        initialization();
        initializedListener();
        statusBarColor();
        authPreference = new AuthPreference(this);
    }


    private void initialization() {
        sharedPreferences = getSharedPreferences("noti",MODE_PRIVATE);
        tvAlreadyAccount = findViewById(R.id.tv_already_account);
        ivBack = findViewById(R.id.iv_back);
        tvSignUp = findViewById(R.id.tv_sign_up);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtPassword = findViewById(R.id.edt_password);
        edt_referalcode = findViewById(R.id.edt_referalcode);

        try {
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            disPrice = getIntent().getStringExtra("disPrice");
            deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
            serviceFees = getIntent().getStringExtra("serviceFees");
            packageFees = getIntent().getStringExtra("packageFees");
            salesTaxAmount = getIntent().getStringExtra("salesTaxAmount");
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            vatTax = getIntent().getStringExtra("vatTax");
            orderType = getIntent().getStringExtra("orderType");
            // itemId = getIntent().getStringExtra("itemId");
            updateTotalPrice = getIntent().getStringExtra("updateTotalPrice");
            customerId = getIntent().getStringExtra("customerId");
            subTot = getIntent().getStringExtra("subTot");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_sign_up:
                registerValidation();
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
        } else if (com.justfoodz.utils.Network.isNetworkCheck(RegisterCheckoutActivity.this)) {
            Intent intent = new Intent(this, UpdateProfileCheckoutActivity.class);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("id", id);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            String itemId = authPreference.getItemId();
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("itemId", itemId);
            intent.putExtra("disPrice", "" + disPrice);
            intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
          //  intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
            intent.putExtra("serviceFees", "" + serviceFees);
            intent.putExtra("packageFees", "" + packageFees);
            intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
            intent.putExtra("vatTax", "" + vatTax);
            intent.putExtra("orderType", orderType);
            String customerId = authPreference.getCustomerId();
            intent.putExtra("customerId", customerId);
            intent.putExtra("updateTotalPrice", updateTotalPrice);
            intent.putExtra("subTot", subTot);
            intent.putExtra("referral_code", ""+edt_referalcode.getText().toString());
            startActivity(intent);
            finish();
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
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.URL_ACCOUNT_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        int error = jObj.getInt("error");
                        String customerId = jObj.getString("CustomerId");
                        authPreference.setCustomerId(jObj.getString("CustomerId"));
                        String successMsg = jObj.getString("success_msg");
                        Toast.makeText(getApplicationContext(), successMsg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterCheckoutActivity.this, UpdateProfileCheckoutActivity.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("email", email);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                        finish();


                    } else {
                        String error_msg = jObj.getString("error_msg");
                        //  "error_msg": "Sorry ! There is following field is blank try again"
                        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

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
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("user_email", email);
                params.put("user_pass", password);
                params.put("user_phone", phoneNumber);
                params.put("device_id", ""+sharedPreferences.getString("token",""));
                params.put("device_platform", "Android");
                Log.e("params",""+params);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}