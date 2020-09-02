package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.justfoodz.utils.MyPref;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private MaterialEditText edtUserAddressTitle, edtFloorNumber, edtCustomerCity,
            edtCustomerState, edtCustomerPostcode, edtCustomerPhoneNumber, edt_address_direction;
    private TextView tvSubmit, edtCustomerStreetName;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    public int addressId;
    private String userAddressTitle, floorNumber, customerStreet, customerStreetNumber, customerCity, customerState, customerPostcode, customerCountry, customerPhoneNumber;

    private String date, time, itemId, id, updateTotalPrice = "", subTot = "", disPrice = "", deliveryChargeValue = "",
            serviceFees = "", packageFees = "", salesTaxAmount = "", vatTax = "", restaurantName, restaurantAddress, orderType = "";

    MyPref myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        myPref = new MyPref(CompleteProfileActivity.this);
        initialization();
        initializedListener();
//        statusBarColor();
        Toast.makeText(this, "lat " + myPref.getLatitude() + "   lng " + myPref.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        //       edtEmail = findViewById(R.id.edt_email);
        edtUserAddressTitle = findViewById(R.id.edt_address_name);
        edtFloorNumber = findViewById(R.id.edt_floor_number);
        edtCustomerStreetName = findViewById(R.id.edt_street_name);
        edtCustomerCity = findViewById(R.id.edt_town_city);
        edt_address_direction = findViewById(R.id.edt_address_direction);
        edtCustomerState = findViewById(R.id.edt_country_region);
        edtCustomerPostcode = findViewById(R.id.edt_postcode);
//        edtCustomerCountry = findViewById(R.id.edt_uk);
        edtCustomerPhoneNumber = findViewById(R.id.edt_phone_number);
        tvSubmit = findViewById(R.id.tv_submit_txt);
        authPreference = new AuthPreference(this);
//        edtEmail.setText(authPreference.getUserEmail());
        edtCustomerPhoneNumber.setText(authPreference.getUserPhone());
        addressId = getIntent().getIntExtra("addressId", 1);
        // userEmail = getIntent().getStringExtra("userEmail");
        userAddressTitle = getIntent().getStringExtra("addressTitle");
        floorNumber = getIntent().getStringExtra("gustUserFloor");
        customerStreetNumber = getIntent().getStringExtra("company_streetNo");
        customerStreet = getIntent().getStringExtra("companyStreet");
        customerCity = getIntent().getStringExtra("cityName");
        customerState = getIntent().getStringExtra("customerState");
        customerPostcode = getIntent().getStringExtra("zipCode");
        customerPhoneNumber = getIntent().getStringExtra("userPhone");
        //  edtEmail.setText(userEmail);
        edtUserAddressTitle.setText(userAddressTitle);
        edtFloorNumber.setText(floorNumber);
        edtCustomerStreetName.setText(myPref.getPickupAdd());
        edtCustomerCity.setText(myPref.getCity());
        edtCustomerState.setText(myPref.getState());
        edtCustomerPostcode.setText(customerPostcode);
        edtCustomerPhoneNumber.setText(customerPhoneNumber);
//        edtCustomerCountry.setText(customerStreet);
        edtCustomerPhoneNumber.setText(customerPhoneNumber);
        try {
            date = getIntent().getStringExtra("date");
            id = getIntent().getStringExtra("id");
            time = getIntent().getStringExtra("time");
            itemId = getIntent().getStringExtra("itemId");
            addressId = getIntent().getIntExtra("addressId", 1);
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
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit_txt:
                completeProfileValidation();
                break;
            default:
        }

    }

    public void completeProfileValidation() {
        customerPhoneNumber = edtCustomerPhoneNumber.getText().toString().trim();
        userAddressTitle = edtUserAddressTitle.getText().toString().trim();
        floorNumber = edtFloorNumber.getText().toString().trim();
        customerStreetNumber = edtCustomerStreetName.getText().toString().trim();
        customerCity = edtCustomerCity.getText().toString().trim();
        customerState = edtCustomerState.getText().toString().trim();
        customerPostcode = edtCustomerPostcode.getText().toString().trim();
//        customerCountry = edtCustomerCountry.getText().toString().trim();

        if (userAddressTitle.isEmpty()) {
            edtUserAddressTitle.setError("Enter Address Title e.g Office, Home etc");
            edtUserAddressTitle.requestFocus();
        } else if (customerStreetNumber.isEmpty()) {
            edtCustomerStreetName.setError("Enter Complete Address");
            edtCustomerStreetName.requestFocus();
        } else if (customerPhoneNumber.isEmpty()) {
            edtCustomerPhoneNumber.setError("Enter Phone Number");
            edtCustomerPhoneNumber.requestFocus();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(CompleteProfileActivity.this)) {
            addUserAddress();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserAddress() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ADD_NEW_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("qwqwresponse", "" + response);
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.optInt("success");
                    if (success == 1) {
                        String successMsg = obj.optString("success_msg");
                        showCustomDialog1decline(successMsg, "1");

                    } else if (success == 0) {
                        String errorMsg = obj.optString("error_msg");
                        showCustomDialog1decline(errorMsg, "2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("qwqwe", "" + e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("qwqwerror", "" + error);
                Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("user_phone", customerPhoneNumber);
                params.put("customerAddressLabel", userAddressTitle);
                params.put("customerAppFloor", floorNumber);
                params.put("customerStreet", customerStreetNumber);
                params.put("customerCity", "" + myPref.getCity());
                params.put("customerState", "");
                params.put("customerZipcode", "" + customerPostcode);
                params.put("customerCountry", "");
                params.put("CustomerId", customerId);
                params.put("address_direction", "" + edt_address_direction.getText().toString());
                params.put("customer_country", myPref.getState());
                params.put("customer_city", myPref.getCity());
                params.put("customer_lat", myPref.getLatitude());
                params.put("customer_long", myPref.getLongitude());
                params.put("check_out_address", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void statusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
    }

    private void showCustomDialog1decline(String s, final String todo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CompleteProfileActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1")) {
//                    startActivity(new Intent(CompleteProfileActivity.this, SelectAddressActivity.class));
                    Intent ii = new Intent(CompleteProfileActivity.this, SelectAddressActivity.class);
                    ii.putExtra("id", id);
                    ii.putExtra("restaurantName", restaurantName);
                    ii.putExtra("restaurantAddress", restaurantAddress);
                    ii.putExtra("date", date);
                    ii.putExtra("time", time);
                    String menuItemId = authPreference.getItemId();
                    ii.putExtra("itemId", menuItemId);
                    ii.putExtra("updateTotalPrice", updateTotalPrice);
                    ii.putExtra("subTot", subTot);
                    ii.putExtra("disPrice", disPrice);
                    ii.putExtra("deliveryChargeValue", deliveryChargeValue);
                    ii.putExtra("serviceFees", serviceFees);
                    ii.putExtra("packageFees", packageFees);
                    ii.putExtra("salesTaxAmount", salesTaxAmount);
                    ii.putExtra("vatTax", vatTax);
                    ii.putExtra("orderType", orderType);
                    //  intent.putExtra("subMenuModelArrayLists", subMenuModelArrayListReceive);
                    ii.putExtra("addressId", addressId);
//                  intent.putExtra("extrasModelArrayList", extrasModelArrayList);
//                  intent.putExtra("menuSizeModelArrayList", menuSizeModelArrayList);
                    startActivity(ii);
                    finish();
                } else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }

}
