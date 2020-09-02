package com.justfoodz.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.justfoodz.BuildConfig;
import com.justfoodz.MyProgressDialog;
import com.justfoodz.R;
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.Model_location;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvForgotPwd;
    private ImageView ivBack;
    private TextView tvSignUp, tvLogin;
    MaterialEditText edtEmail;
    EditText edtPassword;
    private String email, password;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    Dialog dialog, alertDialog;
    Button btnUpdate;
    private String userAddress = "";

    private String date, time, itemId, id, updateTotalPrice = "", subTot = "", disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", preOrderTime, orderType = "", restaurantName, restaurantAddress;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  statusBarColor();
        initialization();
        initializedListener();

    }

    private void initialization() {
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd);
        ivBack = findViewById(R.id.iv_back);
        tvSignUp = findViewById(R.id.tv_sign_up);
        tvLogin = findViewById(R.id.tv_login);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);


        if (BuildConfig.DEBUG) {
            edtEmail.setText("lmangal@gmail.com");
            edtPassword.setText("10TestTest");
        }
        authPreference = new AuthPreference(this);
        sharedPreferences = getSharedPreferences("noti", MODE_PRIVATE);
        try {
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            itemId = getIntent().getStringExtra("itemId");
            updateTotalPrice = getIntent().getStringExtra("updateTotalPrice");
            subTot = getIntent().getStringExtra("subTot");
            disPrice = getIntent().getStringExtra("disPrice");
            deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
            serviceFees = getIntent().getStringExtra("serviceFees");
            packageFees = getIntent().getStringExtra("packageFees");
            salesTaxAmount = getIntent().getStringExtra("salesTaxAmount");
            vatTax = getIntent().getStringExtra("vatTax");
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            orderType = getIntent().getStringExtra("orderType");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializedListener() {
        tvForgotPwd.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    public void loginValidation() {
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (!isValidEmail(email)) {
            edtEmail.setError("Enter Valid Email Address");
            edtEmail.requestFocus();
        } else if (password.isEmpty()) {
            edtPassword.setError("Enter Password");
            edtPassword.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(LoginActivity.this)) {
            userLogin();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_sign_up:
                Intent intent = new Intent(LoginActivity.this, RegisterCheckoutActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("restaurantName", restaurantName);
                intent.putExtra("restaurantAddress", restaurantAddress);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                String itemId = authPreference.getItemId();
                intent.putExtra("itemId", itemId);
                intent.putExtra("updateTotalPrice", updateTotalPrice);
                intent.putExtra("subTot", subTot);
                intent.putExtra("disPrice", disPrice);
                intent.putExtra("deliveryChargeValue", deliveryChargeValue);
//              intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                intent.putExtra("serviceFees", serviceFees);
                intent.putExtra("packageFees", packageFees);
                intent.putExtra("salesTaxAmount", salesTaxAmount);
                intent.putExtra("vatTax", vatTax);
                intent.putExtra("orderType", orderType);
                String customerId = authPreference.getCustomerId();
                intent.putExtra("customerId", customerId);
//                intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_login:
                loginValidation();
                // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                break;
            default:
                break;
        }
    }

    private void userLogin() {

        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_CustomerDetails> userLoginCall = interfaceRetrofit.userLogin(email, password, "" + sharedPreferences.getString("token", ""), "Android");
        userLoginCall.enqueue(new Callback<Model_CustomerDetails>() {
            @Override
            public void onResponse(Call<Model_CustomerDetails> call, retrofit2.Response<Model_CustomerDetails> response) {
                if (response.isSuccessful()) {
                    Model_CustomerDetails body = response.body();

                    if (body.getSuccess() == 0) {

                        authPreference.setCustomerId(body.getCustomerId());
                        authPreference.setReferal_sharingmsg(body.getReferral_sharing_Message());
                        authPreference.setReferal_codemsg(body.getReferral_codeMessage());
                        authPreference.setReferal_code(body.getReferral_code());
                        authPreference.setREferalEarnPoints(body.getReferral_earn_points());
                        authPreference.setReferalJoinFriends(body.getReferral_earn_points());
                        authPreference.setDigitalWallet_CardNumber(body.getDigital_wallet_card_number());
                        authPreference.setDigitalWallet_QrCode(body.getDigital_wallet_qr_code());
                        authPreference.setDigitalWallet_PINNumber(body.getDigital_wallet_card_pin_number());
                        authPreference.setDigitalWallet_MemerSince(body.getDigital_wallet_member_since());
                        authPreference.setDigitalWalletMessage(body.getWEBSITE_DIGITAL_WALLET_TERMS());
                        authPreference.setGiftWalletMoney(body.getTotal_wallet_money_available());
                        authPreference.setFirstName(body.getFirst_name());
                        authPreference.setUserEmail(body.getUser_email());
                        authPreference.setLastName(body.getLast_name());
                        authPreference.setUserPhone(body.getUser().getDeliveryaddress().get(0).getUser_phone());

                        authPreference.setCompanyStreet(body.getUser().getDeliveryaddress().get(0).getCompany_street());
                        authPreference.setCustomerFloor(body.getUser().getDeliveryaddress().get(0).getCompany_streetNo());
                        authPreference.setCustomerCity(body.getUser_city());
                        authPreference.setCustomerState(body.getUser_state());
                        authPreference.setUserPostcode(body.getUser_postcode());
                        authPreference.setUserPhoto(body.getUser_photo());
                        authPreference.setUserAddress(body.getUser_address());
                        authPreference.setCustomerCountry(body.getUser().getDeliveryaddress().get(0).getCustomerCountry());


                        showPopup();
                    } else if (body.getSuccess() == 2) {
                        showCustomDialog1decline(body.getSuccess_msg());
                    } else if (body.getSuccess() == 1) {
                        showCustomDialog1decline(body.getSuccess_msg());
                    }

                }
                MyProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Model_CustomerDetails> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(this, HomeActivity.class));
    }


    public void showPopup() {
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.pop_login_update);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        btnUpdate = (Button) alertDialog.findViewById(R.id.btnOk);
        btnUpdate.setOnClickListener(v -> {
            try {
                if (authPreference.getUserAddress().isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, UpdateProfileActivity.class);
                    intent.putExtra("firstName", authPreference.getFirstName());
                    intent.putExtra("lastName", authPreference.getLastName());
                    intent.putExtra("email", authPreference.getUserEmail());
                    intent.putExtra("phoneNumber", authPreference.getUserPhone());
                    intent.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                    intent.putExtra("streetName", authPreference.getCompanyStreet());
                    intent.putExtra("zipCode", authPreference.getUserPostcode());
                    intent.putExtra("userCity", authPreference.getUserCity());
                    intent.putExtra("userState", authPreference.getUserState());
                    intent.putExtra("userAddress", userAddress);
                    startActivity(intent);
                    finish();
                } else {
                    if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                        Intent intent = new Intent(LoginActivity.this, PaymentActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("updateTotalPrice", updateTotalPrice);
                        intent.putExtra("subTot", subTot);
                        intent.putExtra("disPrice", disPrice);
                        intent.putExtra("deliveryChargeValue", deliveryChargeValue);
                        //intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                        intent.putExtra("serviceFees", serviceFees);
                        intent.putExtra("packageFees", packageFees);
                        intent.putExtra("salesTaxAmount", salesTaxAmount);
                        intent.putExtra("vatTax", vatTax);
                        intent.putExtra("orderType", orderType);
                        String customerId = authPreference.getCustomerId();
                        intent.putExtra("customerId", customerId);
//                        intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                        intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                        startActivity(intent);
                        finish();

                    } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                        Intent intent = new Intent(LoginActivity.this, SelectAddressActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("restaurantName", restaurantName);
                        intent.putExtra("restaurantAddress", restaurantAddress);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("updateTotalPrice", updateTotalPrice);
                        intent.putExtra("subTot", subTot);
                        intent.putExtra("disPrice", disPrice);
                        intent.putExtra("deliveryChargeValue", deliveryChargeValue);
                        intent.putExtra("serviceFees", serviceFees);
                        intent.putExtra("packageFees", packageFees);
                        intent.putExtra("salesTaxAmount", salesTaxAmount);
                        intent.putExtra("vatTax", vatTax);
//                          intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                        intent.putExtra("orderType", orderType);
                        String customerId = authPreference.getCustomerId();
                        intent.putExtra("customerId", customerId);
                        String customerAddressId = authPreference.getCustomerAddressId();
                        intent.putExtra("customerAddressId", customerAddressId);
//                          intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                          intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("qwerty", "" + e);
            }
        });
    }

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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