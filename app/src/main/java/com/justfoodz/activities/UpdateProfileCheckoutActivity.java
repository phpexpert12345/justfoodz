package com.justfoodz.activities;

import android.app.AlertDialog;
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
import com.justfoodz.retrofit.InterUserdata;
import com.justfoodz.retrofit.RetrofitAdapter;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.MyProgressDialog;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

import static com.justfoodz.activities.CartActivity.delivery_to_show;
import static com.justfoodz.activities.CartActivity.tvGrandTotalPrice;

public class UpdateProfileCheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private Button tvSubmit;
    private MaterialEditText edtFirstName, edtLastName, edtEmail, edtPhoneNumber, edtAddressLabel,
            edtFloor, edtStreetName, edtZipCode, edtCity, edtState, edtCountry;
    private AuthPreference authPreference;
    private String firstName, lastName, userPhone, userEmail, password, addressLabel, floor, streetName, zipCode, userCity, userState, userCountry;
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    //    public ArrayList<SubMenuModel> subMenuModelArrayListReceive = null;
//    private ArrayList<MenuSizeModel> menuSizeModelArrayList;
//    private ArrayList<ExtrasModel> extrasModelArrayList;
    private String date, time, itemId, id, disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", preOrderTime, updateTotalPrice, orderType = "", restaurantName, restaurantAddress, subTot;

    MyPref myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_checkout);
        myPref= new MyPref(UpdateProfileCheckoutActivity.this);
        initialization();
        initializedListener();
        statusBarColor();
    }


    private void initialization() {
        authPreference = new AuthPreference(this);
        sharedPreferences = getSharedPreferences("noti", MODE_PRIVATE);
        ivBack = findViewById(R.id.iv_back);
        tvSubmit = findViewById(R.id.tv_submit_txt);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtAddressLabel = findViewById(R.id.edt_address_label);
        edtFloor = findViewById(R.id.edt_floor);
        edtStreetName = findViewById(R.id.edt_street_name);
        edtZipCode = findViewById(R.id.edt_zip_code);
        edtCity = findViewById(R.id.edt_city);
        edtState = findViewById(R.id.edt_state);
        edtCountry = findViewById(R.id.edt_country);

        try {

            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            userEmail = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            userPhone = getIntent().getStringExtra("phoneNumber");
            authPreference.setFirstName(firstName);
            authPreference.setLastName(lastName);
            authPreference.setUserEmail(userEmail);
            authPreference.setUserPhone(userPhone);
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            itemId = getIntent().getStringExtra("itemId");
            disPrice = getIntent().getStringExtra("disPrice");
            deliveryChargeValue = getIntent().getStringExtra("deliveryChargeValue");
            serviceFees = getIntent().getStringExtra("serviceFees");
            packageFees = getIntent().getStringExtra("packageFees");
            salesTaxAmount = getIntent().getStringExtra("salesTaxAmount");
            vatTax = getIntent().getStringExtra("vatTax");
            restaurantName = getIntent().getStringExtra("restaurantName");
            restaurantAddress = getIntent().getStringExtra("restaurantAddress");
            orderType = getIntent().getStringExtra("orderType");
            updateTotalPrice = getIntent().getStringExtra("updateTotalPrice");
            subTot = getIntent().getStringExtra("subTot");

            edtFirstName.setText(firstName);
            edtLastName.setText(lastName);
            edtEmail.setText(userEmail);
            edtPhoneNumber.setText(userPhone);
            edtStreetName.setText("" + myPref.getPickupAdd());
            edtZipCode.setText("" + myPref.getFirebaseTokenId());
          /*  edtFirstName.setEnabled(false);
            edtLastName.setEnabled(false);
            edtEmail.setEnabled(false);
            edtPhoneNumber.setEnabled(false);
*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                HomeActivity.mDrawerLayout.closeDrawer(HomeActivity.rl_main_left_drawer);
                break;
            case R.id.tv_submit_txt:
                updateProfileValidation();
                break;
            default:
                break;
        }
    }

    public void updateProfileValidation() {
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        userEmail = edtEmail.getText().toString().trim();
        userPhone = edtPhoneNumber.getText().toString().trim();
        addressLabel = edtAddressLabel.getText().toString().trim();
//        floor = edtFloor.getText().toString().trim();
        streetName = edtStreetName.getText().toString().trim();
        zipCode = edtZipCode.getText().toString().trim();
        userCity = edtCity.getText().toString().trim();
        //  userState = edtState.getText().toString().trim();
        userCountry = edtCountry.getText().toString().trim();
        authPreference.setCompanyStreet(streetName);
        authPreference.setUserPostcode(zipCode);
        authPreference.setUserCity(userCity);
        //authPreference.setUserState(userState);
        authPreference.setCustomerCountry(userCountry);
        authPreference.setCompanyStreetNo(floor);

        if (firstName.isEmpty()) {
            edtFirstName.setError("Enter First Name");
            edtFirstName.requestFocus();
        } else if (lastName.isEmpty()) {
            edtLastName.setError("Enter Last Name");
            edtLastName.requestFocus();
        } else if (!isValidEmail(userEmail)) {
            edtEmail.setError("Enter Valid Email Address");
            edtEmail.requestFocus();
        } else if (userPhone.isEmpty()) {
            edtPhoneNumber.setError("Enter Phone Number");
            edtPhoneNumber.requestFocus();
        } else if (addressLabel.isEmpty()) {
            edtAddressLabel.setError("Enter Address Title e.g Office, Home etc");
            edtAddressLabel.requestFocus();
        } else if (streetName.isEmpty()) {
            edtStreetName.setError("Enter Complete Address");
            edtStreetName.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(UpdateProfileCheckoutActivity.this)) {
            registerCheckout();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerCheckout() {


        MyProgressDialog.show(this, R.string.please_wait);
        InterUserdata interfaceRetrofit = RetrofitAdapter.createService(InterUserdata.class);
        Call<Model_UpdateProfileUpdateNew> userLoginCall = interfaceRetrofit.phpexpert_customer_checkout_account_registers(firstName, lastName, userEmail, password, userPhone, streetName, "hhh",
                streetName, userCity, userCountry, zipCode, userCountry, "" + sharedPreferences.getString("token", ""), "Android", "" + getIntent().getStringExtra("referral_code"),
                myPref.getState(), myPref.getCity(), myPref.getLatitude(), myPref.getLongitude());
        userLoginCall.enqueue(new Callback<Model_UpdateProfileUpdateNew>() {
            @Override
            public void onResponse(Call<Model_UpdateProfileUpdateNew> call, retrofit2.Response<Model_UpdateProfileUpdateNew> response) {
                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        Model_UpdateProfileUpdateNew body = response.body();

                        String customerId = body.getCustomerId();
                        String customerAddressId = body.getCustomerAddressId();


                        authPreference.setDigitalWallet_CardNumber(body.getDigital_wallet_card_number());
                        authPreference.setDigitalWallet_QrCode(body.getDigital_wallet_qr_code());
                        authPreference.setDigitalWallet_PINNumber(body.getDigital_wallet_card_pin_number());
                        authPreference.setDigitalWallet_MemerSince(body.getDigital_wallet_member_since());
                        authPreference.setDigitalWalletMessage(body.getWEBSITE_DIGITAL_WALLET_TERMS());
                        authPreference.setGiftWalletMoney(body.getTotal_wallet_money_available());

                        authPreference.setCustomerId(body.getCustomerId());
                        authPreference.setCustomerAddressId(customerAddressId);
                        if (customerId.isEmpty() && customerAddressId.isEmpty()) {
                            Intent intent = new Intent(UpdateProfileCheckoutActivity.this, LoginActivity.class);
                            finish();
                        } else {
                            try {
                                if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, PaymentActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("restaurantName", restaurantName);
                                    intent.putExtra("restaurantAddress", restaurantAddress);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time", time);
                                    String itemId = authPreference.getItemId();
                                    intent.putExtra("itemId", itemId);
                                    intent.putExtra("disPrice", "" + disPrice);
                                    intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                                    // intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                                    intent.putExtra("serviceFees", "" + serviceFees);
                                    intent.putExtra("packageFees", "" + packageFees);
                                    intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                                    intent.putExtra("vatTax", "" + vatTax);
                                    intent.putExtra("orderType", orderType);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerAddressId", customerAddressId);
//                                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                                    intent.putExtra("updateTotalPrice", updateTotalPrice);
                                    intent.putExtra("subTot", subTot);
                                    startActivity(intent);
                                    finish();

                                } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, SelectAddressActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("restaurantName", restaurantName);
                                    intent.putExtra("restaurantAddress", restaurantAddress);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time", time);
                                    String itemId = authPreference.getItemId();
                                    intent.putExtra("itemId", itemId);
                                    intent.putExtra("disPrice", "" + disPrice);
                                    intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                                    intent.putExtra("serviceFees", "" + serviceFees);
                                    intent.putExtra("packageFees", "" + packageFees);
                                    intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                                    intent.putExtra("vatTax", "" + vatTax);
//                                    intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                                    intent.putExtra("orderType", orderType);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerAddressId", customerAddressId);
                                    intent.putExtra("updateTotalPrice", updateTotalPrice);
                                    intent.putExtra("subTot", subTot);
//                                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        showCustomDialog1decline(response.body().getError_msg());
                    }


                    MyProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Model_UpdateProfileUpdateNew> call, Throwable t) {
                Toast.makeText(UpdateProfileCheckoutActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                MyProgressDialog.dismiss();
            }
        });


       /* pDialog = new

                ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.REGISTER_CHECKOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                pDialog.dismiss();
                Log.e("response", "" + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String success_msg = jObj.getString("success_msg");
                        String customerId = jObj.getString("CustomerId");
                        String customerAddressId = jObj.getString("CustomerAddressId");
                        String digital_wallet_card_number = jObj.optString("digital_wallet_card_number");
                        String digital_wallet_qr_code = jObj.optString("digital_wallet_qr_code");
                        String digital_wallet_card_pin_number = jObj.optString("digital_wallet_card_pin_number");
                        String digital_wallet_member_since = jObj.optString("digital_wallet_member_since");
                        String WEBSITE_DIGITAL_WALLET_TERMS = jObj.optString("WEBSITE_DIGITAL_WALLET_TERMS");
                        String Total_wallet_money_available = jObj.optString("Total_wallet_money_available");


                        authPreference.setDigitalWallet_CardNumber(digital_wallet_card_number);
                        authPreference.setDigitalWallet_QrCode(digital_wallet_qr_code);
                        authPreference.setDigitalWallet_PINNumber(digital_wallet_card_pin_number);
                        authPreference.setDigitalWallet_MemerSince(digital_wallet_member_since);
                        authPreference.setDigitalWalletMessage(WEBSITE_DIGITAL_WALLET_TERMS);
                        authPreference.setGiftWalletMoney(Total_wallet_money_available);

                        authPreference.setCustomerId(customerId);
                        authPreference.setCustomerAddressId(customerAddressId);
                        if (customerId.isEmpty() && customerAddressId.isEmpty()) {
                            Intent intent = new Intent(UpdateProfileCheckoutActivity.this, LoginActivity.class);
                            finish();
                        } else {
                            try {
                                if (orderType.equalsIgnoreCase("Collection") || orderType.equalsIgnoreCase("Pre Order Collection")) {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, PaymentActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("restaurantName", restaurantName);
                                    intent.putExtra("restaurantAddress", restaurantAddress);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time", time);
                                    String itemId = authPreference.getItemId();
                                    intent.putExtra("itemId", itemId);
                                    intent.putExtra("disPrice", "" + disPrice);
                                    intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                                    // intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                                    intent.putExtra("serviceFees", "" + serviceFees);
                                    intent.putExtra("packageFees", "" + packageFees);
                                    intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                                    intent.putExtra("vatTax", "" + vatTax);
                                    intent.putExtra("orderType", orderType);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerAddressId", customerAddressId);
//                                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                                    intent.putExtra("updateTotalPrice", updateTotalPrice);
                                    intent.putExtra("subTot", subTot);
                                    startActivity(intent);
                                    finish();

                                } else if (orderType.equalsIgnoreCase("Delivery") || orderType.equalsIgnoreCase("Pre Order Delivery")) {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, SelectAddressActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("restaurantName", restaurantName);
                                    intent.putExtra("restaurantAddress", restaurantAddress);
                                    intent.putExtra("date", date);
                                    intent.putExtra("time", time);
                                    String itemId = authPreference.getItemId();
                                    intent.putExtra("itemId", itemId);
                                    intent.putExtra("disPrice", "" + disPrice);
                                    intent.putExtra("deliveryChargeValue", "" + deliveryChargeValue);
                                    intent.putExtra("serviceFees", "" + serviceFees);
                                    intent.putExtra("packageFees", "" + packageFees);
                                    intent.putExtra("salesTaxAmount", "" + salesTaxAmount);
                                    intent.putExtra("vatTax", "" + vatTax);
//                                    intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                                    intent.putExtra("orderType", orderType);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerAddressId", customerAddressId);
                                    intent.putExtra("updateTotalPrice", updateTotalPrice);
                                    intent.putExtra("subTot", subTot);
//                                    intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                                    intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(UpdateProfileCheckoutActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (success == 0) {
                        String error_msg = jObj.getString("error_msg");
                        showCustomDialog1decline(error_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please check your network co", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer login
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("user_email", userEmail);
                params.put("user_pass", password);
                params.put("user_phone", userPhone);
                params.put("customerAddressLabel", streetName);
                params.put("customerAppFloor", "hhh");
                params.put("customerStreet", streetName);
                params.put("customerCity", userCity);
                params.put("customerState", userCountry);
                params.put("customerZipcode", zipCode);
                params.put("customerCountry", userCountry);
                params.put("device_id", "" + sharedPreferences.getString("token", ""));
                params.put("device_platform", "Android");
                params.put("referral_code", "" + getIntent().getStringExtra("referral_code"));
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
        final AlertDialog alertDialog = new AlertDialog.Builder(UpdateProfileCheckoutActivity.this).create();
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}