package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileUserAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private MaterialEditText edtEmail, edtUserAddressTitle, edtFloorNumber, edtCustomerStreetName, edtCustomerCity,
            edtCustomerState, edtCustomerPostcode, edtCustomerCountry, edtCustomerPhoneNumber,edt_address_direction;
    private Button tvSubmit;
    private ProgressDialog pDialog;
    private AuthPreference authPreference;
    public int addressId;
    String customerAddressId;
    private String userEmail, userAddress, userAddressTitle, floorNumber, customerStreet, update, customerCity, customerState, customerPostcode, customerCountry, customerPhoneNumber;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_user_address);
        initialization();
        initializedListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initialization() {
        ivBack = findViewById(R.id.iv_back);
        edtEmail = findViewById(R.id.edt_email);
        edtUserAddressTitle = findViewById(R.id.edt_address_name);
        edtFloorNumber = findViewById(R.id.edt_floor_number);
        edtCustomerStreetName = findViewById(R.id.edt_street_name);
        edtCustomerCity = findViewById(R.id.edt_town_city);
        edtCustomerState = findViewById(R.id.edt_country_region);
        edtCustomerPostcode = findViewById(R.id.edt_postcode);
        edt_address_direction = findViewById(R.id.edt_address_direction);
//        edtCustomerCountry = findViewById(R.id.edt_uk);
        edtCustomerPhoneNumber = findViewById(R.id.edt_phone_number);
        tvSubmit = findViewById(R.id.tv_submit_txt);
        authPreference = new AuthPreference(this);
        edtEmail.setText(authPreference.getUserEmail());
        edtCustomerPhoneNumber.setText(authPreference.getUserPhone());
        addressId = getIntent().getIntExtra("addressId", 1);
        edtEmail.setText(authPreference.getUserEmail());
        edtCustomerPhoneNumber.setText(authPreference.getUserPhone());



        if(getIntent().getExtras()!=null)
        { update = getIntent().getStringExtra("update");
            if(update.equals("2"))
            {edtCustomerStreetName.setTextColor(getColor(R.color.black));

                edtCustomerStreetName.setText(""+UserAddressActivity.customer_full_address);
                edtCustomerStreetName.setEnabled(false);


            }
            else {
                edtCustomerStreetName.setText(""+UserAddressActivity.customer_full_address);
                edtCustomerStreetName.setEnabled(true);
            }
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
                if (addressId == 0) {
                    Intent intent = new Intent(new Intent(CompleteProfileUserAddressActivity.this, MyAccountActivity.class));
                    intent.putExtra("userAddress", authPreference.getUserAddress());
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
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
        customerStreet = edtCustomerStreetName.getText().toString().trim();
        customerCity = edtCustomerCity.getText().toString().trim();
        customerState = edtCustomerState.getText().toString().trim();
        customerPostcode = edtCustomerPostcode.getText().toString().trim();
//        customerCountry = edtCustomerCountry.getText().toString().trim();
        edtUserAddressTitle.setText(userAddressTitle);
        edtFloorNumber.setText(floorNumber);
        edtCustomerStreetName.setText(customerStreet);
        edtCustomerCity.setText(customerCity);
        edtCustomerState.setText(customerState);
        edtCustomerPostcode.setText(customerPostcode);
//        edtCustomerCountry.setText(customerCountry);
        if (userAddressTitle.isEmpty()) {
            edtUserAddressTitle.setError("Enter Address Title e.g Office, Home etc");
            edtUserAddressTitle.requestFocus();
        }  else if (customerStreet.isEmpty()) {
            edtCustomerStreetName.setError("Enter Complete Address");
            edtCustomerStreetName.requestFocus();
        } else if (customerPhoneNumber.isEmpty()) {
            edtCustomerPhoneNumber.setError("Enter phone number");
            edtCustomerPhoneNumber.requestFocus();
//        } else if (customerCountry.isEmpty()) {
//            Toast.makeText(getApplicationContext(), R.string.enter_user_country_txt, Toast.LENGTH_SHORT).show();
        } else if (com.justfoodz.utils.Network.isNetworkCheck(CompleteProfileUserAddressActivity.this)) {

            if(update.equals("2"))
            {
                addUserAddress("1");
            }
            else addUserAddress("");

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }


    public void addUserAddress(final String checkout) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.ADD_NEW_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("response",""+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.optInt("success");
                    if (success == 1) {
                        String successMsg = obj.optString("success_msg");
                         customerAddressId = obj.optString("CustomerAddressId");//customer Address id
                        showCustomDialog1decline(successMsg,"1");

                    } else if (success == 0) {
                        String errorMsg = obj.optString("error_msg");
                        showCustomDialog1decline(errorMsg,"2");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e",""+e);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",""+error);
                Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_LONG).show();
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
                params.put("customerStreet", customerStreet);
                params.put("customerCity", customerCity);
                params.put("customerState", customerState);
                params.put("customerZipcode", customerPostcode);
                params.put("customerCountry", "");
                params.put("CustomerId", customerId);
                params.put("address_direction", ""+edt_address_direction.getText().toString());
                params.put("customer_country",UserAddressActivity.customer_country);
                params.put("customer_city",UserAddressActivity.customer_city);
                params.put("customer_lat",UserAddressActivity.customer_lat);
                params.put("customer_long",UserAddressActivity.customer_long);
                params.put("check_out_address",checkout);
                Log.e("params",""+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }



    private void showCustomDialog1decline (String s, final String todo)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(CompleteProfileUserAddressActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1"))
                {
                    Intent intent = new Intent(CompleteProfileUserAddressActivity.this, UserAddressActivity.class);
                    intent.putExtra("email", authPreference.getUserEmail());
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber);
                    intent.putExtra("userAddressTitle", userAddressTitle);
                    intent.putExtra("floorNumber", floorNumber);
                    intent.putExtra("customerStreet", customerStreet);
                    intent.putExtra("customerCity", customerCity);
                    intent.putExtra("customerState", customerState);
                    intent.putExtra("customerPostcode", customerPostcode);
                    intent.putExtra("customerCountry", customerCountry);
                    intent.putExtra("CustomerAddressId", customerAddressId);
                    intent.putExtra("userAddress", authPreference.getUserAddress());
                    startActivity(intent);
                    finish();
//                    alertDialog.dismiss();
                }
                else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (addressId == 0) {
            startActivity(new Intent(CompleteProfileUserAddressActivity.this, UserAddressActivity.class));
            finish();
        } else {
            startActivity(new Intent(CompleteProfileUserAddressActivity.this, UserAddressActivity.class));
            finish();
        }
    }
}