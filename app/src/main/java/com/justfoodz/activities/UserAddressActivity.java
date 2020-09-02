package com.justfoodz.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.location.LocationRequest;
import com.justfoodz.R;
import com.justfoodz.adapters.AddressDeleteAdapter;
import com.justfoodz.interfaces.AddressItemClickListener;
import com.justfoodz.models.AddressModel;
import com.justfoodz.utils.AuthPreference;
import com.justfoodz.utils.UrlConstants;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UserAddressActivity extends AppCompatActivity implements View.OnClickListener, AddressItemClickListener, LocationListener {
    private AuthPreference authPreference;
    private ImageView ivBack;
    private TextView tvAddNewAddress,tvCurrent;
    private AddressDeleteAdapter deleteAdapter;
    private RecyclerView rvUserAddressList;
    private ProgressDialog pDialog;
    private ArrayList<AddressModel> addressModelArrayList = new ArrayList<AddressModel>();
    public AddressModel addressModel;
    public int addressId;
    public Dialog dialog;
    private AddressDeleteAdapter addressDeleteAdapter;
    private AddressItemClickListener addressItemClickListener;
    private MaterialEditText edtEmail, edtUserAddressTitle, edtFloorNumber, edtCustomerStreetName, edtCustomerCity,
            edtCustomerState, edtCustomerPostcode, edtCustomerCountry, edtCustomerPhoneNumber,edt_address_direction;
    public int updateindexpos;
    private String CustomerAddressId, userEmail, userPhone, addressTitle, userAddress, userFloor, userStreetName, userZipCode, city, state, customerCountry;

    ImageView imageblank;
    TextView emptytxt;
    LinearLayout layout2,linearCurrent;

    double latitude, longitude;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    Location location;
    String  lat="", lng="";
    RequestQueue requestQueue;
    ImageView imgnoapp;
    public static String customer_city,customer_locality, customer_country,customer_country_code,customer_postcode
            ,customer_full_address, customer_lat, customer_long, customer_search, customer_search_display,
            currency_symbol;
    private final int CURRENT_LOCATION_UPDATE = 500;

    Handler ha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        emptytxt=findViewById(R.id.emptytxt);
        imageblank=findViewById(R.id.imageblank);
        layout2=findViewById(R.id.layout);
        linearCurrent=findViewById(R.id.linearCurrent);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        initialization();
        initializedListener();
        statusBarColor();
        if (com.justfoodz.utils.Network.isNetworkCheck(
                UserAddressActivity.this)) {
            userAddressList();

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

         ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                fn_getlocation();

                ha.postDelayed(this, 10000);
            }
        }, 500);
}






    private void initialization() {
        authPreference = new AuthPreference(this);
        ivBack = findViewById(R.id.iv_back);
        tvCurrent = findViewById(R.id.tvCurrent);
        tvAddNewAddress = findViewById(R.id.tv_add_new_address);
        rvUserAddressList = findViewById(R.id.rv_user_address_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvUserAddressList.setLayoutManager(mLayoutManager);
        rvUserAddressList.setItemAnimator(new DefaultItemAnimator());
        if(getIntent().getExtras()!=null){
            userEmail = getIntent().getStringExtra("email");
            userPhone = getIntent().getStringExtra("customerPhoneNumber");
            addressTitle = getIntent().getStringExtra("userAddressTitle");
            userFloor = getIntent().getStringExtra("floorNumber");//floor
            userStreetName = getIntent().getStringExtra("customerStreet");
            userZipCode = getIntent().getStringExtra("customerPostcode");
            city = getIntent().getStringExtra("customerCity");
            state = getIntent().getStringExtra("customerState");
            customerCountry = getIntent().getStringExtra("customerCountry");
            userAddress = getIntent().getStringExtra("userAddress");
            CustomerAddressId = getIntent().getStringExtra("CustomerAddressId");


        }


    }

    private void initializedListener() {
        ivBack.setOnClickListener(this);
        tvAddNewAddress.setOnClickListener(this);
        linearCurrent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_new_address:
                Intent intent = new Intent(UserAddressActivity.this, CompleteProfileUserAddressActivity.class);
                intent.putExtra("update", "1");
                intent.putExtra("addressId", addressId);
                intent.putExtra("userEmail", authPreference.getUserEmail());
                intent.putExtra("userPhone", authPreference.getUserPhone());
                intent.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                intent.putExtra("streetName", authPreference.getCompanyStreet());
                intent.putExtra("zipCode", authPreference.getUserPostcode());
                intent.putExtra("userCity", authPreference.getUserCity());
                intent.putExtra("userState", authPreference.getUserState());
                intent.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(intent);
                finish();
                break;
            case R.id.linearCurrent:
                Intent intent1 = new Intent(UserAddressActivity.this, CompleteProfileUserAddressActivity.class);
                intent1.putExtra("update", "2");
                intent1.putExtra("addressId", addressId);
                intent1.putExtra("userEmail", authPreference.getUserEmail());
                intent1.putExtra("userPhone", authPreference.getUserPhone());
                intent1.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                intent1.putExtra("streetName", authPreference.getCompanyStreet());
                intent1.putExtra("zipCode", authPreference.getUserPostcode());
                intent1.putExtra("userCity", authPreference.getUserCity());
                intent1.putExtra("userState", authPreference.getUserState());
                intent1.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(intent1);
                finish();


                break;
        }

    }


    public void getData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.open_location, response -> {
            Log.e("searchdata",""+response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String success = jsonObject.getString("success");
                if (success.equals("0"))
                {
                    customer_city = jsonObject.getString("customer_city");
                    customer_locality = jsonObject.getString("customer_locality");
                    customer_country = jsonObject.getString("customer_country");
                    customer_country_code = jsonObject.getString("customer_country_code");
                    customer_postcode = jsonObject.getString("customer_postcode");
                    customer_full_address = jsonObject.getString("customer_full_address");
                    customer_lat = jsonObject.getString("customer_lat");
                    customer_long = jsonObject.getString("customer_long");
                    customer_search = jsonObject.getString("customer_search");
                    customer_search_display = jsonObject.getString("customer_search_display");
                     tvCurrent.setText(""+customer_full_address);

//                    if(currency_symbol!=null)
//                    {
//                        currency_symbol = SplashScreenActivity.Utils.getCurrencySymbol(jsonObject.getString("customer_currency"));
//
//                    }
//
//                    else  currency_symbol="$";
////                        currency_symbol = Utils.getCurrencySymbol(jsonObject.getString("customer_currency"));
//
//                    Log.e("response",""+currency_symbol+""+jsonObject.getString("customer_currency"));



//                    startActivity(new Intent(UserAddressActivity.this, HomeActivity.class));
//                    finish();
                }else {
                    imgnoapp.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("customer_long",lng);
                params.put("customer_lat",lat);
                Log.e("user","activity"+params);

                return params;

            }
        };
        requestQueue.add(stringRequest);
    }



    private void fn_getlocation() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lat = String.valueOf(latitude);
                        lng = String.valueOf(longitude);
                     //    Toast.makeText(this, "networl"+lat+lng, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//
                        lat = (String.format("%.6f",latitude));
                        lng = (String.format("%.6f",longitude));


                    }
                }
            }

        }

        if (lat.equals("")||lat.equals(null)) {
            fn_getlocation();
        }else {
            getData();
        }
    }


    public void userAddressList() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlConstants.USER_ADDRESS_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 pDialog.dismiss();
                Log.e("add",""+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String successMsg = "";
                    int success= obj.getInt("success");
                    if (success==1){
                        String success_msg= obj.getString("success_msg");
                        rvUserAddressList.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        imageblank.setVisibility(View.VISIBLE);
                        emptytxt.setVisibility(View.VISIBLE);
                        emptytxt.setText(success_msg);


                    }else {
                        rvUserAddressList.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        imageblank.setVisibility(View.GONE);
                        emptytxt.setVisibility(View.GONE);
                        successMsg = obj.optString("success_msg");
                        String customerId = obj.optString("CustomerId");
                        JSONObject user = obj.getJSONObject("user");
                        JSONArray jsonArray = user.getJSONArray("deliveryaddress");
                        for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                addressId = jsonObject.optInt("id");//addressId
                                String userAddress = jsonObject.optString("address");  //user address
                                String userPhone = jsonObject.optString("user_phone");
                                String zipCode = jsonObject.optString("zipcode");
                                String cityName = jsonObject.optString("city_name");
                                String userLocality = jsonObject.optString("user_locality");
                                String customerState = jsonObject.optString("customerState");
                                String customerCountry = jsonObject.optString("customerCountry");
                                String companyStreet = jsonObject.optString("company_street");
                                String gustUserLandLineAddress = jsonObject.optString("GustUserLandlineAdress");
                                String gustUserFloor = jsonObject.optString("GustUserFloor");
                                String company_streetNo = jsonObject.optString("company_streetNo");
                                String address_direction = jsonObject.optString("address_direction");
                                String addressTitle = jsonObject.optString("address_title");//Home,Office
                                addressModel = new AddressModel();
                                addressModel.setId(addressId);
                                addressModel.setAddress(userAddress);
                                addressModel.setAddress_title(addressTitle);
                                addressModel.setUser_phone(userPhone);
                                addressModel.setZipcode(zipCode);
                                addressModel.setCity_name(cityName);
                                addressModel.setCustomerCountry(customerCountry);
                                addressModel.setUser_locality(userLocality);
                                addressModel.setCustomerState(customerState);
                                addressModel.setCompany_street(companyStreet);
                                addressModel.setGustUserLandlineAdress(gustUserLandLineAddress);
                                addressModel.setGustUserFloor(gustUserFloor);
                                addressModel.setCompany_streetNo(company_streetNo);
                                addressModel.setaddress_direction(address_direction);
                                addressModelArrayList.add(addressModel);
                            addressDeleteAdapter = new AddressDeleteAdapter(UserAddressActivity.this, addressModelArrayList, UserAddressActivity.this);
                            rvUserAddressList.setAdapter(addressDeleteAdapter);


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String customerId = authPreference.getCustomerId();
                params.put("CustomerId", customerId);
                return params;
            }
        };
        requestQueue.add(strReq);
    }

    @Override
    public void addressDeleteItemClick(final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.address_delete_popup);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.diologIntertnet;
        dialog.setCanceledOnTouchOutside(true);
        Button cancelBtn = dialog.findViewById(R.id.btn_cancel);
        Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
        TextView tvDeleteMessage = (TextView) dialog.findViewById(R.id.tv_delete_message);
        tvDeleteMessage.setText(getResources().getString(R.string.address_delete_txt));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.justfoodz.utils.Network.isNetworkCheck(UserAddressActivity.this)) {
                    userAddressDelete(position);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();


                }
                dialog.dismiss();

            }
        });
        dialog.show();


    }

    @Override
    public void addressEditItemClick(final int position) {

        String userPhone = addressModelArrayList.get(position).getUser_phone();
        String addressTitle = addressModelArrayList.get(position).getAddress_title();
        String userFloor = addressModelArrayList.get(position).getGustUserFloor();
        String userStreetName = addressModelArrayList.get(position).getCompany_street();
        String city = addressModelArrayList.get(position).getCity_name();
        String state = addressModelArrayList.get(position).getCustomerState();
        String userZipCode = addressModelArrayList.get(position).getZipcode();
        String customerCountry = addressModelArrayList.get(position).getCustomerCountry();
        String address_direction = addressModelArrayList.get(position).getaddress_direction();

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_address_popup);

        edtEmail = (MaterialEditText) dialog.findViewById(R.id.edt_email);
        edt_address_direction = dialog.findViewById(R.id.edt_address_direction);
        edtUserAddressTitle =  dialog.findViewById(R.id.edt_address_name);
        edtFloorNumber = dialog.findViewById(R.id.edt_floor_number);
        edtCustomerStreetName = dialog.findViewById(R.id.edt_street_name);
        edtCustomerCity =  dialog.findViewById(R.id.edt_town_city);
        edtCustomerState = (MaterialEditText) dialog.findViewById(R.id.edt_country_region);
        edtCustomerPostcode = (MaterialEditText) dialog.findViewById(R.id.edt_postcode);
        edtCustomerCountry = (MaterialEditText) dialog.findViewById(R.id.edt_uk);
        edtCustomerPhoneNumber = (MaterialEditText) dialog.findViewById(R.id.edt_phone_number);
        Button tvSubmit = (Button) dialog.findViewById(R.id.tv_submit_txt);
        edtEmail.setText(authPreference.getUserEmail());
        edtEmail.setEnabled(false);
        edtUserAddressTitle.setText(addressTitle);
        edtFloorNumber.setText(userFloor);
        edtCustomerStreetName.setText(userStreetName);
        edtCustomerCity.setText(city);
        edtCustomerState.setText(state);
        edtCustomerPostcode.setText(userZipCode);
        edtCustomerCountry.setText(customerCountry);
        edtCustomerPhoneNumber.setText(userPhone);
        edt_address_direction.setText(address_direction);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateindexpos = position;
                addressModel = new AddressModel();
                addressModel = addressModelArrayList.get(updateindexpos);
                addressModel.setAddress_title(edtUserAddressTitle.getText().toString() + "");
                addressModel.setGustUserFloor(edtFloorNumber.getText().toString() + "");
                addressModel.setCompany_street(edtCustomerStreetName.getText().toString() + "");
                addressModel.setCity_name(edtCustomerCity.getText().toString() + "");
                addressModel.setCustomerState(edtCustomerState.getText().toString() + "");
                addressModel.setZipcode(edtCustomerPostcode.getText().toString() + "");
                addressModel.setCustomerCountry(edtCustomerCountry.getText().toString() + "");
                addressModel.setUser_phone(edtCustomerPhoneNumber.getText().toString() + "");
                addressModel.setaddress_direction(edt_address_direction.getText().toString() + "");
                addressModelArrayList.set(updateindexpos, addressModel);


                if (com.justfoodz.utils.Network.isNetworkCheck(UserAddressActivity.this)) {
                    editUserAddress(position);


                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();


                }
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        ha.removeCallbacks(null);



    }

    public void editUserAddress(final int position) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.UPDATE_USER_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.optInt("success");
                    if (success == 1) {
                        String successMsg = obj.optString("success_msg");
                        showCustomDialog(successMsg);


                    } else if (success == 0) {
                        String errorMsg = obj.optString("error_msg");
                       showCustomDialog(errorMsg);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone", addressModelArrayList.get(position).getUser_phone());
                params.put("customerAddressLabel", addressModelArrayList.get(position).getAddress_title());
                params.put("customerAppFloor", addressModelArrayList.get(position).getGustUserFloor());
                params.put("customerStreet", addressModelArrayList.get(position).getCompany_street());
                params.put("customerCity", addressModelArrayList.get(position).getCity_name());
                params.put("customerState", addressModelArrayList.get(position).getCustomerState());
                params.put("customerZipcode", addressModelArrayList.get(position).getZipcode());
                params.put("customerCountry", addressModelArrayList.get(position).getCustomerCountry());
                params.put("address_direction", addressModelArrayList.get(position).getaddress_direction());
                int customerAddressId = addressModelArrayList.get(position).getId();
                params.put("CustomerAddressId", customerAddressId + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }
    private void showCustomDialog(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                addressModelArrayList.clear();
                userAddressList();
            }
        });
        alertDialog.show();

    }

    public void userAddressDelete(final int position) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlConstants.USER_ADDRESS_DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.optInt("success");
                    if (success == 1) {
                        String successMsg = obj.getString("success_msg");
                        addressModelArrayList.remove(position);
                        addressDeleteAdapter.notifyItemRemoved(position);
                        addressDeleteAdapter.notifyDataSetChanged();
                        if (addressModelArrayList.isEmpty()) {
                            showCustomDialog1decline (successMsg,"1");
                        }
                    } else if (success == 0) {
                        String successMsg = obj.getString("success_msg");
                        showCustomDialog1decline (successMsg,"2");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
            pDialog.dismiss();
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                int customerAddressId = addressModelArrayList.get(position).getId();
                params.put("CustomerAddressId", customerAddressId + "");
                return params;
            }
        };
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
        super.onBackPressed();
    }

    private void showCustomDialog1decline (String s, final String todo)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(UserAddressActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (todo.equals("1"))
                {
                    Intent intent = new Intent(UserAddressActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                    finish();
//                    alertDialog.dismiss();
                }
                else if (todo.equals("3")){
                    addressDeleteAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }  else {
                    alertDialog.dismiss();
                }

            }
        });
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
